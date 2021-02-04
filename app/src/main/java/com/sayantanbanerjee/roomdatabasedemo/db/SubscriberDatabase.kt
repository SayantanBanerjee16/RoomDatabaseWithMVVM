package com.sayantanbanerjee.roomdatabasedemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// It builds and returns a single instance of the Database to be used in rest of the project.
// Annotate with the Database tag and provide all the defined entities class.
@Database(entities = [Subscriber::class], version = 1)
abstract class SubscriberDatabase : RoomDatabase() {

    // define all the DAO here for their corresponding data Entities class.
    abstract val subscriberDao: SubscriberDao

    companion object {
        @Volatile
        private var INSTANCE: SubscriberDatabase? = null
        fun getInstance(context: Context): SubscriberDatabase? {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SubscriberDatabase::class.java,
                        "subscriber_data_database "
                    ).build()
                }
                return INSTANCE
            }
        }
    }
}
