package com.uliian.easyrbac.dto

import com.fasterxml.jackson.annotation.JsonIgnore

class UserInfo {
    var id: Long = 0
    var userName: String? = null
    var realName: String? = null
    var mobilePhone: String? = null
    var isEnable: Boolean = false
    var easyRbacToken: String = ""
}
