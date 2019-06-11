package xyz.seansun.rambutan.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.Proxy


/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/8 18:45
 */
@ConfigurationProperties("http-client")
data class HttpClientProp(
    var proxyEnable: Boolean,
    var proxyIp: String,
    var proxyPort: Int,
    var proxyType: Proxy.Type,
    var connectionTimeout: Int,
    var readTime: Int
)