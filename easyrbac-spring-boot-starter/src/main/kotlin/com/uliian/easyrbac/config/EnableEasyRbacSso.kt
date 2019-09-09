package com.uliian.easyrbac.config

import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(SsoAuthConfiguration::class)
annotation class EnableEasyRbacSso