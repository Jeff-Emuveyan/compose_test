package com.example.composetest.model

data class User(val id: Int,
                        val imageUrl: String?,
                        val firstName: String?,
                        val lastName: String?,
                        val email: String?,
                        val contactNumber: String?,
                        val age: Int?,
                        val dob: String?
)