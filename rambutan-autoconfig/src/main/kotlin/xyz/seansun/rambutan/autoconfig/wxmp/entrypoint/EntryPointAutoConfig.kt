package xyz.seansun.rambutan.autoconfig.wxmp.entrypoint

import me.chanjar.weixin.mp.api.WxMpMessageRouter
import me.chanjar.weixin.mp.api.WxMpService
import org.apache.juli.logging.LogFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import xyz.seansun.rambutan.service.WxMpMessageEntrypointService
import xyz.seansun.rambutan.service.impl.DefaultWxMpMessageEntrypointService

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/9 12:28
 */
@Configuration
@ConditionalOnProperty(prefix = "wechat.mp", name = ["token", "aes-key", "entrypoint-url"])
@ConditionalOnBean(WxMpService::class)
class EntryPointAutoConfig {
    private val log = LogFactory.getLog(javaClass)

    @Bean
    @ConditionalOnMissingBean(WxMpMessageRouter::class)
    fun wxMpMessageRouter(wxMpService: WxMpService): WxMpMessageRouter {
        log.debug("initialing WxMpMessageRouter...")
        return WxMpMessageRouter(wxMpService)
    }

    @Bean
    @ConditionalOnMissingBean(WxMpMessageEntrypointService::class)
    fun wxMpMessageEntrypointService(
        wxMpService: WxMpService,
        wxMpMessageRouter: WxMpMessageRouter
    ): WxMpMessageEntrypointService {
        log.debug("initialing DefaultWechatWorkService...")
        return DefaultWxMpMessageEntrypointService(wxMpService, wxMpMessageRouter)
    }

    @Bean
    @ConditionalOnMissingBean(WxMpEntrypointController::class)
    fun wechatWorkEntryPointController(): WxMpEntrypointController {
        log.info("initializing wechat entrypoint controller...")
        return WxMpEntrypointController()
    }

}