package xyz.seansun.rambutan.autoconfig.wxmp.basic

import me.chanjar.weixin.mp.api.WxMpConfigStorage
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import xyz.seansun.rambutan.properties.WxMpProp

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/9 11:11
 */
@Configuration
class InMemoryStorageConfig {

    @Bean
    @ConditionalOnMissingBean(WxMpConfigStorage::class)
    fun wxMpConfigStorage(wxMpProp: WxMpProp): WxMpConfigStorage {
        val wxMpInMemoryConfigStorage = WxMpInMemoryConfigStorage()
        wxMpInMemoryConfigStorage.appId = wxMpProp.appId
        wxMpInMemoryConfigStorage.secret = wxMpProp.secret
        wxMpProp.aesKey?.let {
            wxMpInMemoryConfigStorage.aesKey = it
        }
        wxMpProp.token?.let {
            wxMpInMemoryConfigStorage.token = it
        }

        wxMpInMemoryConfigStorage.oauth2redirectUri = wxMpInMemoryConfigStorage.oauth2redirectUri
        if (wxMpProp.httpProxyEnable) {
            wxMpInMemoryConfigStorage.httpProxyHost = wxMpProp.httpProxyHost
            wxMpInMemoryConfigStorage.httpProxyPort = wxMpProp.httpProxyPort ?: 8080
            wxMpInMemoryConfigStorage.httpProxyUsername = wxMpProp.httpProxyUsername
            wxMpInMemoryConfigStorage.httpProxyPassword = wxMpProp.httpProxyPassword
        }
        return wxMpInMemoryConfigStorage
    }

}