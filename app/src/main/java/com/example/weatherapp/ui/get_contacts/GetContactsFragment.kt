package com.example.weatherapp.ui.get_contacts

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import com.example.weatherapp.databinding.FragmentGetContactsBinding
import com.example.weatherapp.ui.viewmodel.GetContactsViewModel
import com.example.weatherapp.ui.viewmodel.MainViewModel

const val REQUEST_CODE = 20

class GetContactsFragment : Fragment() {
    private var _binding: FragmentGetContactsBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: GetContactsViewModel by lazy {
        ViewModelProvider(this)[GetContactsViewModel::class.java]
    }

    interface OnItemClickListener {
        fun onItemClick(contact: String)
    }

    private val adapter: ContactsAdapter = ContactsAdapter(object : OnItemClickListener {
        override fun onItemClick(contact: String) {
            val number = viewModel.getPhoneNumber(contact)
            makeCallToContact(contact, number)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGetContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()

        viewModel.getLiveDataContacts().observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        binding.lvSimple.adapter = adapter

    }

    private fun checkPermission() {

        context?.let {

            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    viewModel.getContacts()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("Открыть")
                        .setPositiveButton("Предоставить доступ") { _, _ ->
                            requestPermission()
                        }
                        .setNegativeButton("Отказаться") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                else -> requestPermission()
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {

        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    viewModel.getContacts()
                } else {
                    context?.let {

                        AlertDialog.Builder(it)
                            .setTitle("Доступ к контактам")
                            .setMessage("Объяснение")
                            .setNegativeButton("Закрыть") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
                return
            }
        }
    }

    fun makeCallToContact(name: String, number: String) {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle("Звонок")
                .setMessage("Абонент $name \n Номер $number")
                .setNegativeButton("Отмена") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GetContactsFragment()
    }
}