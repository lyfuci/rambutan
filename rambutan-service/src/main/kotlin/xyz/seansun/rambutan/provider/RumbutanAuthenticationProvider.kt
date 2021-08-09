package xyz.seansun.rambutan.provider

import me.chanjar.weixin.mp.api.WxMpService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.util.ObjectUtils
import xyz.seansun.rambutan.model.WxMpOauthCodeToken

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/9 0:28
 */
class RumbutanAuthenticationProvider(val wxMpService: WxMpService) : AuthenticationProvider {
    val log = LoggerFactory.getLogger(javaClass)

    override fun authenticate(authentication: Authentication?): Authentication {
        //获取过滤器封装的token信息
        val authenticationToken = authentication as WxMpOauthCodeToken

        //有授权码的请况下直接获取用户相关的信息
        log.debug("使用 code[{}] 获取token", authentication.principal)

        val token = wxMpService.oAuth2Service.getAccessToken(authenticationToken.principal as String)
        log.debug("使用 token[{}] 获取用户基本信息", authentication.principal)

        val oAuth2UserInfo = wxMpService.oAuth2Service.getUserInfo(token, null)
        val wxMpUser = wxMpService.userService.userInfo(oAuth2UserInfo.openid)
        log.debug("认证的用户信息[{}]", wxMpUser)

        if (ObjectUtils.isEmpty(wxMpUser)) {
            log.error("微信响应数据错误,wxMpUser为空")
            throw AuthenticationServiceException("数据请求异常，请联系管理员")
        }

        //通过校验构造token
        val authenticationResult = WxMpOauthCodeToken(wxMpUser)
        authenticationResult.details = authenticationToken.details

        return authenticationResult

    }

    override fun supports(authentication: Class<*>?): Boolean {
        return WxMpOauthCodeToken::class.java.isAssignableFrom(authentication)
    }


}