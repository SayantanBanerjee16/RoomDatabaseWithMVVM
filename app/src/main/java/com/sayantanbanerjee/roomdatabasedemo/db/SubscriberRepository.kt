package com.sayantanbanerjee.roomdatabasedemo.db

// Repository class acts as the gateway between the Model Class and the View Model.
// This also takes an instance of Subscriber Dao as constructor for this class.

class SubscriberRepository(private val dao : SubscriberDao) {

    val subscribers = dao.getAllSubscribers()

    suspend fun insert(subscriber: Subscriber){
        dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber){
        dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber){
        dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll(){
        dao.deleteAll()
    }

}
