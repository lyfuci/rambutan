package xyz.seansun.rambutan.service

/** wxmp消息接入服务
 *
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/9 1:10
 */
interface WxMpMessageEntrypointService {
    /**
     * 验证微信公众号签名
     */
    fun verifySignature(
        signature: String?,
        timestamp: String?,
        nonce: String?,
        echoStr: String?
    ): String

    /**
     * 验证微信公众号消息
     */
    fun dealMsg(
        requestBody: String,
        signature: String,
        encType: String?,
        msgSignature: String?,
        timestamp: String,
        nonce: String
    ): Any
}