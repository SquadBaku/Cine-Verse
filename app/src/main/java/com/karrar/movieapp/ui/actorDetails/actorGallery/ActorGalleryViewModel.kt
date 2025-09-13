package com.karrar.movieapp.ui.actorDetails.actorGallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.usecases.GetActorImagesUseCase
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActorGalleryViewModel @Inject constructor(
    state: SavedStateHandle,
    private val getActorImagesUseCase: GetActorImagesUseCase
) : BaseViewModel() {

    val args = ActorGalleryFragmentArgs.fromSavedStateHandle(state)

    private val _uiState = MutableStateFlow(ActorGalleryUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getActorImages()
    }

    private fun getActorImages() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val images = getActorImagesUseCase(args.id) // بيرجع List<String> links
                _uiState.update {
                    it.copy(
                        actorImages = images,
                        isLoading = false,
                        isSuccess = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    fun onClickBack() {
        _uiState.update { it.copy(backEvent = Event(true)) }
    }

    override fun getData() {
        TODO("Not yet implemented")
    }
}
