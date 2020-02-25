package com.uliian.easyrbac.dto

open class UserInfo {
    var id: Long = 0
    var userName: String? = null
    var realName: String? = null
    var mobilePhone: String? = null
    var isEnable: Boolean = false
    var easyRbacToken: String = ""
    var roles:List<String> = arrayListOf()
}
