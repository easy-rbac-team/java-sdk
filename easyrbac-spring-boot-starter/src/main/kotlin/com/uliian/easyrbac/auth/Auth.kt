package com.uliian.easyrbac.auth

annotation class Auth(val resourceCode:String,val noCheckPermission: Boolean=false)