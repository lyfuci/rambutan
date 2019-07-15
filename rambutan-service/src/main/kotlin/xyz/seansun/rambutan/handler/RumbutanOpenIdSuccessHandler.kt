package xyz.seansun.rambutan.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/8 18:41
 */
class RumbutanOpenIdSuccessHandler(val mapper: ObjectMapper) : AuthenticationSuccessHandler {


    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        response!!.contentType = MediaType.APPLICATION_JSON_UTF8_VALUE
        val result = String.format(
            "{ \"code\": 0,  \"msg\": \"success\", \"data\": %s  }",
            mapper.writeValueAsString(authentication?.principal)
        )
        response.writer.write(result)
    }
}