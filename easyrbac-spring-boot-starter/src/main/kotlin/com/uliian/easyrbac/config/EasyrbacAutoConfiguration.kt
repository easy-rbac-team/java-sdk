package com.uliian.easyrbac.config

import com.uliian.easyrbac.service.EasyRbacService
import com.uliian.easyrbac.service.IEasyRbacService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration

class EasyrbacAutoConfiguration {
    @Bean
    @ConfigurationProperties("easyrbac")
    @ConditionalOnMissingBean(EasyRbacConfig::class)
    fun easyRbacConfig(): EasyRbacConfig {
        return EasyRbacConfig()
    }


    @Bean
    @ConditionalOnMissingBean(IEasyRbacService::class)
    fun easyRbacService(easyRbacConfig: EasyRbacConfig): IEasyRbacService {
        return EasyRbacService(easyRbacConfig)
    }
}