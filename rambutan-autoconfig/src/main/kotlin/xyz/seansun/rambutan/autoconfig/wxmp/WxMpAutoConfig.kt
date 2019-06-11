package xyz.seansun.rambutan.autoconfig.wxmp

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import xyz.seansun.rambutan.autoconfig.wxmp.basic.WxMpBasicAutoConfig
import xyz.seansun.rambutan.autoconfig.wxmp.entrypoint.EntryPointAutoConfig
import xyz.seansun.rambutan.autoconfig.wxmp.security.WxMpSecurityAutoConfiguration
import xyz.seansun.rambutan.properties.WxMpProp
import javax.servlet.Servlet

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/9 3:15
 */
@Configuration
@EnableConfigurationProperties(WxMpProp::class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(Servlet::class, DispatcherServlet::class, WebMvcConfigurer::class)
@Import(WxMpBasicAutoConfig::class, WxMpSecurityAutoConfiguration::class, EntryPointAutoConfig::class)
class WxMpAutoConfig