package xyz.seansun.rambutan.filter

import org.slf4j.LoggerFactory
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import xyz.seansun.rambutan.model.WxMpStringToken
import xyz.seansun.rambutan.utils.ServletUtils
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * created by <a href="mailto:sunqifu@szunicom.com" > 孙琦夫 </a>
 * on : 2019-07-14 14:13
 */
abstract class RumbutanAuthFilter : AbstractAuthenticationProcessingFilter {
    protected constructor(urlPattern: String?) : super(AntPathRequestMatcher(urlPattern))

    protected constructor (urlPattern: String?, httpMethod: String) : super(
        AntPathRequestMatcher(
            urlPattern,
            httpMethod
        )
    )

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val SPRING_SECURITY_PARAM_CODE_KEY = "code"
        private const val SPRING_SECURITY_PARAM_USER_ID_KEY = "openId"
    }

    protected var postOnly: Boolean = false

    protected fun obtainOauth2Code(request: HttpServletRequest): String? {
        return request.getParameter(SPRING_SECURITY_PARAM_CODE_KEY)
    }

    protected fun obtainOpenId(request: HttpServletRequest): String? {
        return request.getParameter(SPRING_SECURITY_PARAM_USER_ID_KEY)
    }

    protected fun setDetails(
        request: HttpServletRequest,
        authRequest: WxMpStringToken
    ) {

        val detail = HashMap<String, String?>()
        detail["X-Real-IP"] = request.getHeader("X-Real-IP")
        detail["X-Forwarded-For"] = request.getHeader("X-Forwarded-For")
        detail["IP"] = ServletUtils.getIpAddress(request) ?: "unknown"
        detail["SESSION-ID"] = request.getSession(false)?.id ?: "unknown"

        authRequest.details = detail
    }

}