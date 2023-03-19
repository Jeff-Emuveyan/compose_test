package com.example.composetest.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.composetest.model.User

@Entity(tableName = "users")
class UserEntity (@PrimaryKey val id: Int = 1,
                  val imageUrl: String?,
                  val firstName: String?,
                  val lastName: String?,
                  val email: String?,
                  val contactNumber: String?,
                  val age: Int?,
                  val dob: String?
)
{
    fun toUser(): User {
        return User(id, imageUrl, firstName, lastName, email, contactNumber, age, dob)
    }


}
