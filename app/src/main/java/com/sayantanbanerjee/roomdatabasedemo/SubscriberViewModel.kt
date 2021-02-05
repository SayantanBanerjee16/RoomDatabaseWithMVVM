package com.sayantanbanerjee.roomdatabasedemo

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayantanbanerjee.roomdatabasedemo.db.Subscriber
import com.sayantanbanerjee.roomdatabasedemo.db.SubscriberRepository
import kotlinx.coroutines.launch

// Subscriber View Model class.
class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel(), Observable {

    // 2-way data binding for the following variables. That is change in the UI side is reflected here
    // and also change in the variable is directly reflected in the UI.
    // [inputName], [inputEmail], [saveOrUpdateButtonText], [clearAllOrDeleteButtonText] are all 2-way Data Binding.
    @Bindable
    val inputName = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    // This variable is directly connected to the repository subscribers which in turns calls all the list items from the DAO interface.
    // Therefore this Live-data variable holds all the items of the database.
    // This data is observed in the [MainActivity.kt] to display the whole list in the UI.
    val subscriber = repository.subscribers

    // Initializing the variables
    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    // Inserting/Updating into the database.
    fun saveOrUpdate() {
        val name = inputName.value!!
        val email = inputEmail.value!!
        insert(Subscriber(0, name, email))
        inputName.value = null
        inputEmail.value = null
    }

    // Deleting a particular or all the items from the database.
    fun clearAllOrDelete() {
        clearAll()
    }

    // Inserting a field into the database in a background thread.
    // For that we are calling the Repository class Insert function.
    private fun insert(subscriber: Subscriber) = viewModelScope.launch {
        repository.insert(subscriber)
    }

    // Updating a field into the database in a background thread.
    // For that we are calling the Repository class Update() function.
    fun update(subscriber: Subscriber) = viewModelScope.launch {
        repository.update(subscriber)
    }

    // Deleting a field into the database in a background thread.
    // For that we are calling the Repository class Delete() function.
    fun delete(subscriber: Subscriber) = viewModelScope.launch {
        repository.delete(subscriber)
    }

    // Deleting all the fields from the database in a background thread.
    // For that we are calling the Repository class DeleteALl() function.
    private fun clearAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    // Over-ridden function of the Observable class
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
    // Over-ridden function of the Observable class
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}