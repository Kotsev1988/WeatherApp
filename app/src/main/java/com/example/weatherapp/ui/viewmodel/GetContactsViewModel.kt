package com.example.weatherapp.ui.viewmodel

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.App

class GetContactsViewModel(): ViewModel() {

    private val liveDataContacts: MutableLiveData<List<String>> = MutableLiveData()
    fun getLiveDataContacts(): LiveData<List<String>> = liveDataContacts

    private val contacts : ArrayList<String> = arrayListOf()

    private var getNumber: String = ""




     fun getContacts(){

        App.getAppContext().let {
            val contentResolver: ContentResolver = it.contentResolver
            val runnable = Thread {

                val cursorContacts: Cursor? =contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
                )

                cursorContacts?.let { cursor ->
                    val pos = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    for (i in 0.. cursorContacts.count){
                        if (cursor.moveToPosition(i)){
                            val name = cursor.getString(pos)
                            contacts.add(name)
                        }
                    }
                    cursor.close()
                }
            }
            runnable.start()
            try {
                runnable.join()
            }catch (e: InterruptedException){
                println(e.message)
            }
            liveDataContacts.value = contacts
        }
    }

     fun getPhoneNumber(contact: String) : String {

        val projection =
            arrayOf(ContactsContract.Data.CONTACT_ID,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data.DATA1,
                ContactsContract.Data.MIMETYPE)
        val allContacts: Uri = ContactsContract.Data.CONTENT_URI

        val whereString = "display_name LIKE ?"

        val whereParams = arrayOf("%$contact%")

        App.getAppContext().let {
            val contentResolver: ContentResolver = it.contentResolver

            val c: Cursor? =contentResolver.query(
                allContacts,
                projection,
                whereString,
                whereParams,
                null
            )

            c?.let {

                val pos = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

                val pos1 = c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

                c.moveToFirst()
                while (c.moveToNext()) {
                    val contactID = c.getString(pos)
                    val hasPhoneNumber = c.getInt(pos1)

                    println("CONTACTID "+contactID + "contact "+contact)
                    if (contactID.equals(contact)){
                    if (hasPhoneNumber > 0) {

                        val whereString = "display_name LIKE ?"
                        val whereParams = arrayOf("%$contactID%")

                        val phoneNumberCursor: Cursor? =
                            contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                whereString,
                                whereParams,
                                null)


                        if (phoneNumberCursor != null) {

                            val number =
                                phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val displayName =
                                phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)


                                while (phoneNumberCursor.moveToNext()) {
                                    val displayName1 = phoneNumberCursor.getString(displayName)

                                    if (displayName1.equals(contact)){
                                        getNumber = phoneNumberCursor.getString(number)
                                    }

                                }

                            phoneNumberCursor.close()
                        }
                    }
                }
                }
                c.close()
            }
        }
        return getNumber
    }
}