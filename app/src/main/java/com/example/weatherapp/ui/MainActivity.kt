package com.example.weatherapp.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.weatherapp.R
import com.example.weatherapp.ui.addcity.AddCityFragment
import com.example.weatherapp.ui.get_contacts.GetContactsFragment
import com.example.weatherapp.ui.history.HistoryFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        println("OnCreate Activity")
//        val city = intent.getStringExtra("city")
//        if (intent != null) {
//            if (city != null) {
//                val bundle = Bundle()
//                bundle.putParcelable(CityWeather.BUNDLE_EXTRA, Cities(city, false))
//                supportFragmentManager.apply {
//                    beginTransaction()
//                        .add(R.id.container, CityWeather.newInstance(bundle))
//                        .addToBackStack("")
//                        .commitAllowingStateLoss()
//                }
//            }
//            println("CITY PUSH $city")
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_history -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .add(R.id.container, HistoryFragment.newInstance())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            R.id.add_city ->{
                supportFragmentManager.apply {
                    beginTransaction()
                        .add(R.id.container, AddCityFragment.newInstance())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }

            R.id.get_contacts ->{
                supportFragmentManager.apply {
                    beginTransaction()
                        .add(R.id.container, GetContactsFragment.newInstance())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}