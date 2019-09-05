package com.uliian.easyrbac.dto

import java.util.ArrayList

class UserResource {
    private val RESOURCE = 1
    private val MENU = 2
    private val PUBLIC = 3


    var id: String? = null

    var applicationId: Long = 0

    var resourceName: String? = null

    var resourceCode: String? = null

    var url: String? = null

    var resourceType: Int = 0

    var iconUrl: String? = null

    var paramaters: String? = null

    var describe: String? = null


    val isResource: Boolean
        get() = this.resourceType and RESOURCE == RESOURCE

    val isMenu: Boolean
        get() = this.resourceType and MENU == MENU

    val isPublic: Boolean
        get() = this.resourceType and PUBLIC == PUBLIC


    var children: List<UserResource>? = null

    init {
        this.children = ArrayList()
    }
}
