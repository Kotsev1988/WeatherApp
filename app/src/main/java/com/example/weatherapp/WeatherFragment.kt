package com.example.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import com.example.weatherapp.data.Example
import com.example.weatherapp.data.ExampleDataClass
import com.example.weatherapp.data.ServiceApi
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.model.Weather
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherFragment : Fragment(R.layout.fragment_weather) {

    private var city = Example.getCity("Nalchik")
    private val  exampleDataClass: ExampleDataClass = ExampleDataClass("Mike", 54)

    private  var binding: FragmentWeatherBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* //использование Callback
        service.getWeather("55.75396", "37.620393").enqueue(object : Callback<Weather>{
            override fun onResponse(call: retrofit2.Call<Weather>, response: Response<Weather>) {
                println("Weather "+response.body()?.info)
            }

            override fun onFailure(call: retrofit2.Call<Weather>, t: Throwable) {

            }
        }
         */
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherBinding.bind(view)

        val data = Example.getCities()
        WeatherRecycler.adapter = WeatherRecyclerAdapter(data)

        var cityCopy = city?.copy(22.0, 25.0, "en_US")
        println("Object Copy "+cityCopy?.lat + " "+cityCopy?.lon)
        Example.fun2()

        println("DataClass val"+exampleDataClass.name +" "+exampleDataClass.age)

    }

}