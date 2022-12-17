package com.example.weatherapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.Cities
import com.google.android.material.button.MaterialButton

class WeatherRecyclerAdapter(private var onItemClickListener: WeatherFragment.OnItemClickListener?) :
    RecyclerView.Adapter<WeatherRecyclerAdapter.ViewHolder>() {
    private var listCity: List<Cities> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listCity[position])
    }

    override fun getItemCount(): Int = listCity.size

    fun removeListener() {
        onItemClickListener = null
    }

    fun setData(data: List<Cities>) {
        listCity = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val temp: TextView = itemView.findViewById(R.id.temp)
        private val condition: TextView = itemView.findViewById(R.id.condition)
        private val details: MaterialButton = itemView.findViewById(R.id.details)

        fun bind(city: Cities) {

            name.text = city.cityName

            details.setOnClickListener {
                onItemClickListener?.onItemClick(cities = city)
            }

        }
    }
}


