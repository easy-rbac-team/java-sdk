package com.uliian.easyrbac.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.labijie.caching.ICacheManager
import com.labijie.caching.TimePolicy
import com.uliian.easyrbac.config.DefaultInstance
import com.uliian.easyrbac.config.EasyRbacConfig
import com.uliian.easyrbac.dto.*
import com.uliian.easyrbac.exception.EasyRbacException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.HashMap

class EasyRbacService(private val easyRbacConfig: EasyRbacConfig,
                      private val okHttpClient: OkHttpClient = DefaultInstance.defaultOkHttpClient,
                      private val objectMapper: ObjectMapper = DefaultInstance.defaultJackson,
                      private val cache: ICacheManager = DefaultInstance.defaultCache) : IEasyRbacService {
    override fun getAppToken(): LoginResult {
        val key = "easyRbacToken"
        var tokenObj: LoginResult? = cache.get(key, LoginResult::class)
        if (tokenObj == null) {
            val path = "/sso/UserLogin"
            val request = HashMap<String, String>()
            request["userName"] = easyRbacConfig.appId
            request["password"] = easyRbacConfig.appSecret
            request["appCode"] = easyRbacConfig.appId
            val requestJson = this.objectMapper.writeValueAsString(request)
            var req = Request.Builder().url("${easyRbacConfig.url}/$path").post(RequestBody.create(DefaultInstance.JSON_TYPE, requestJson)).build()

            val result = callApi<LoginResult>(req)
            cache.set(key, result, result.expireIn * 1000L)
            tokenObj = result
        }

        return tokenObj
    }

    override fun getEasyRbacUserInfo(easyRbacUserToken: String): UserInfo {
        val path = "/app/user/$easyRbacUserToken"
        val req = Request.Builder().url("${this.easyRbacConfig.url}/$path").build()
        return this.callApi<UserInfo>(req)
    }

//    override fun generateToken(easyRbacUserToken: String): String {
//        val userInfo = this.getEasyRbacUserInfo(easyRbacUserToken)
//        return this.localTokenService.generateLocalToken(userInfo)
//    }

//    override fun getUserInfo(localToken: String): UserInfo {
//        return this.localTokenService.getUserInfoByLocalToken(localToken)
//    }

    override fun getUserResource(easyRbacToken: String): List<UserResource> {
        val key = "EasyRbac-Resource:$easyRbacToken"
        var resource = this.cache.get(key) as UserResourceList?
        if (resource == null || resource.isEmpty()) {
            val path = "app/resource/$easyRbacToken"
            val req = Request.Builder().url("${this.easyRbacConfig.url}/$path").header("authorization", "token $easyRbacToken").build()
            resource = this.callApi(req)
            this.cache.set(key, resource!!, 60_1000, TimePolicy.Sliding)
        }
        return resource
    }

    override fun hasPermission(easyRbacToken: String, resourceCode: String): Boolean {
        val resources = this.getUserResource(easyRbacToken)
        return this.hasItem(resources) { it.resourceCode == resourceCode }
    }

    private fun hasItem(resources: List<UserResource>, condition: (UserResource) -> Boolean): Boolean {
        for (item in resources) {
            if (item.resourceCode != null && condition.invoke(item)) {
                return true
            }
            if (item.children!!.isNotEmpty()) {
                if (hasItem(item.children!!, condition)) {
                    return true
                }
            }
        }
        return false
    }

    private inline fun <reified T> callApi(req: Request): T {
        val rsp = this.okHttpClient.newCall(req).execute()
        val json = rsp.body()
        if (!rsp.isSuccessful) {
            val errorMsg = this.objectMapper.readValue<ErrorMessage>(json!!.string())
            throw EasyRbacException(errorMsg.message, rsp.code())
        }

        val result = this.objectMapper.readValue<T>(rsp.body()!!.string())
        return result
    }
}