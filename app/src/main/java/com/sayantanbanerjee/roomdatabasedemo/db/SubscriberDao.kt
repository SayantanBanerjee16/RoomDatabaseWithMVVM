package com.sayantanbanerjee.roomdatabasedemo.db

import androidx.lifecycle.LiveData
import androidx.room.*

// Data Access Object Interface.
// This file is used to directly interact with the database and we define the CRUD operations here.

@Dao
interface SubscriberDao {

    // to insert the data into the database
    // "Suspend" modifier is used to have database access on background thread.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscriber(subscriber: Subscriber) : Long

    // to update the data in the database
    @Update
    suspend fun updateSubscriber(subscriber: Subscriber)

    // to delete the data in the database
    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber)

    // to delete all the data into the database
    @Query("DELETE from subscriber_data_table")
    suspend fun deleteAll()

    // To fetch the data from the database.
    // These queries are asynchronous queries.
    // Whatever returns as a LiveData, room by default run those on background thread.
    // So no need of additional "suspend" modifier.
    @Query("SELECT * from subscriber_data_table")
    fun getAllSubscribers() : LiveData<List<Subscriber>>

}
