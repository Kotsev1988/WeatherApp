package com.example.weatherapp.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.databinding.FragmentHistoryBinding
import com.example.weatherapp.ui.viewmodel.appSatets.AppStateHistory
import com.example.weatherapp.ui.viewmodel.HistoryViewModel


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    private val adapter: HistoryAdapter by lazy {
        HistoryAdapter()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HistoryFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.historyRecyclerview.adapter = adapter

        viewModel.historyLiveData.observe(viewLifecycleOwner, Observer {
            renderData(it)
        })

        viewModel.getAllHistory()

        binding.searchItems.setOnCloseListener {

            viewModel.getAllHistory()
            true
        }

        binding.searchItems.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getFilterData(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.getFilterData(newText)
                return true
            }
        }
        )

    }

    private fun renderData(appState: AppStateHistory) {

        when (appState) {
            is AppStateHistory.Success -> {
                with(binding) {
                    binding.historyRecyclerview.visibility = View.VISIBLE
                    binding.frameLoadingHistory.visibility = View.GONE
                }

                adapter.setData(appState.weather)
            }

            is AppStateHistory.Loading -> {

                with(binding) {
                    binding.historyRecyclerview.visibility = View.GONE
                    binding.frameLoadingHistory.visibility = View.VISIBLE
                }
            }

            is AppStateHistory.EmptyData -> {

            }

            is AppStateHistory.Error -> {

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}