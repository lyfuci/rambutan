package xyz.seansun.rambutan.entrypoint

import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import xyz.seansun.rambutan.utils.ServletUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/8 18:41
 */
class RambutanAuthenticationEntryPoint : AuthenticationEntryPoint {
    val log = LoggerFactory.getLogger(this.javaClass)
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        log.error("可能为伪造请求,该请求的ip为: ${ServletUtils.getIpAddress(request!!)}")
        response!!.status = HttpServletResponse.SC_OK
        response.writer.write("request not comes from wechat, we have record your IP address to prevent bad behavior")
    }

}