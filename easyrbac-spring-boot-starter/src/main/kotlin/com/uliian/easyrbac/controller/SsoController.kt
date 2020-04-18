package com.uliian.easyrbac.controller

import com.uliian.easyrbac.auth.Auth
import com.uliian.easyrbac.dto.LoginRequest
import com.uliian.easyrbac.dto.UserInfo
import com.uliian.easyrbac.dto.UserResource
import com.uliian.easyrbac.service.IEasyRbacService
import com.uliian.easyrbac.service.ILocalTokenService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/sso")
class SsoController(private val easyRbacService: IEasyRbacService,private val localTokenService: ILocalTokenService) {
    @GetMapping("/local_login")
    fun getAuthInfo(easyRbacToken: String, request: HttpServletRequest): String {
//        println(request.getHeader("Authorization"))
        val userInfo = this.easyRbacService.getEasyRbacUserInfo(easyRbacToken)
        return this.localTokenService.generateLocalToken(userInfo)
    }

    @Auth(noCheckPermission = true,resourceCode = "")
    @GetMapping("/user_menu")
    fun getUserResource(request: HttpServletRequest,userInfo: UserInfo): List<UserResource> {
        val userResource = this.easyRbacService.getUserResource(userInfo.easyRbacToken)
        return userResource
    }

    @PostMapping("/user_login")
    fun loginDirect(req:LoginRequest):String{
        val loginResult = this.easyRbacService.directLogin(req)
        val userInfo = this.easyRbacService.getEasyRbacUserInfo(loginResult.token)
        return this.localTokenService.generateLocalToken(userInfo)
    }
}