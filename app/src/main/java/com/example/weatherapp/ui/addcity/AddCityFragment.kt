package com.example.weatherapp.ui.addcity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.databinding.FragmentAddCityBinding


class AddCityFragment : Fragment() {
    private var _binding: FragmentAddCityBinding? = null
    private val binding
        get() = _binding!!

    private val addCityViewModel by lazy {
        ViewModelProvider(this)[AddCityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAddCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.adding.setOnClickListener {
            addCityViewModel.addCity(binding.addCity.text.toString(), binding.isRussain.isChecked)
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddCityFragment()
    }
}