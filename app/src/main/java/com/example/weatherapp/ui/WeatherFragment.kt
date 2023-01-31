package com.example.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weatherapp.MyReceiver
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.domain.model.*
import com.example.weatherapp.ui.viewmodel.appSatets.AppState
import com.example.weatherapp.ui.viewmodel.MainViewModel
import com.example.weatherapp.ui.adapters.WeatherHorizontalDay
import com.example.weatherapp.ui.adapters.WeatherRecyclerAdapter
import com.example.weatherapp.ui.googleMap.MapsFragment
import com.example.weatherapp.ui.viewmodel.appSatets.AppStateLocation
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_weather.*

private const val IS_WORLD_KEY = "LIST_OF_TOWNS_KEY"
const val REQUEST_CODE = 30
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

private const val PROCESS_ERROR = "Обработка ошибки"
const val INTENT_FILTER = "INTENT_FILTER"

const val PUT_CITY_EXTRA = "CITY_NAME"


class WeatherFragment : Fragment(R.layout.fragment_weather) {

    private var _binding: FragmentWeatherBinding? = null

    private val adapter = WeatherRecyclerAdapter(object : OnItemClickListener {
        override fun onItemClick(cities: Cities) {

            activity?.supportFragmentManager?.apply {
                val bundle = Bundle()
                bundle.putParcelable(CityWeather.BUNDLE_EXTRA, cities)
                beginTransaction()
                    .add(R.id.container, CityWeather.newInstance(bundle))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })
    private val adapterOfDay = WeatherHorizontalDay(this)
    private val binding
        get() = _binding!!

    private var isRussianCities: Boolean = true

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val loadResultsReceiver: BroadcastReceiver = object :
        BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val message = intent.getStringExtra(PUT_CITY_EXTRA)
            val cities = message?.let { Cities(it, false) }
            activity?.supportFragmentManager?.apply {
                val bundle = Bundle()
                bundle.putParcelable(CityWeather.BUNDLE_EXTRA, cities)
                beginTransaction()
                    .add(R.id.container, CityWeather.newInstance(bundle))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }

        }
    }


    interface OnItemClickListener {
        fun onItemClick(cities: Cities)
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//            println("OnCreate Fragment")
            context?.let {
                LocalBroadcastManager.getInstance(it)
                    .registerReceiver(loadResultsReceiver,
                        IntentFilter(INTENT_FILTER))
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWeatherBinding.bind(view)

        showListOfTowns()


        binding.mainFragmentFAB.setOnClickListener {
            changeWeatherData()
        }

        binding.locationIcon.setOnClickListener{

            openDetailsFragmentMap()
        }

        checkPermission()

        WeatherRecycler.adapter = adapter
        weatherHorizontal.adapter = adapterOfDay

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            getAppState(it)
        })

        viewModel.getLiveDataCities().observe(viewLifecycleOwner, Observer {

            adapter.setData(it)
        })

        viewModel.getLiveDataLocation().observe(viewLifecycleOwner, Observer {
            getAppStateLocation(it)
        })

        viewModel.getLocation()
        viewModel.updateCitites()
        viewModel.getWeather(isRussianCities)

    }

    private fun showListOfTowns() {
        activity?.let {
            if (it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_WORLD_KEY,
                    false)
            ) {
                changeWeatherData()
            } else {
                viewModel.getDataFromService(isRussianCities)
            }
        }
    }

    private fun saveListOfTowns() {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(IS_WORLD_KEY, !isRussianCities)
                apply()
            }
        }
    }

    private fun checkPermission(){
        activity?.let {
            when{
                ContextCompat.checkSelfPermission(it,
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ->{
                   viewModel.getLocation()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ->{
                    showRationaleDialog()
                }

                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE ->{
                var grantedPermission = 0;
                if (grantResults.isNotEmpty()) {
                    for (i in grantResults){
                        if (i == PackageManager.PERMISSION_GRANTED) {
                            grantedPermission++
                        }
                    }
                    if (grantResults.size == grantedPermission) {
                        viewModel.getLocation()
                    } else {
                        showDialog(getString(R.string.dialog_title_no_gps), getString(R.string.dialog_message_no_gps))
                    }
                }else{
                    showDialog(getString(R.string.dialog_title_no_gps), getString(R.string.dialog_message_no_gps))
                }
            }
        }
    }

    private fun showDialog(tile: String, message: String) {

        context?.let {
            AlertDialog.Builder(it)
                .setTitle(tile)
                .setMessage(message)
                .setNegativeButton(R.string.dialog_button_close) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun openDetailsFragmnet(cities: Cities) {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .add(R.id.container, CityWeather.newInstance(Bundle().apply{
                   putParcelable (CityWeather.BUNDLE_EXTRA, cities)
                }))
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    private fun openDetailsFragmentMap() {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .add(R.id.container, MapsFragment.newInstance())
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    private fun changeWeatherData() {

        isRussianCities = !isRussianCities
        viewModel.getWeather(isRussianCities)

        saveListOfTowns()

    }


    private fun getAppState(it: AppState) {
        when (it) {

            is AppState.Success -> {

                binding.frameLoading.visibility = View.GONE

                val weather = it.weather
                val cities = it.cities

                binding.city.text = cities.get(0).cityName
                weather?.forecast?.forecastday?.get(0)?.hour?.let { it1 -> adapterOfDay.setData(it1) }
            }

            is AppState.Loading -> {
                binding.frameLoading.visibility = View.VISIBLE
            }

            is AppState.EmptyData -> {
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
            }

            is AppState.Error -> {
                binding.frameLoading.visibility = View.GONE
                val error = it.error

                error.message?.let { it1 ->
                    binding.mainView.showSnackBar(
                        it1,
                        getString(R.string.reload),

                        {
                            viewModel.getDataFromService(isRussianCities)
                        }
                    )
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getAppStateLocation(it: AppStateLocation) {
        when (it) {

            is AppStateLocation.Success -> {
                binding.location.text = it.cities.toString()+ " " +
                it.weather?.current?.temp_c.toString() +"°C"
                }

            is AppStateLocation.EmptyData -> {
                context?.let { context ->
                    showDialog(context.getString(R.string.dialog_title_gps_turned_off),
                        context.getString(R.string.dialog_message_last_location_unknown))
                }
            }

            is AppStateLocation.ShowRationalDialog-> {
                showRationaleDialog()
            }

            is AppStateLocation.Error -> {
                binding.frameLoading.visibility = View.GONE
                val error = it.error

                error.message?.let { it1 ->
                    binding.mainView.showSnackBar(
                        it1,
                        getString(R.string.reload),

                        {
                            viewModel.getDataFromService(isRussianCities)
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        adapter.removeListener()
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }

        super.onDestroy()
    }

    private fun showRationaleDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.dialog_rationale_title)
                .setMessage(R.string.dialog_rationale_meaasge)
                .setPositiveButton(it.getString(R.string.dialog_rationale_give_access))
                {
                        _, _ ->
                     requestPermission()
                }
                .setNegativeButton(R.string.dialog_rationale_decline) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun View.showSnackBar(
        text: String,
        actionText: String,
        action: (View) -> Unit,
        length: Int = Snackbar.LENGTH_INDEFINITE,
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }

}


