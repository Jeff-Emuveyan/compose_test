package com.example.composetest.model.ui

import com.example.composetest.model.User

data class UiState (
    val isLoading: Boolean = false,
    val networkError: Boolean = false,
    val listOfUsers: List<User> = emptyList()
)