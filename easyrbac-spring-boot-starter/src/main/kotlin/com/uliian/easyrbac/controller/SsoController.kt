package com.uliian.easyrbac.demo.controller

import com.uliian.easyrbac.auth.Auth
import com.uliian.easyrbac.dto.UserInfo
import com.uliian.easyrbac.dto.UserResource
import com.uliian.easyrbac.service.IEasyRbacService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/sso")
class SsoController(private val easyRbacService: IEasyRbacService) {

    @GetMapping("/test")
    fun test() {
        println("test")
    }

    @Value("\${jwt.key}")
    private val jwtToken: String? = null

    @PostMapping
    fun getAuthInfo(@RequestParam token: String, request: HttpServletRequest): String {
        println(request.getHeader("Authorization"))
//        val easyRbacUserInfo = this.easyRbacService.getEasyRbacUserInfo(token)
//        val adminUserInfo = AdminUserInfo(easyRbacUserInfo.id, easyRbacUserInfo.easyRbacToken)
//        adminUserInfo.enable = true
//        adminUserInfo.mobilePhone = easyRbacUserInfo.mobilePhone
//        adminUserInfo.name = easyRbacUserInfo.name
//        adminUserInfo.realName = easyRbacUserInfo.realName
//        val result = adminUserInfo.generateJwt(jwtToken!!, 12 * 60 * 60)
//        return result
        return this.easyRbacService.generateToken(token)
    }

    @Auth(noCheckPermission = true,resourceCode = "")
    @GetMapping("user_menu")
    fun getUserResource(request: HttpServletRequest,userInfo: UserInfo): List<UserResource> {
        val userResource = this.easyRbacService.getUserResource(userInfo.easyRbacToken)
//        userResource.toTree()
//        val arrayList = ArrayList<UserResource>()
//        userResource.toOneResources(arrayList)
//        arrayList.toTree()
        return userResource
    }
}