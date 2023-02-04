package com.example.weatherapp.ui.addcity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.App
import com.example.weatherapp.databinding.FragmentAddCityBinding
import com.example.weatherapp.domain.model.Cities
import com.example.weatherapp.domain.model.repository.addcity.AddRepository
import com.example.weatherapp.domain.model.repository.addcity.AddRepositoryImpl


class AddCityFragment : Fragment() {
    private val repository: AddRepository = AddRepositoryImpl(App.getCitiesDao())
    private var _binding: FragmentAddCityBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.adding.setOnClickListener {
            Thread{
                repository.addCity(Cities(binding.addCity.text.toString(), binding.isRussain.isChecked))
            }.start()


            activity?.supportFragmentManager?.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddCityFragment()
    }
}