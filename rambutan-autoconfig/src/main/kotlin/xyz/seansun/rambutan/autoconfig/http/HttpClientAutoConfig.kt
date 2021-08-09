package xyz.seansun.rambutan.autoconfig.http

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.util.MimeType
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.transport.ProxyProvider
import xyz.seansun.rambutan.properties.HttpClientProp
import java.nio.charset.StandardCharsets

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/9 2:37
 */
@Configuration
@ConditionalOnClass(WebClient::class)
@AutoConfigureAfter(
    CodecsAutoConfiguration::class,
    ClientHttpConnectorAutoConfiguration::class,
    JacksonAutoConfiguration::class
)
@ConditionalOnBean(ObjectMapper::class)
@EnableConfigurationProperties(HttpClientProp::class)
class HttpClientAutoConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Bean
    @ConditionalOnMissingBean
    fun webClient(httpClientProp: HttpClientProp): WebClient {
        log.debug("WebClient initialing")
        val httpClient: HttpClient
        if (httpClientProp.proxyEnable) {
            httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, httpClientProp.connectionTimeout)
                .doOnConnected { conn ->
                    conn
                        .addHandlerLast(ReadTimeoutHandler(httpClientProp.readTime))
                        .addHandlerLast(WriteTimeoutHandler(httpClientProp.readTime))
                }
                .proxy { proxyOptions ->
                    proxyOptions
                        .type(ProxyProvider.Proxy.HTTP)
                        .host(httpClientProp.proxyIp)
                        .port(httpClientProp.proxyPort)
                }

        } else {
            httpClient = HttpClient.create()
                .option(
                    ChannelOption.CONNECT_TIMEOUT_MILLIS, httpClientProp.connectionTimeout
                )
                .doOnConnected { conn ->
                    conn
                        .addHandlerLast(ReadTimeoutHandler(httpClientProp.readTime))
                        .addHandlerLast(WriteTimeoutHandler(httpClientProp.readTime))
                }
        }
        val httpConnector = ReactorClientHttpConnector(httpClient)

        val strategies = ExchangeStrategies
            .builder()
            .codecs { clientDefaultCodecsConfigurer ->
                clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonEncoder(
                    Jackson2JsonEncoder(
                        objectMapper,
                        MimeType("text", "html", StandardCharsets.UTF_8)
                    )
                )
                clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonDecoder(
                    Jackson2JsonDecoder(
                        objectMapper,
                        MimeType("text", "html", StandardCharsets.UTF_8)
                    )
                )
            }.build()


        return WebClient.builder()
            .clientConnector(httpConnector)
            .exchangeStrategies(strategies)
            .build()
    }
}