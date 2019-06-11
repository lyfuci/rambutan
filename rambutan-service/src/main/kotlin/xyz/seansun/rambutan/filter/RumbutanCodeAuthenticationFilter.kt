package xyz.seansun.rambutan.filter

import org.apache.juli.logging.LogFactory
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import xyz.seansun.rambutan.model.WxMpToken
import xyz.seansun.rambutan.utils.ServletUtils
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/8 18:41
 */
class RumbutanCodeAuthenticationFilter :
    AbstractAuthenticationProcessingFilter {

    constructor(urlPattern: String) : super(AntPathRequestMatcher(urlPattern))

    constructor (urlPattern: String, httpMethod: String) : super(AntPathRequestMatcher(urlPattern, httpMethod))

    private val log = LogFactory.getLog(javaClass)

    companion object {
        private const val SPRING_SECURITY_PARAM_CODE_KEY = "code"
    }

    var postOnly: Boolean = false

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        if (postOnly && request!!.method != "POST") {
            throw AuthenticationServiceException(
                "Authentication method not supported: " + request.method
            )
        }
        if (SecurityContextHolder.getContext()?.authentication?.isAuthenticated == true) {
            return SecurityContextHolder.getContext().authentication
        }
        var oauth2Code: String? = obtainOauth2Code(request!!)
        log.debug("oauth2.0 code为: $oauth2Code")

        if (oauth2Code.isNullOrBlank()) {
            log.error("oauth2.0 code为空，可能为伪造请求,该请求的X-Forwarded-For为$oauth2Code,IP为${ServletUtils.getIpAddress(request)}")
            throw InsufficientAuthenticationException("bad request, we have record your IP address.")
        }

        oauth2Code = oauth2Code.trim()

        val authRequest = WxMpToken(oauth2Code)
        setDetails(request, authRequest)

        return authenticationManager.authenticate(authRequest)
    }


    private fun obtainOauth2Code(request: HttpServletRequest): String {
        return request.getParameter(SPRING_SECURITY_PARAM_CODE_KEY)
    }

    private fun setDetails(
        request: HttpServletRequest,
        authRequest: WxMpToken
    ) {

        val detail = HashMap<String, String>()
        detail["X-Real-IP"] = request.getHeader("X-Real-IP")
        detail["X-Forwarded-For"] = request.getHeader("X-Forwarded-For")
        detail["IP"] = ServletUtils.getIpAddress(request)
        detail["SESSION-ID"] = request.getSession(false)?.id ?: "unknown"

        authRequest.details = detail
    }
}
