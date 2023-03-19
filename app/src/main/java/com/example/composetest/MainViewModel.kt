package com.example.composetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.composetest.data.UserRepository
import com.example.composetest.model.User
import com.example.composetest.model.UserResponse
import com.example.composetest.model.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (private val userRepository: UserRepository): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    /*fun getUsers(id: Int, noofRecords: Int) = viewModelScope.launch {
        _uiState.update { uiState -> uiState.copy(isLoading = true) }

        val result = userRepository.getUser(id, noofRecords)
        if (result == null) {
            _uiState.update { uiState -> uiState.copy(isLoading = false, networkError = true) }
        } else {
            _uiState.update { uiState -> uiState.copy(isLoading = false, networkError = false, listOfUsers = convertResponse(result)) }
        }
    }*/

    fun getUsers() = userRepository.getUsers().flow.cachedIn(viewModelScope)

    private fun convertResponse(list: List<UserResponse>): List<User> {
        val result: MutableList<User> = mutableListOf()
        list.forEach { result.add(it.toUser()) }
        return result
    }
}