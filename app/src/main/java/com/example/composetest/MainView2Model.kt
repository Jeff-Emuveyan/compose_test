package com.example.composetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.composetest.data.UserRepository
import com.example.composetest.model.User
import com.example.composetest.model.UserResponse
import com.example.composetest.model.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainView2Model @Inject constructor (private val userRepository: UserRepository): ViewModel() {

    fun getUsers(id: Int) = userRepository.getUsersWithLocalPagination(id).flow.map { it.map { it.toUser() } }

}