package com.uliian.easyrbac.config

import com.uliian.easyrbac.auth.EasyRbacAuthHandler
import com.uliian.easyrbac.auth.RegistAuthHandler
import com.uliian.easyrbac.demo.controller.SsoController
import com.uliian.easyrbac.exception.EasyRbacException
import com.uliian.easyrbac.service.EasyRbacService
import com.uliian.easyrbac.service.IEasyRbacService
import com.uliian.easyrbac.service.ILocalTokenService
import com.uliian.easyrbac.service.JwtTokenService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext

@Configuration

class EasyrbacAutoConfiguration {
    @Bean
    @ConfigurationProperties("easyrbac")
    @ConditionalOnMissingBean(EasyRbacConfig::class)
    fun easyRbacConfig(): EasyRbacConfig {
        return EasyRbacConfig()
    }

    @Bean
    @ConditionalOnMissingBean(ILocalTokenService::class)
    fun jwtTokenService(easyRbacConfig: EasyRbacConfig): ILocalTokenService {
        if (easyRbacConfig.localTokenConfig?.key == null) {
            throw EasyRbacException("缺少配置项：token-config.key", 500)
        }
        return JwtTokenService(easyRbacConfig.localTokenConfig!!.key!!)
    }

    @Bean
    @ConditionalOnMissingBean(IEasyRbacService::class)
    fun easyRbacService(easyRbacConfig: EasyRbacConfig, localTokenService: ILocalTokenService): IEasyRbacService {
        return EasyRbacService(easyRbacConfig, localTokenService = localTokenService)
    }

    @Bean
    @ConditionalOnMissingBean(SsoController::class)
    fun ssoController(webApplicationContext: AbstractRefreshableWebApplicationContext): SsoController {
        return webApplicationContext.beanFactory.createBean(SsoController::class.java)
    }

    @Bean
    @ConditionalOnMissingBean(EasyRbacAuthHandler::class)
    fun easyRbacAuthHandler(easyRbacConfig: EasyRbacConfig, easyRbacService: IEasyRbacService): EasyRbacAuthHandler {
        return EasyRbacAuthHandler(easyRbacConfig,easyRbacService)
    }

    @Bean
    @ConditionalOnMissingBean(RegistAuthHandler::class)
    fun registAuthHandler(authHandler: EasyRbacAuthHandler): RegistAuthHandler {
        return RegistAuthHandler(authHandler)
    }
}