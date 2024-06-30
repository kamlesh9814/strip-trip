package com.app.stripstrips.viewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.stripstrips.network.Repository

class ViewModelFactory(
    private val application: Application = Application(),
    private val repository: Repository = Repository(),
) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(com.app.stripstrips.viewModel.ViewModel::class.java) -> {
                return ViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}