package com.example.weatherapp.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.domain.model.Weather
import kotlinx.android.synthetic.main.fragment_history_item.view.*

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.ItemViewHolder>() {
    private var data: List<Weather> = arrayListOf()

    inner class ItemViewHolder(view : View): RecyclerView.ViewHolder(view){
        fun bind(weather: Weather) {

            println("CITIES "+weather.location.name)


            if (layoutPosition != RecyclerView.NO_POSITION){


                itemView.cityItem.text =
                    String.format("%s %.1f %d", weather.location.name, weather.current.temp_c, weather.current.humidity)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_history_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun setData(weather: List<Weather>) {

        data = weather
        println("DATA "+data.size)
        notifyDataSetChanged()
    }
}