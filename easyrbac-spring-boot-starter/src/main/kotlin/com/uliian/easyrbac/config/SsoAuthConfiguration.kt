package com.uliian.easyrbac.config

import com.uliian.easyrbac.auth.EasyRbacAuthHandler
import com.uliian.easyrbac.auth.RegistAuthHandler
import com.uliian.easyrbac.controller.SsoController
import com.uliian.easyrbac.exception.EasyRbacException
import com.uliian.easyrbac.service.IEasyRbacService
import com.uliian.easyrbac.service.ILocalTokenService
import com.uliian.easyrbac.service.JwtTokenService
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext

@Configuration
class SsoAuthConfiguration {
    @Bean
    @ConfigurationProperties("easyrbac.jwt")
    fun jwtConfig():JwtConfig{
        return JwtConfig()
    }

    @Bean
    @ConditionalOnMissingBean(ILocalTokenService::class)
    @ConditionalOnBean(JwtConfig::class)
    fun jwtTokenService(jwtConfig: JwtConfig): ILocalTokenService {
        if (jwtConfig.key == null) {
            throw EasyRbacException("缺少配置项：token-config.key", 500)
        }
        return JwtTokenService(jwtConfig.key!!,jwtConfig.schema.toString())
    }

    @Bean
    @ConditionalOnMissingBean(SsoController::class)
    fun ssoController(easyRbacService: IEasyRbacService,localTokenService: ILocalTokenService): SsoController {
        return SsoController(easyRbacService,localTokenService)
    }

    @Bean
    @ConditionalOnBean(JwtConfig::class)
    fun easyRbacAuthHandler(easyRbacConfig: EasyRbacConfig, easyRbacService: IEasyRbacService, jwtConfig: JwtConfig,localTokenService: ILocalTokenService): EasyRbacAuthHandler {
        return EasyRbacAuthHandler(easyRbacConfig,jwtConfig,easyRbacService,localTokenService)
    }

    @Bean
    @ConditionalOnMissingBean(RegistAuthHandler::class)
    fun registAuthHandler(authHandler: EasyRbacAuthHandler): RegistAuthHandler {
        return RegistAuthHandler(authHandler)
    }
}