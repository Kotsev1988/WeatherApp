package com.example.weatherapp.ui.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.model.Hour
import com.example.weatherapp.ui.WeatherFragment

class WeatherHorizontalDay(val context: WeatherFragment) : RecyclerView.Adapter<WeatherHorizontalDay.ViewHolder>() {

    private var weatherOfDay: List<Hour> = listOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.weather_by_time, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(weatherOfDay[position])
    }

    override fun getItemCount(): Int = weatherOfDay.size

    fun setData(data: List<Hour>) {
        weatherOfDay = data
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hour: TextView = itemView.findViewById(R.id.hour)
        private val tempC: TextView = itemView.findViewById(R.id.tempInC)
        private val conditionItem: TextView = itemView.findViewById(R.id.conditionItem)
        private val tempIcon: ImageView = itemView.findViewById(R.id.tempIcon)
        fun bind(weather: Hour) {
            hour.text = weather.time
            tempC.text = weather.temp_c.toString()

            conditionItem.text = weather.condition.text
            Glide.with(context).asBitmap()
                .load("http://" + weather.condition.icon)
                .override(600,200)
                .fitCenter()
                .into(tempIcon)
        }
    }
}