package com.benope.verbose.spoon.core_backend.common.config

import org.modelmapper.ModelMapper
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource


@Configuration
class BeanConfig {

    @Bean
    fun modelMapper(): ModelMapper {
        val modelMapper = ModelMapper()
        modelMapper
            .configuration
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
        return modelMapper
    }

    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setDefaultEncoding("UTF-8") // 인코딩 설정
        messageSource.setBasenames(
            "classpath:message/messages",
            "classpath:org/springframework/security/messages"
        )
        return messageSource
    }


}