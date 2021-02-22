package com.example.ahernandez.sporttrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mapViewGoogle: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        val mapViewBundle = savedInstanceState?.getBundle(MapActivity.MAPVIEW_BUNDLE_KEY)
        mapViewGoogle = findViewById(R.id.mapView)
        mapViewGoogle.onCreate(mapViewBundle)
        mapViewGoogle.getMapAsync(this)


        // Set Main as selected navigation item
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setSelectedItemId(R.id.navigationSearch)

        // Check which item in custom navigator is selected
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.navigationHome -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                }

                R.id.navigationSearch -> {
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigationWatch -> {
                    val intent = Intent(this, WatchActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()

                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigationSetting -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()

                    return@setOnNavigationItemSelectedListener true
                }
            }

            return@setOnNavigationItemSelectedListener false

        } // END setOnNavigationItemSelectedListener


    } // END onCreate()



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY) ?: Bundle().also {
            outState.putBundle(MAPVIEW_BUNDLE_KEY, it)
        }
        mapViewGoogle.onSaveInstanceState(mapViewBundle)
    }


    override fun onResume() {
        super.onResume()
        mapViewGoogle.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapViewGoogle.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapViewGoogle.onStop()
    }

    // TODO: Set it to work from info passed in SearchActivity
    override fun onMapReady(map: GoogleMap?) {

        if (map != null) {

            //map.addMarker(MarkerOptions().position(LatLng(36.15917, -86.77861)).title("Marker"))
            //map.addMarker(MarkerOptions().position(LatLng(45.29694, -75.92722)).title("Marker"))

            val builder = LatLngBounds.Builder()

            // Calculate the min and max bounds containing all markers
            builder.include(map.addMarker(MarkerOptions().position(LatLng(36.15917, -86.77861)).title("Marker")).getPosition())
            builder.include(map.addMarker(MarkerOptions().position(LatLng(45.29694, -75.92722)).title("Marker")).getPosition())
            val bounds = builder.build()

            // Set padding so that all markers are visible when zoom is set
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            val padding = (height * 0.10).toInt()

            // Move camera and zoom to calculated box
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
            map.animateCamera(cu)

        }

    } // END onMapReady()


    override fun onPause() {
        mapViewGoogle.onPause()
        super.onPause()
    }


    override fun onDestroy() {
        mapViewGoogle.onDestroy()
        super.onDestroy()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        mapViewGoogle.onLowMemory()
    }


    companion object {
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }



} // END MapActivity class