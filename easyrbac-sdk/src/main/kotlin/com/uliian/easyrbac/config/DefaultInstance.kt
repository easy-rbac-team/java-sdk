package com.uliian.easyrbac.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.labijie.caching.memory.MemoryCacheManager
import com.labijie.caching.memory.MemoryCacheOptions
import okhttp3.MediaType
import okhttp3.OkHttpClient

object DefaultInstance {
    val defaultOkHttpClient = OkHttpClient()

    val defaultCache = MemoryCacheManager(MemoryCacheOptions());

    val JSON_TYPE: MediaType = MediaType.get("application/json; charset=utf-8");

    val defaultJackson = ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())
}