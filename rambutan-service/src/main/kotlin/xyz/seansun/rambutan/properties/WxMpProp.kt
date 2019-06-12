package xyz.seansun.rambutan.properties


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.http.HttpMethod

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/8 18:45
 */
@ConfigurationProperties("wechat.mp")
class WxMpProp {
    /**
     * 对应微信公众号 id
     */
    var appId: String? = null
    /**
     * 微信公众号 secret
     */
    var secret: String? = null
    /**
     * 微信公众号 token
     */
    var token: String? = null
    /**
     * aesKey 消息加密秘钥
     */
    var aesKey: String? = null
    /**
     * if Enable Http Proxy
     */
    var httpProxyEnable: Boolean = false
    /**
     * http proxy host
     */
    var httpProxyHost: String? = "localhost"
    /**
     * http proxy port
     */
    var httpProxyPort: Int? = 8080
    /**
     * http proxy username
     */
    var httpProxyUsername: String? = null
    /**
     * http proxy password
     */
    var httpProxyPassword: String? = null
    /**
     * 消息接入url,默认已经配置好了消息接入，只需要使用router直接接入消息即可
     */
    var entrypointUrl: String = "/wechat/mp"
    /**
     * 鉴权相关配置
     */
    val authentication = WxMpAuthenticationProp()

    class WxMpAuthenticationProp {
        /**
         * 是否开启认证
         */
        var enable: Boolean = false
        /**
         * 具有登录功能的路径
         * 必须手动指定登录路径（应该和微信菜单项一致）
         */
        var loginPaths: List<AuthorizedRequest>? = arrayListOf()
        /**
         * 需要登录才能访问的路径，支持ant通配符
         * 启用后默认过滤所有路径
         */
        var authenticationRequiredPaths: List<AuthorizedRequest>? = arrayListOf()
        /**
         * 仅配置改选项拦截器不生效，需同时配置 authenticationRequiredPaths 属性
         * 不需要登录认真就能访问的路径，支持ant通配符
         */
        var authenticationExcludePaths: List<AuthorizedRequest>? = arrayListOf()
        /**
         * 是否开启csrf
         */
        var csrfEnable: Boolean = false

    }

    data class AuthorizedRequest(
        /**
         * 访问路径
         */
        var url: String? = null,
        /**
         * 允许访问的方法
         */
        var method: HttpMethod? = null
    ) {
        constructor(url: String?) : this(url, null)


    }

}
