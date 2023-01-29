package com.example.weatherapp.ui.googleMap

import android.graphics.Color
import android.location.Address
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentMapBinding
import com.example.weatherapp.ui.viewmodel.appSatets.AppStateMap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_map.*

class MapsFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding
        get() = _binding!!


    private val viewModel: ViewModelMaps by lazy {
        ViewModelProvider(this)[ViewModelMaps::class.java]
    }
    private val markers: ArrayList<Marker> = arrayListOf()
    private lateinit var map: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val initPlace = LatLng(52.52000659999999, 13.404953999999975)
        MarkerOptions().position(initPlace).title(getString(R.string.marker_start))

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initPlace))
        googleMap.setOnMapClickListener {

            viewModel.getAddressAsync(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMapBinding.bind(view)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        viewModel.getLiveDataLocation().observe(viewLifecycleOwner, Observer {
            getAppState(it)
        })

        binding.buttonSearch.setOnClickListener {
            viewModel.initSearchByAddress(searchAddress.text.toString())
        }
    }

    private fun addMarkerToArray(location: LatLng, titleText: String) {

        val marker = setMarker(location, titleText, R.drawable.google_map_icon)
        if (marker != null) {
            markers.add(marker)
        }
    }

    private fun setMarker(
        location: LatLng,
        titleText: String,
        icLocationIconForeground: Int,
    ): Marker? {

        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(titleText)
                .icon(BitmapDescriptorFactory.fromResource(icLocationIconForeground))
        )
    }

    private fun drawLine() {

        val last: Int = markers.size - 1
        if (last >= 1) {
            val previous: LatLng = markers[last - 1].position
            val current: LatLng = markers[last].position

            map.addPolyline(
                PolylineOptions()
                    .add(previous, current)
                    .color(Color.RED)
                    .width(5f)
            )
        }
    }

    private fun getAppState(appState: AppStateMap) {
        when (appState) {

            is AppStateMap.Success -> {
                val location = LatLng(
                    appState.addresses[0].latitude,
                    appState.addresses[0].longitude
                )
                binding.textAddress?.let {

                    goToAddress(appState.addresses, it, appState.searchText +
                            " Температура " + appState.weather?.current?.temp_c)
                    textAddress.text = appState.addresses[0].getAddressLine(0) +
                            "  " + appState.weather?.current?.temp_c
                    addMarkerToArray(location, appState.addresses[0].getAddressLine(0) +
                            "  " + appState.weather?.current?.temp_c)
                    drawLine()
                }
            }

            is AppStateMap.SuccessClickMap -> {
                val location = LatLng(
                    appState.addresses[0].latitude,
                    appState.addresses[0].longitude
                )
                addMarkerToArray(location, "В " + appState.weather?.location?.name +
                        " температура " + appState.weather?.current?.temp_c.toString())
                drawLine()
            }

            is AppStateMap.EmptyData -> {
                Toast.makeText(requireActivity(), appState.message, Toast.LENGTH_SHORT).show()
            }

            is AppStateMap.Error -> {

                val error = appState.error
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToAddress(addresses: List<Address>, view: View?, searchText: String) {
        val location = LatLng(
            addresses[0].latitude,
            addresses[0].longitude
        )

        view?.post {
            setMarker(location, searchText, R.drawable.google_map_icon)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }

    companion object {
        fun newInstance() = MapsFragment()
    }
}