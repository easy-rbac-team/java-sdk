package com.uliian.easyrbac.dto

data class AddUserDto (val userName:String,val password:String,val realName:String,val confirmPassword:String,val mobilePhone:String?)

data class AddUserOneRoleDto(val userId:Long, val roleId:Long)

data class RemoveUserFromRole(val userId: Long, val roleId: Long)