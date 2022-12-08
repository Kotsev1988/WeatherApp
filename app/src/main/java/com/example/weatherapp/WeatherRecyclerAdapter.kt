package com.example.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.Example
import com.example.weatherapp.data.ServiceApi
import com.example.weatherapp.model.Info
import com.example.weatherapp.model.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherRecyclerAdapter(private val listCity: ArrayList<Info>) :
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

        private val cityName: TextView = itemView.findViewById(R.id.city)
        private val temperature: TextView = itemView.findViewById(R.id.temperature)


        fun bind(info: Info) {

            var weather: Weather
            var city = Example.getCityName(info.lat, info.lon)

            CoroutineScope(Dispatchers.IO).launch {
                weather = service.getWeather(info.lat, info.lon).body()!!

                withContext(Dispatchers.Main) {

                    cityName.text = city
                    temperature.text = weather.fact.temp.toString() + " °C"
                    itemView.setOnClickListener{

                        val cityWeather: CityWeather = CityWeather.newInstance(city, temperature.text.toString())
                        var fragmentManager = (itemView.context as MainActivity).supportFragmentManager
                        fragmentManager
                            .beginTransaction()
                            .replace(R.id.cityTemp, cityWeather)
                            .addToBackStack("")
                            .commit()
                    }
                }


            }
        }

    }

}


