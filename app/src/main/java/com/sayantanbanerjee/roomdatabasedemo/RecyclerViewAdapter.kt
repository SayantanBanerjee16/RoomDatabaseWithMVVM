package com.sayantanbanerjee.roomdatabasedemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sayantanbanerjee.roomdatabasedemo.databinding.ListItemBinding
import com.sayantanbanerjee.roomdatabasedemo.db.Subscriber

// Recycler View Adapter to display the subscriber list fetched from the database.
class RecyclerViewAdapter(
    private val clickListener: (Subscriber) -> Unit
) : RecyclerView.Adapter<RecyclerViewHolder>() {

    private val subscribersList = ArrayList<Subscriber>()

    // Create the View Holder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return RecyclerViewHolder(binding)
    }

    // Bind each data with the view holder, i.e., create a new card.
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(subscribersList[position], clickListener)
    }

    // Item count of the Recycler View.
    override fun getItemCount(): Int {
        return subscribersList.size
    }

    // We are fetching the updated list and setting it to the adapter.
    fun setList(subscribers: List<Subscriber>) {
        subscribersList.clear()
        subscribersList.addAll(subscribers)
    }

}

class RecyclerViewHolder(private val binding: ListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    // This is called to bind the information to that particular card.
    fun bind(subscriber: Subscriber, clickListener: (Subscriber) -> Unit) {
        binding.nameTextView.text = subscriber.name
        binding.emailTextView.text = subscriber.email
        // click listener higher order function is triggered from here.
        binding.listItemLayout.setOnClickListener {
            clickListener(subscriber)
        }
    }
}
