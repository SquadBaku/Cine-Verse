package com.karrar.movieapp.ui.profile

import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.usecases.CheckIfLoggedInUseCase
import com.karrar.movieapp.domain.usecases.GetAccountDetailsUseCase
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val accountUIStateMapper: AccountUIStateMapper,
    private val checkIfLoggedInUseCase: CheckIfLoggedInUseCase
) : BaseViewModel() {

    private val _profileDetailsUIState = MutableStateFlow(ProfileUIState())
    val profileDetailsUIState = _profileDetailsUIState.asStateFlow()

    private val _profileUIEvent: MutableStateFlow<Event<ProfileUIEvent?>> =
        MutableStateFlow(Event(null))
    val profileUIEvent = _profileUIEvent.asStateFlow()

    init {
        getData()
    }

    override fun getData() {
        getProfileDetails()
    }

    private fun getProfileDetails() {
        if (checkIfLoggedInUseCase()) {
            _profileDetailsUIState.update {
                it.copy(isLoading = true, isLoggedIn = true, error = false)
            }

            viewModelScope.launch {
                try {
                    val accountDetails = accountUIStateMapper.map(getAccountDetailsUseCase())
                    _profileDetailsUIState.update {
                        it.copy(
                            avatarPath = accountDetails.avatarPath,
                            name = accountDetails.name,
                            username = accountDetails.username,
                            isLoading = false
                        )
                    }
                } catch (t: Throwable) {
                    _profileDetailsUIState.update {
                        it.copy(isLoading = false, error = true)
                    }
                }
            }
        } else {
            _profileDetailsUIState.update {
                it.copy(isLoggedIn = false)
            }
        }
    }

    fun onClickRatedMovies() {
        _profileUIEvent.update { Event(ProfileUIEvent.RatedMoviesEvent) }
    }

    fun onClickLogout() {
        _profileUIEvent.update { Event(ProfileUIEvent.DialogLogoutEvent) }
    }

    fun onClickWatchHistory() {
        _profileUIEvent.update { Event(ProfileUIEvent.WatchHistoryEvent) }
    }

    fun onClickMyCollections() {
        _profileUIEvent.update { Event(ProfileUIEvent.MyCollectionsEvent) }
    }

    fun onClickProfileCard() {
        _profileUIEvent.update {
            Event(
                if (profileDetailsUIState.value.isLoggedIn)
                    ProfileUIEvent.EditProfileEvent
                else
                    ProfileUIEvent.LoginEvent
            )
        }
    }
}