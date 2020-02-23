package com.uliian.easyrbac

import com.uliian.easyrbac.config.EasyRbacConfig
import com.uliian.easyrbac.dto.AddUserDto
import com.uliian.easyrbac.dto.AddUserOneRoleDto
import com.uliian.easyrbac.dto.RemoveUserFromRole
import com.uliian.easyrbac.service.EasyRbacService
import org.junit.Assert
import org.junit.Test

class EasyRbacServiceTest {
    val easyRbacService:EasyRbacService
    init {
        val config = EasyRbacConfig()


        this.easyRbacService = EasyRbacService(config)
    }

    @Test
    fun createUserTest(){
        val result = this.easyRbacService.createUser(AddUserDto("test1","123456777","test","123456777","15198777777"))
        println(result)
        Assert.assertNotNull(result)
    }

    @Test
    fun addUserToRoleTest(){
        this.easyRbacService.addUserToOneRole(AddUserOneRoleDto(1365362215690961930,1365364880718169098))
    }

    @Test
    fun removeUserFromRole(){
        this.easyRbacService.removeUserFromOneGroup(RemoveUserFromRole(1365362215690961930,1365364880718169098))
    }
}