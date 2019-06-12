package xyz.seansun.rambutan.properties

import org.springframework.boot.context.properties.ConfigurationProperties


/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/8 18:45
 */
@ConfigurationProperties("http-client")
class HttpClientProp {
    var proxyEnable: Boolean = false
    var proxyIp: String = "localhost"
    var proxyPort: Int = 8080
    var connectionTimeout: Int = 1000
    var readTime: Int = 1000
}