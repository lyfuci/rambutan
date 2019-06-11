package xyz.seansun.rambutan.autoconfig.wxmp.entrypoint

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import xyz.seansun.rambutan.service.WxMpMessageEntrypointService
import javax.annotation.Resource

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/9 12:41
 */
@RestController
@RequestMapping("\${wechat.mp.entrypoint-url}")
class WxMpEntrypointController {

    @Resource
    private lateinit var wxMpMessageEntrypointService: WxMpMessageEntrypointService

    /**
     * 微信校验接口
     */
    @GetMapping(produces = [MediaType.TEXT_PLAIN_VALUE])
    fun verifySignature(
        @RequestParam(required = false) signature: String,
        @RequestParam(required = false) timestamp: String,
        @RequestParam(required = false) nonce: String,
        @RequestParam(name = "echostr", required = false) echoStr: String
    ): String {
        return wxMpMessageEntrypointService.verifySignature(signature, timestamp, nonce, echoStr)
    }

    /**
     * 处理微信消息
     */
    @PostMapping(produces = [MediaType.APPLICATION_XML_VALUE])
    fun dealMsg(
        @RequestBody requestBody: String,
        @RequestParam("signature") signature: String,
        @RequestParam(name = "encrypt_type", required = false) encType: String,
        @RequestParam(name = "msg_signature", required = false) msgSignature: String,
        @RequestParam("timestamp") timestamp: String,
        @RequestParam("nonce") nonce: String
    ): Any {
        return wxMpMessageEntrypointService.dealMsg(requestBody, signature, encType, msgSignature, timestamp, nonce)
    }


}