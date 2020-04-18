package com.uliian.easyrbac.service

import com.uliian.easyrbac.dto.*

interface IEasyRbacService {
    fun getAppToken(): LoginResult

    fun getEasyRbacUserInfo(easyRbacUserToken: String): UserInfo

//     fun generateToken(easyRbacUserToken: String): String

//     fun getUserInfo(localToken: String): UserInfo

    fun getUserResource(easyRbacToken: String): List<UserResource>

    fun hasPermission(easyRbacToken: String, resourceCode: String): Boolean

    fun createUser(addUserDto: AddUserDto): Long

    fun addUserToOneRole(dto: AddUserOneRoleDto)

    fun removeUserFromOneGroup(dto: RemoveUserFromRole)
    fun disableUser(userId: Long)
    fun enableUser(userId: Long)
    fun getEasyRbacUserInfo(userId: Long): UserInfo

    fun directLogin(login:LoginRequest):LoginResult
}