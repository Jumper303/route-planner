package com.jumper.routeplanner.components

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
@PropertySource("classpath:application.properties")
class ConfigProperties {

    @Autowired
    private val env: Environment? = null

    fun getConfigValue(configKey: String?): String? {
        return env!!.getProperty(configKey)
    }
}