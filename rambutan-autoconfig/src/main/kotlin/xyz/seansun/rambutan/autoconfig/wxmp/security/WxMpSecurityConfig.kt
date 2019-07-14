package xyz.seansun.rambutan.autoconfig.wxmp.security

import com.fasterxml.jackson.databind.ObjectMapper
import me.chanjar.weixin.mp.api.WxMpService
import org.apache.juli.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.util.Assert
import org.springframework.util.ObjectUtils
import org.springframework.web.filter.CharacterEncodingFilter
import xyz.seansun.rambutan.entrypoint.RambutanAuthenticationEntryPoint
import xyz.seansun.rambutan.filter.RumbutanCodeAuthenticationFilter
import xyz.seansun.rambutan.filter.RumbutanOpenIdAuthenticationFilter
import xyz.seansun.rambutan.handler.RumbutanAccessDeniedHandler
import xyz.seansun.rambutan.handler.RumbutanAuthenticationFailureHandler
import xyz.seansun.rambutan.handler.RumbutanOauthCodeSuccessHandler
import xyz.seansun.rambutan.handler.RumbutanOpenIdSuccessHandler
import xyz.seansun.rambutan.properties.WxMpProp
import xyz.seansun.rambutan.provider.RumbutanAuthenticationProvider
import xyz.seansun.rambutan.provider.RumbutanOpenIdAuthenticationProvider
import java.nio.charset.StandardCharsets

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/9 3:16
 */
@Configuration
@ConditionalOnBean(ObjectMapper::class)
class WxMpSecurityConfig : WebSecurityConfigurerAdapter() {

    private val log = LogFactory.getLog(javaClass)

    @Autowired
    lateinit var wxMpProp: WxMpProp
    @Autowired
    lateinit var wxMpService: WxMpService
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Bean
    @ConditionalOnMissingBean(AuthenticationFailureHandler::class)
    fun authenticationFailureHandler(): AuthenticationFailureHandler {
        log.debug("initialing authenticationFailureHandler")
        return RumbutanAuthenticationFailureHandler()
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler::class)
    fun accessDeniedHandler(): AccessDeniedHandler {
        log.debug("initialing accessDeniedHandler")
        return RumbutanAccessDeniedHandler()
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint::class)
    fun authenticationEntryPoint(): AuthenticationEntryPoint {
        log.debug("initialing authenticationEntryPoint")
        return RambutanAuthenticationEntryPoint()
    }

    @Bean
    @ConditionalOnProperty(prefix = "wechat.mp.authentication", name = ["enableOpenIdLogin"])
    @ConditionalOnMissingBean(RumbutanOpenIdSuccessHandler::class)
    fun rumbutanOpenIdSuccessHandler(): RumbutanOpenIdSuccessHandler {
        log.debug("initialing OpenIdSuccessHandler")
        return RumbutanOpenIdSuccessHandler(objectMapper)
    }

    @Bean
    @ConditionalOnMissingBean(RumbutanOauthCodeSuccessHandler::class)
    fun rumbutanOauthCodeSuccessHandler(): RumbutanOauthCodeSuccessHandler {
        log.debug("initialing RumbutanOauthCodeSuccessHandler")
        return RumbutanOauthCodeSuccessHandler()
    }


    @Throws(Exception::class)
    public override fun configure(http: HttpSecurity) {
        log.info("configuring spring security")
        http
            .logout().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint())
            .accessDeniedHandler(accessDeniedHandler())
        if (wxMpProp.authentication.enable) {

            //处理post情况下错误信息乱码
            val encodingFilter = CharacterEncodingFilter()
            encodingFilter.encoding = StandardCharsets.UTF_8.name()
            encodingFilter.setForceEncoding(true)
            http.addFilterBefore(encodingFilter, CsrfFilter::class.java)

            if (!wxMpProp.authentication.loginPaths.isNullOrEmpty()) {


                for (urlPattern in wxMpProp.authentication.loginPaths!!) {
                    //添加登录方式和处理器

                    val lycheeCodeAuthenticationFilter = if (ObjectUtils.isEmpty(urlPattern.method)) {
                        RumbutanCodeAuthenticationFilter(urlPattern.url)
                    } else {
                        RumbutanCodeAuthenticationFilter(urlPattern.url, urlPattern.method!!.name)
                    }

                    lycheeCodeAuthenticationFilter.setAuthenticationManager(this.authenticationManagerBean())
                    lycheeCodeAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler())
                    lycheeCodeAuthenticationFilter.setAuthenticationSuccessHandler(rumbutanOauthCodeSuccessHandler())

                    val lycheeAuthenticationProvider = RumbutanAuthenticationProvider(wxMpService)

                    http.authenticationProvider(lycheeAuthenticationProvider)
                        .addFilterBefore(
                            lycheeCodeAuthenticationFilter,
                            UsernamePasswordAuthenticationFilter::class.java
                        )
                    log.info("adding wechat work login filter to mvc context and listed paths could do login action :\n$urlPattern")
                }
            }

            if (wxMpProp.authentication.enableOpenIdLogin) {
                //伪造用户登录路径
                Assert.notNull(wxMpProp.authentication.openIdLoginPath, "伪造路径为空的情况下不能启用伪造用户相关接口")

                val rumbutanOpenIdAuthenticationFilter =
                    RumbutanOpenIdAuthenticationFilter(wxMpProp.authentication.openIdLoginPath)
                rumbutanOpenIdAuthenticationFilter.setAuthenticationManager(this.authenticationManagerBean())
                rumbutanOpenIdAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler())
                rumbutanOpenIdAuthenticationFilter.setAuthenticationSuccessHandler(rumbutanOpenIdSuccessHandler())

                val rumbutanOpenIdAuthenticationProvider = RumbutanOpenIdAuthenticationProvider(wxMpService)
                http.authenticationProvider(rumbutanOpenIdAuthenticationProvider)
                    .addFilterBefore(
                        rumbutanOpenIdAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter::class.java
                    )
                log.info(
                    "faker login filter to mvc context enable, following path allow user faker user :${wxMpProp.authentication.openIdLoginPath}"
                )
            }


            if (!wxMpProp.authentication.authenticationExcludePaths.isNullOrEmpty()) {
                for (request in wxMpProp.authentication.authenticationExcludePaths!!) {
                    http.authorizeRequests()
                        .antMatchers(request.method, request.url)
                        .permitAll()
                }
                log.info("listed paths exclued from authentication \n${wxMpProp.authentication.authenticationExcludePaths}")
            }

            if (!ObjectUtils.isEmpty(wxMpProp.authentication.authenticationRequiredPaths)) {

                for (request in wxMpProp.authentication.authenticationRequiredPaths!!) {
                    http.authorizeRequests()
                        .antMatchers(request.method, request.url)
                        .authenticated()

                }
                log.info("adding wechat work authentication filter to mvc context and listed paths need authentication \n${wxMpProp.authentication.authenticationRequiredPaths}")
            }

        } else {
            http.authorizeRequests()
                .antMatchers("/**")
                .permitAll()
        }

        //是否开启csrf
        if (wxMpProp.authentication.enable && wxMpProp.authentication.csrfEnable) {
            log.info("enable csrf cookie token")
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        } else {
            http.csrf().disable()
        }
    }

}