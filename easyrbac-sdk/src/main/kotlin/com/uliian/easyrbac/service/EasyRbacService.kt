package com.uliian.easyrbac.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.labijie.caching.ICacheManager
import com.labijie.caching.TimePolicy
import com.uliian.easyrbac.config.DefaultInstance
import com.uliian.easyrbac.config.EasyRbacConfig
import com.uliian.easyrbac.dto.*
import com.uliian.easyrbac.exception.EasyRbacException
import okhttp3.MediaType
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
            var req = Request.Builder().url("${easyRbacConfig.url}$path").post(RequestBody.create(DefaultInstance.JSON_TYPE, requestJson)).build()

            val resultJson = callApi(req)!!
            val result = this.objectMapper.readValue(resultJson,LoginResult::class.java)
            cache.set(key, result, result.expireIn * 1000L)
            tokenObj = result
        }

        return tokenObj!!
    }

    override fun getEasyRbacUserInfo(easyRbacUserToken: String): UserInfo {
        val path = "/app/user/$easyRbacUserToken"
        val appToken = this.getAppToken()
        val req = Request.Builder()
                .url("${this.easyRbacConfig.url}$path")
                .header("Authorization","${appToken.schema} ${appToken.token}")
                .build()
        val resultJson = this.callApi(req)
        val result = this.objectMapper.readValue(resultJson,UserInfo::class.java)
        result.easyRbacToken = easyRbacUserToken
        return result
    }

    override fun getUserResource(easyRbacToken: String): List<UserResource> {
        val key = "EasyRbac-Resource:$easyRbacToken"
        var resource = this.cache.get(key) as UserResourceList?
        if (resource == null || resource.isEmpty()) {
            val path = "app/resource/$easyRbacToken"
            val resourceJson = callAuthApi<Any>(path,"GET")
            val resource = this.objectMapper.readValue<List<UserResource>>(resourceJson!!)
            this.cache.set(key, resource!!, 60_1000, TimePolicy.Sliding)
        }
        return resource!!
    }

    protected fun <TIn> callAuthApi(path: String,method:String,body: TIn?=null): String? {
        val appToken = this.getAppToken()
        val req = Request.Builder()
                .url("${this.easyRbacConfig.url}/$path")
                .header("Authorization", "${appToken.schema} ${appToken.token}")
        if(body!=null){
            val json = this.objectMapper.writeValueAsString(body)
            val body = RequestBody.create(MediaType.get("application/json; charset=UTF-8"),json)
            req.method(method,body)
        }else{
            req.method(method, RequestBody.create(null,ByteArray(0)))
        }
        return callApi(req.build())
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

    protected fun callApi(req: Request): String? {
        val rsp = this.okHttpClient.newCall(req).execute()
        val json = rsp.body()
        if (!rsp.isSuccessful) {
            val errorMsg = this.objectMapper.readValue<ErrorMessage>(json!!.string())
            throw EasyRbacException(errorMsg.message, rsp.code())
        }
        return rsp.body()?.string()

    }

    override fun createUser(addUserDto:AddUserDto):Long{
        val path ="user"
        val userIdStr = callAuthApi(path,"POST", addUserDto)
        return this.objectMapper.readValue<Long>(userIdStr!!)
    }

    override fun addUserToOneRole(dto:AddUserOneRoleDto){
        val path = "Role/${dto.roleId}/user/${dto.userId}"
        this.callAuthApi<Any>(path,"POST")
    }

    override fun removeUserFromOneGroup(dto:RemoveUserFromRole){
        val path = "Role/${dto.roleId}/user/${dto.userId}"
        this.callAuthApi<Any>(path,"DELETE")
    }
}