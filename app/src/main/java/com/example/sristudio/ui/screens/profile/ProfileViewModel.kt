package com.example.sristudio.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sristudio.data.local.entity.UserAccount
import com.example.sristudio.data.repository.HealthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: UserAccount? = null,
    val isDarkMode: Boolean = false,
    val useMetric: Boolean = true,
    val showEditDialog: Boolean = false,
    val editName: String = "",
    val editAge: String = "",
    val editWeight: String = "",
    val editHeight: String = "",
    val editGender: String = "Not specified"
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: HealthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeCurrentUser().collect { user ->
                _uiState.update { it.copy(
                    user = user,
                    editName = user?.displayName ?: it.editName.ifBlank { "" },
                    editAge = if ((user?.age ?: 0) > 0) user?.age.toString() else it.editAge.ifBlank { "" },
                    editWeight = if ((user?.weightKg ?: 0.0) > 0) user?.weightKg.toString() else it.editWeight.ifBlank { "" },
                    editHeight = if ((user?.heightCm ?: 0.0) > 0) user?.heightCm.toString() else it.editHeight.ifBlank { "" },
                    editGender = user?.gender ?: it.editGender
                ) }
            }
        }
        viewModelScope.launch {
            repository.preferences.isDarkMode.collect { isDark ->
                _uiState.update { it.copy(isDarkMode = isDark) }
            }
        }
        viewModelScope.launch {
            repository.preferences.useMetric.collect { metric ->
                _uiState.update { it.copy(useMetric = metric) }
            }
        }
    }

    fun showEditDialog() {
        _uiState.value = _uiState.value.copy(showEditDialog = true)
    }

    fun hideEditDialog() {
        _uiState.value = _uiState.value.copy(showEditDialog = false)
    }

    fun onEditNameChange(v: String) { _uiState.value = _uiState.value.copy(editName = v) }
    fun onEditAgeChange(v: String) { _uiState.value = _uiState.value.copy(editAge = v.filter { it.isDigit() }) }
    fun onEditWeightChange(v: String) { _uiState.value = _uiState.value.copy(editWeight = v) }
    fun onEditHeightChange(v: String) { _uiState.value = _uiState.value.copy(editHeight = v) }
    fun onEditGenderChange(v: String) { _uiState.value = _uiState.value.copy(editGender = v) }

    fun saveProfile() {
        val state = _uiState.value
        val userId = state.user?.id ?: return
        viewModelScope.launch {
            repository.updateProfile(
                userId = userId,
                name = state.editName,
                age = state.editAge.toIntOrNull() ?: 0,
                weight = state.editWeight.toDoubleOrNull() ?: 0.0,
                height = state.editHeight.toDoubleOrNull() ?: 0.0,
                gender = state.editGender
            )
            hideEditDialog()
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { repository.preferences.setDarkMode(enabled) }
    }

    fun setUseMetric(metric: Boolean) {
        viewModelScope.launch { repository.preferences.setUseMetric(metric) }
    }

    fun logout() {
        viewModelScope.launch { repository.logout() }
    }
}
