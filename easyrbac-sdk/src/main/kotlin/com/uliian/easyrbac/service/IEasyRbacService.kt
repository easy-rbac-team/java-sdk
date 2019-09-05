package com.uliian.easyrbac.service

import com.uliian.easyrbac.dto.LoginResult
import com.uliian.easyrbac.dto.UserInfo
import com.uliian.easyrbac.dto.UserResource

interface IEasyRbacService {
     fun getAppToken(): LoginResult

     fun getEasyRbacUserInfo(easyRbacUserToken: String): UserInfo

     fun generateToken(easyRbacUserToken: String): String

     fun getUserInfo(localToken: String): UserInfo

     fun getUserResource(easyRbacToken: String): List<UserResource>

     fun hasPermission(easyRbacToken: String, resourceCode: String): Boolean
}