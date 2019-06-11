package xyz.seansun.rambutan.autoconfig

import org.springframework.context.annotation.Import
import xyz.seansun.rambutan.autoconfig.wxmp.WxMpAutoConfig

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/10 22:13
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention
@Import(WxMpAutoConfig::class)
annotation class EnableWechatWork