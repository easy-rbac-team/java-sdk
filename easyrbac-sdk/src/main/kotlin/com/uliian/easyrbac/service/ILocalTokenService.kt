package com.uliian.easyrbac.service

import com.uliian.easyrbac.dto.UserInfo

/**
 * 本地Token服务
 */
interface ILocalTokenService {
    /**
     * 根据用户信息生成Token
     */
    fun generateLocalToken(userInfo: UserInfo): String

    /**
     * 根据本地Token获取用户信息
     */
    fun getUserInfoByLocalToken(token: String): UserInfo
}