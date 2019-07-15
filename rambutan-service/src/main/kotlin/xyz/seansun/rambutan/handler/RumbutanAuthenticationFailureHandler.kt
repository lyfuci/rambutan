package xyz.seansun.rambutan.handler

import org.apache.juli.logging.LogFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/8 18:41
 */
class RumbutanAuthenticationFailureHandler : AuthenticationFailureHandler {

    private val log = LogFactory.getLog(javaClass)
    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException
    ) {
        log.error("登录失败", exception)
        response!!.writer.write(exception.message ?: "登录失败")
    }

}
