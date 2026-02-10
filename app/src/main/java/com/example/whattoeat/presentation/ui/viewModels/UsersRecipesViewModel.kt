package com.example.whattoeat.presentation.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.whattoeat.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class UsersRecipesViewModel @Inject constructor (
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
): ViewModel()