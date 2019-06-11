package xyz.seansun.rambutan.properties


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.http.HttpMethod

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/8 18:45
 */
@ConfigurationProperties("wechat.mp")
data class WxMpProp(
    /**
     * 对应微信公众号 id
     */
    var appId: String,
    /**
     * 微信公众号 secret
     */
    var secret: String,
    /**
     * 微信公众号 token
     */
    var token: String,
    /**
     * aesKey 消息加密秘钥
     */
    var aesKey: String,
    /**
     * entrypoint-url
     */
    /**
     * if Enable Http Proxy
     */
    var httpProxyEnable: Boolean = false,
    /**
     * http proxy host
     */
    var httpProxyHost: String,
    /**
     * http proxy port
     */
    var httpProxyPort: Int,
    /**
     * http proxy username
     */
    var httpProxyUsername: String,
    /**
     * http proxy password
     */
    var httpProxyPassword: String,
    /**
     * 消息接入url,默认已经配置好了消息接入，只需要使用router直接接入消息即可
     */
    var entrypointUrl: String,
    /**
     * 鉴权相关配置
     */
    @NestedConfigurationProperty
    var authentication: WxMpAuthenticationProp
) {


    data class WxMpAuthenticationProp(
        /**
         * 是否开启认证
         */
        var enable: Boolean = false,
        /**
         * 具有登录功能的路径
         * 必须手动指定登录路径（应该和微信菜单项一致）
         */
        var loginPaths: List<AuthorizedRequest>,
        /**
         * 需要登录才能访问的路径，支持ant通配符
         * 启用后默认过滤所有路径
         */
        var authenticationRequiredPaths: List<AuthorizedRequest>,
        /**
         * 仅配置改选项拦截器不生效，需同时配置 authenticationRequiredPaths 属性
         * 不需要登录认真就能访问的路径，支持ant通配符
         */
        var authenticationExcludePaths: List<AuthorizedRequest>,
        /**
         * 是否开启csrf
         */
        var csrfEnable: Boolean = false

    )

    data class AuthorizedRequest(
        /**
         * 访问路径
         */
        var url: String,
        /**
         * 允许访问的方法
         */
        var method: HttpMethod?
    ) {
        constructor(url: String) : this(url, null)
    }

}
