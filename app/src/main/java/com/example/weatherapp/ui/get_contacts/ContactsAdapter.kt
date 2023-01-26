package com.example.weatherapp.ui.get_contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.ui.WeatherFragment
import kotlinx.android.synthetic.main.contact_item.view.*

class ContactsAdapter(private var onItemClickListener: GetContactsFragment.OnItemClickListener?) :
    RecyclerView.Adapter<ContactsAdapter.ItemViewHolder>() {
    private var data: List<String> = arrayListOf()

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: String) {

            itemView.text_contactItem.text = data

            itemView.text_contactItem.setOnClickListener {
                onItemClickListener?.onItemClick(data)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.contact_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun setData(contact: List<String>) {

        data = contact
        notifyDataSetChanged()
    }
}