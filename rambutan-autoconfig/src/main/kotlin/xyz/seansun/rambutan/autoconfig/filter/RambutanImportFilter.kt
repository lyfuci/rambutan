package xyz.seansun.rambutan.autoconfig.filter

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata

/**
 * created by <a href="mailto:1194458432@qq.com" > lyfuci </a>
 * on : 2019/6/9 22:46
 */
class RambutanImportFilter : AutoConfigurationImportFilter {

    override fun match(
        autoConfigurationClasses: Array<out String>?,
        autoConfigurationMetadata: AutoConfigurationMetadata?
    ): BooleanArray {
        return autoConfigurationClasses!!.asSequence()
            .map { !TO_EXCLUDE.contains(it) }
            .toList()
            .toBooleanArray()
    }

    companion object {
        val TO_EXCLUDE =
            listOf("org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration")
    }


}