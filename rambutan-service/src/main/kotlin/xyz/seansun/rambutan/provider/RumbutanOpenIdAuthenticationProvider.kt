package xyz.seansun.rambutan.provider

import me.chanjar.weixin.common.error.WxErrorException
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.result.WxMpUser
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.util.ObjectUtils
import xyz.seansun.rambutan.model.WxMpOpenIdToken

/**
 * created by [ 孙琦夫 ](mailto:sunqifu@szunicom.com)
 * on : 2019-02-26 09:36
 */
class RumbutanOpenIdAuthenticationProvider(val wxMpService: WxMpService) : AuthenticationProvider {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        log.info("RumbutanOpenIdAuthenticationProvider loading ...")
    }

    override fun authenticate(authentication: Authentication): Authentication {
        //获取过滤器封装的token信息
        val authenticationToken = authentication as WxMpOpenIdToken

        //有授权码的请况下直接获取用户相关的信息
        log.debug("使用 openId [${authentication.principal}] 获取用户基本信息")
        var user: WxMpUser? = null
        try {
            user = wxMpService.userService.userInfo(authentication.principal as String)
        } catch (e: WxErrorException) {
            log.error("请求微信侧获取openId为[${authentication.principal}]的用户信息异常", e)
            throw AuthenticationServiceException("request not comes from wechat, we have record your IP address to prevent bad behavior")
        }

//        检查openId是否正常获取到用户信息
        if (ObjectUtils.isEmpty(user)) {
            log.error("微信响应数据错误,或者此openId[{}]非本公众号openId", authentication.principal)
            throw AuthenticationServiceException("数据请求异常，请联系管理员")
        }

        //然后使用openId查看数据库是否已经有该用户的注册信息
        log.debug("认证的用户信息为 [{}]", user)

        //通过
        val authenticationResult = WxMpOpenIdToken(user)
        authenticationResult.details = authenticationToken.details

        return authenticationResult
    }

    /**
     * 根据token类型，来判断使用哪个Provider
     */
    override fun supports(authentication: Class<*>): Boolean {
        return WxMpOpenIdToken::class.java.isAssignableFrom(authentication)
    }

}
