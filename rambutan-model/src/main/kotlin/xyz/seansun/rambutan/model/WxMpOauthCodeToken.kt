package xyz.seansun.rambutan.model

import me.chanjar.weixin.mp.bean.result.WxMpUser
import org.apache.commons.logging.LogFactory

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/8 18:41
 */
class WxMpOauthCodeToken : WxMpStringToken {
    private val log = LogFactory.getLog(javaClass)

    constructor(code: String) : super(code) {
        log.debug("WxMpOauthCodeToken setAuthenticated ->false loading ...")
    }

    constructor(principal: WxMpUser) : super(principal) {
        log.debug("WxMpOauthCodeToken setAuthenticated ->false loading ...")
    }


}