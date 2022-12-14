package com.example.weatherapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.ServiceApi

class WeatherRecyclerAdapter(private val listCity: ArrayList<String>) :
    RecyclerView.Adapter<WeatherRecyclerAdapter.ViewHolder>() {
    private val service = ServiceApi.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listCity[position])
    }

    override fun getItemCount(): Int = listCity.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val partWeather: TextView = itemView.findViewById(R.id.part)
        private val feelsLike: TextView = itemView.findViewById(R.id.feelsLike)
        private val condition: TextView = itemView.findViewById(R.id.condition)
        fun bind(part: String) {

        }
    }
}


