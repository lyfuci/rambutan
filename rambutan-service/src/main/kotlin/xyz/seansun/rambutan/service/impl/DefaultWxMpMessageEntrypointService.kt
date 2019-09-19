package xyz.seansun.rambutan.service.impl

import me.chanjar.weixin.mp.api.WxMpMessageRouter
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import xyz.seansun.rambutan.service.WxMpMessageEntrypointService


/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/9 1:12
 */
class DefaultWxMpMessageEntrypointService(
    private val wxMpService: WxMpService,
    private val wxMpMessageRouter: WxMpMessageRouter
) : WxMpMessageEntrypointService {
    private val log = LoggerFactory.getLogger(javaClass)
    override fun verifySignature(
        signature: String?,
        timestamp: String?,
        nonce: String?,
        echoStr: String?
    ): String {
        log.debug("接收到来自微信服务器的认证消息：[signature = $signature, timestamp = $timestamp, nonce = $nonce, echoStr = $echoStr]")
        //没有字段为空，并且签名检验合法
        return if (StringUtils.isNoneBlank(signature, timestamp, nonce, echoStr) &&
            wxMpService.checkSignature(timestamp, nonce, signature)
        ) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            log.debug("校验通过")
            echoStr ?: "success"
        } else {
            log.debug("非法请求")
            return "error"
        }

    }

    override fun dealMsg(
        requestBody: String,
        signature: String,
        encType: String?,
        msgSignature: String?,
        timestamp: String,
        nonce: String
    ): Any {
        log.debug(
            "接收微信请求：[signature = $signature, encType = $encType, msgSignature = $msgSignature,"
                    + " timestamp = $timestamp, nonce = $nonce, requestBody = \n$requestBody\n"
        )
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            throw IllegalArgumentException("非法请求，可能属于伪造的请求！")
        }
        var out = ""
        if (encType == null) {
            // 明文传输的消息
            val inMessage = WxMpXmlMessage.fromXml(requestBody)
            val outMessage = wxMpMessageRouter.route(inMessage) ?: return ""

            out = outMessage.toXml()
        } else if ("aes" == encType) {
            // aes加密的消息
            val inMessage = WxMpXmlMessage.fromEncryptedXml(
                requestBody,
                wxMpService.wxMpConfigStorage, timestamp, nonce, msgSignature
            )
            log.debug("消息解密后内容为：$inMessage ")
            val outMessage = this.wxMpMessageRouter.route(inMessage) ?: return ""

            out = outMessage.toEncryptedXml(wxMpService.wxMpConfigStorage)
        }

        log.debug("组装回复信息：$out")

        return out
    }


}