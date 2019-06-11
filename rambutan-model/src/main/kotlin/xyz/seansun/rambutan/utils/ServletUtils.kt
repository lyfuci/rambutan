package xyz.seansun.rambutan.utils

import javax.servlet.http.HttpServletRequest

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/8 18:41
 */
class ServletUtils {
    companion object {
        private const val unknown = "unknown"
        fun getIpAddress(request: HttpServletRequest): String {
            return sequenceOf(
                "x-forwarded-for",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
            ).filter {
                !request.getHeader(it).isNullOrEmpty() && unknown != request.getHeader(it)
            }
                .map { request.getHeader(it) }
                .first()
                ?: request.remoteAddr
                ?: unknown
        }
    }
}