package com.example.composetest.model

import com.example.composetest.model.local.UserEntity

data class UserResponse(val id: Int?,
                        val imageUrl: String?,
                        val firstName: String?,
                        val lastName: String?,
                        val email: String?,
                        val contactNumber: String?,
                        val age: Int?,
                        val dob: String?
                        ) {

    fun toUser(): User {
        return User(
            id = id ?: 0,
            imageUrl = imageUrl ?: "",
            firstName = firstName ?: "",
            lastName = lastName ?: "",
            email = email ?: "",
            contactNumber = contactNumber ?: "",
            age = age ?: 0,
            dob = dob ?: "")
    }

    fun toUserEntity(): UserEntity {
        return UserEntity(
            id = id ?: 0,
            imageUrl = imageUrl ?: "",
            firstName = firstName ?: "",
            lastName = lastName ?: "",
            email = email ?: "",
            contactNumber = contactNumber ?: "",
            age = age ?: 0,
            dob = dob ?: "")
    }
}