package xyz.seansun.rambutan.autoconfig.wxmp.basic

import me.chanjar.weixin.mp.api.WxMpConfigStorage
import me.chanjar.weixin.mp.api.WxMpInRedisConfigStorage
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.ObjectUtils
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import xyz.seansun.rambutan.properties.WxMpProp

import java.time.temporal.ChronoUnit.NANOS

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/9 11:13
 */
@Configuration
@ConditionalOnClass(RedisProperties::class, JedisPool::class, JedisPoolConfig::class)
@EnableConfigurationProperties(RedisProperties::class)
class JedisStorageConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    @ConditionalOnMissingBean(JedisPool::class)
    fun jedisPool(redisProperties: RedisProperties): JedisPool {
        val jedisPoolConfig = JedisPoolConfig()
        jedisPoolConfig.maxIdle = 30000
        jedisPoolConfig.maxWaitMillis = 1000
        return JedisPool(
            jedisPoolConfig,
            redisProperties.host,
            redisProperties.port,
            (if (redisProperties.timeout == null) 1000 else redisProperties.timeout.get(NANOS) / 1000).toInt(),
            redisProperties.password,
            redisProperties.database,
            "rumbutan-redis-client"
        )
    }

    @Bean
    @ConditionalOnMissingBean(WxMpConfigStorage::class)
    fun wxMpConfigStorage(wxMpProp: WxMpProp, jedisPool: JedisPool): WxMpConfigStorage {
        log.debug("WxMpInRedisConfigStorage initializing")
        if (ObjectUtils.isEmpty(wxMpProp.appId) || ObjectUtils.isEmpty(wxMpProp.secret)) {
            throw RuntimeException("appid、secret都不能为空")
        }
        val wxMpConfigStorage = WxMpInRedisConfigStorage(jedisPool)

        wxMpConfigStorage.appId = wxMpProp.appId
        wxMpConfigStorage.secret = wxMpProp.secret

        wxMpConfigStorage.aesKey = wxMpProp.aesKey
        wxMpConfigStorage.token = wxMpProp.token

        if (wxMpProp.httpProxyEnable) {
            wxMpConfigStorage.httpProxyHost = wxMpProp.httpProxyHost
            wxMpConfigStorage.httpProxyPort = wxMpProp.httpProxyPort ?: 8080
            wxMpConfigStorage.httpProxyUsername = wxMpProp.httpProxyUsername
            wxMpConfigStorage.httpProxyPassword = wxMpProp.httpProxyPassword
        }
        return wxMpConfigStorage

    }
}