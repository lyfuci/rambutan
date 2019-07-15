package xyz.seansun.rambutan.filter

import org.apache.juli.logging.LogFactory
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import xyz.seansun.rambutan.model.WxMpOauthCodeToken
import xyz.seansun.rambutan.utils.ServletUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/8 18:41
 */
class RumbutanCodeAuthenticationFilter : RumbutanAuthFilter {

    constructor(urlPattern: String?) : super(urlPattern)

    constructor (urlPattern: String?, httpMethod: String) : super(urlPattern, httpMethod)

    private val log = LogFactory.getLog(javaClass)

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        if (postOnly && request!!.method != "POST") {
            throw AuthenticationServiceException(
                "Authentication method not supported: ${request.method}"
            )
        }
        if (SecurityContextHolder.getContext()?.authentication?.isAuthenticated == true) {
            log.debug("already loged in, use exist authentication.")
            return SecurityContextHolder.getContext().authentication
        }
        var oauth2Code: String? = obtainOauth2Code(request!!)
        log.debug("oauth2.0 code为: $oauth2Code")

        if (oauth2Code.isNullOrBlank()) {
            log.error("oauth2.0 code为空，可能为伪造请求,该请求的X-Forwarded-For为$oauth2Code,IP为${ServletUtils.getIpAddress(request)}")
            throw InsufficientAuthenticationException("bad request, we have record your IP address.")
        }

        oauth2Code = oauth2Code.trim()

        val authRequest = WxMpOauthCodeToken(oauth2Code)
        setDetails(request, authRequest)
        return authenticationManager.authenticate(authRequest)
    }

}
