package xyz.seansun.rambutan.model

import me.chanjar.weixin.mp.bean.result.WxMpUser
import org.apache.commons.logging.LogFactory
import org.springframework.security.authentication.AbstractAuthenticationToken

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/8 18:41
 */
open class WxMpStringToken : AbstractAuthenticationToken {
    private val log = LogFactory.getLog(javaClass)

    private val principal: Any

    constructor(code: String) : super(null) {
        this.principal = code
        this.isAuthenticated = false
    }

    constructor(principal: WxMpUser) : super(null) {
        this.principal = principal
        this.isAuthenticated = true
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any {
        return this.principal
    }


}