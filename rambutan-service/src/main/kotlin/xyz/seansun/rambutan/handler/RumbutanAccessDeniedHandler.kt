package xyz.seansun.rambutan.handler

import org.apache.juli.logging.LogFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/8 18:41
 */
class RumbutanAccessDeniedHandler : AccessDeniedHandler {
    val log = LogFactory.getLog(javaClass)
    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        log.error("access denied", accessDeniedException)
        response!!.writer.write("access denied")
    }

}