package xyz.seansun.rambutan.autoconfig.wxmp.basic

import me.chanjar.weixin.mp.api.WxMpConfigStorage
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.ObjectUtils
import xyz.seansun.rambutan.properties.WxMpProp

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/9 11:11
 */
@Configuration
class InMemoryStorageConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    @ConditionalOnMissingBean(WxMpConfigStorage::class)
    fun wxMpConfigStorage(wxMpProp: WxMpProp): WxMpConfigStorage {
        log.debug("WxMpInMemoryConfigStorage initializing")
        if (ObjectUtils.isEmpty(wxMpProp.appId) || ObjectUtils.isEmpty(wxMpProp.secret)) {
            throw RuntimeException("appid、secret都不能为空")
        }
        val wxMpInMemoryConfigStorage = WxMpInMemoryConfigStorage()
        wxMpInMemoryConfigStorage.appId = wxMpProp.appId
        wxMpInMemoryConfigStorage.secret = wxMpProp.secret
        wxMpProp.aesKey?.let {
            wxMpInMemoryConfigStorage.aesKey = it
        }
        wxMpProp.token?.let {
            wxMpInMemoryConfigStorage.token = it
        }

        if (wxMpProp.httpProxyEnable) {
            wxMpInMemoryConfigStorage.httpProxyHost = wxMpProp.httpProxyHost
            wxMpInMemoryConfigStorage.httpProxyPort = wxMpProp.httpProxyPort ?: 8080
            wxMpInMemoryConfigStorage.httpProxyUsername = wxMpProp.httpProxyUsername
            wxMpInMemoryConfigStorage.httpProxyPassword = wxMpProp.httpProxyPassword
        }
        return wxMpInMemoryConfigStorage
    }

}