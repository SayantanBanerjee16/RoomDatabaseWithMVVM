package com.sayantanbanerjee.roomdatabasedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sayantanbanerjee.roomdatabasedemo.databinding.ActivityMainBinding
import com.sayantanbanerjee.roomdatabasedemo.db.Subscriber
import com.sayantanbanerjee.roomdatabasedemo.db.SubscriberDatabase
import com.sayantanbanerjee.roomdatabasedemo.db.SubscriberRepository

class MainActivity : AppCompatActivity() {

    // Declare the Data-Binding variable
    private lateinit var binding: ActivityMainBinding

    // Declare the Subscriber View Model variable
    private lateinit var subscriberViewModel: SubscriberViewModel

    // Declare the Adapter variable
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // First set the DataBinding variable.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Next declare the database and fetch the dao instance.
        val dao = SubscriberDatabase.getInstance(application).subscriberDao

        // Using the dao instance , initialize the repository.
        val repository = SubscriberRepository(dao)

        // We need to pass values in the View Model constructor. So first declare View Model Factory.
        val factory = SubscriberViewModelFactory(repository)

        // Once we have the factory object, we can declare the View Model.
        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)

        // Now set the View Model object with the data binding data object.
        binding.myViewModel = subscriberViewModel

        // Also set the view model lifecycle owner to this activity.
        binding.lifecycleOwner = this

        // Observe the Subscriber List and display on the recycler View.
        initRecyclerView()

        // Observe and display Logging toast message
        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRecyclerView() {
        // Initialize the recycler view.
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        // Initialize the adapter and bind it to the recycler view.
        adapter = RecyclerViewAdapter { selectedItem: Subscriber -> listItemClicked(selectedItem) }
        binding.subscriberRecyclerView.adapter = adapter

        // Display the Subscriber List
        displaySubscribersList()
    }

    private fun displaySubscribersList() {
        // Observe for any change of data and when new data appears, the updated list is send to the adapter.
        subscriberViewModel.subscriber.observe(this, Observer {
            // Every time we get updated data, we send the updated data to the adapter,
            // and notify it that the data set has been changed.
            // So that only the changed data is reflected, and all other other items remain unaffected.
            // Thus having a good performance.
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    // Function defined to execute when user click on a particular list item.
    // But how to make this function trigger?
    // For that it is passed as Higher Order Function reference to the list view.
    private fun listItemClicked(subscriber: Subscriber) {
        subscriberViewModel.initUpdateOrDelete(subscriber)
    }
}
