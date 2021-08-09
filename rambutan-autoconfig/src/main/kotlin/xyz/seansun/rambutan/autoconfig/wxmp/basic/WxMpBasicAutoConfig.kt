package xyz.seansun.rambutan.autoconfig.wxmp.basic

import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl
import me.chanjar.weixin.mp.config.WxMpConfigStorage
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/9 11:44
 */
@Configuration
@ConditionalOnProperty(prefix = "wechat.mp", name = ["app-id", "secret"])
@Import(InMemoryStorageConfig::class)
class WxMpBasicAutoConfig {
    private val log = LoggerFactory.getLogger(javaClass)
    @Bean
    @ConditionalOnMissingBean(WxMpService::class)
    fun wxMpService(wxMpConfigStorage: WxMpConfigStorage): WxMpService {
        log.info("initializing wxMpService bean...")
        val wxMpService = WxMpServiceImpl()
        wxMpService.wxMpConfigStorage = wxMpConfigStorage
        return wxMpService
    }
}