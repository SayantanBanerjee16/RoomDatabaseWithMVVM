package com.sayantanbanerjee.roomdatabasedemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayantanbanerjee.roomdatabasedemo.db.SubscriberRepository
import java.lang.IllegalArgumentException

// As we need to pass objects to the View Model through a constructor, we need to define a custom View model factory class.
class SubscriberViewModelFactory(private val repository: SubscriberRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubscriberViewModel::class.java)) {
            return SubscriberViewModel(repository) as T
        }
        throw IllegalArgumentException("Invalid View Model Class")
    }
}
