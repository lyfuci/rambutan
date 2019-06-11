package xyz.seansun.rambutan.autoconfig.wxmp.security

import me.chanjar.weixin.mp.api.WxMpService
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import xyz.seansun.rambutan.autoconfig.wxmp.basic.WxMpBasicAutoConfig

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/9 22:39
 */
@Configuration
@ConditionalOnBean(WxMpService::class)
@AutoConfigureAfter(WxMpBasicAutoConfig::class)
@Import(WxMpSecurityConfig::class)
class WxMpSecurityAutoConfiguration
