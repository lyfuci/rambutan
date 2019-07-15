package xyz.seansun.rambutan.filter

import org.apache.juli.logging.LogFactory
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.Authentication
import xyz.seansun.rambutan.model.WxMpOpenIdToken
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * created by <a href="mailto:sunqifu@szunicom.com" > 孙琦夫 </a>
 * on : 2019-07-14 14:13
 */
class RumbutanOpenIdAuthenticationFilter : RumbutanAuthFilter {
    constructor(urlPattern: String?) : super(urlPattern)

    constructor (urlPattern: String?, httpMethod: String) : super(urlPattern, httpMethod)

    private val log = LogFactory.getLog(javaClass)

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        if (postOnly && request!!.method != "POST") {
            throw AuthenticationServiceException(
                "Authentication method not supported: ${request.method}"
            )
        }
        //没有登录按流程处理
        var openId = obtainOpenId(request!!)

        if (openId.isNullOrBlank()) {
            log.error("参数为null，可能为伪造请求,该请求的X-Forwarded-For为[${request.getHeader("X-Forwarded-For")}],IP为[${request.remoteAddr}}]")
            throw InsufficientAuthenticationException("request not comes from wechat, we have record your IP address to prevent bad behavior")
        }

        openId = openId.trim { it <= ' ' }

        val authRequest = WxMpOpenIdToken(openId)

        setDetails(request, authRequest)
        return authenticationManager.authenticate(authRequest)

    }


}