package com.sayantanbanerjee.roomdatabasedemo

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
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

    // Boolean variable to hold the state. When True -> Saving and Clearing All. When False -> Update or Delete a particular item.
    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber

    // For logging, we will declare a mutable live data of Event class.
    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    // Initializing the variables.
    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    // Inserting/Updating into the database.
    fun saveOrUpdate() {
        if (inputName.value == null || inputEmail.value == null) {
            statusMessage.value = Event("Please insert the fields value.")
        } else {
            val name = inputName.value!!
            val email = inputEmail.value!!
            if (isUpdateOrDelete) {
                subscriberToUpdateOrDelete.name = name
                subscriberToUpdateOrDelete.email = email
                update(subscriberToUpdateOrDelete)
                resetAfterDeleteOrUpdate()
            } else {
                insert(Subscriber(0, name, email))
                inputName.value = null
                inputEmail.value = null
            }

        }

    }

    // Deleting a particular or all the items from the database.
    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(subscriberToUpdateOrDelete)
            resetAfterDeleteOrUpdate()
        } else {
            clearAll()
        }
    }

    // Initializing the field values and button values once a particular item is selected.
    // This function is triggered from the onClickListener()
    fun initUpdateOrDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    // Resetting the field values and button values once a particular entry is updated or deleted.
    private fun resetAfterDeleteOrUpdate() {
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    // Inserting a field into the database in a background thread.
    // For that we are calling the Repository class Insert function.
    private fun insert(subscriber: Subscriber) = viewModelScope.launch {
        repository.insert(subscriber)
        statusMessage.value = Event("Input successfully done.")
    }

    // Updating a field into the database in a background thread.
    // For that we are calling the Repository class Update() function.
    private fun update(subscriber: Subscriber) = viewModelScope.launch {
        repository.update(subscriber)
        statusMessage.value = Event("Update successfully done.")
    }

    // Deleting a field into the database in a background thread.
    // For that we are calling the Repository class Delete() function.
    private fun delete(subscriber: Subscriber) = viewModelScope.launch {
        repository.delete(subscriber)
        statusMessage.value = Event("Delete successfully done.")
    }

    // Deleting all the fields from the database in a background thread.
    // For that we are calling the Repository class DeleteALl() function.
    private fun clearAll() = viewModelScope.launch {
        repository.deleteAll()
        statusMessage.value = Event("Clear All successfully done.")
    }

    // Over-ridden function of the Observable class
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    // Over-ridden function of the Observable class
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}