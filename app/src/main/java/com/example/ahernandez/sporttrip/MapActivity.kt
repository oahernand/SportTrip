package com.example.ahernandez.sporttrip

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ahernandez.sporttrip.adapters.CustomInfoWindowAdapter
import com.example.ahernandez.sporttrip.model.Game
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mapViewGoogle: MapView
    private lateinit var allGamesArray: ArrayList<Game>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Retrieve data passed in w Intent
        var leagueName: String? = getIntent().getStringExtra("leagueName")
        var jsonList: String? = getIntent().getStringExtra("gameList")

        // Use GSON library to convert json to ArrayList
        val gson = Gson()
        val arrayTutorialType = object : TypeToken<ArrayList<Game>>() {}.type
        allGamesArray = gson.fromJson(jsonList, arrayTutorialType)


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



    override fun onMapReady(map: GoogleMap?) {

        // Load the Teams data and store as a JSON Object
        val utils = Utils()
        val teamsJson = utils.loadJsonFromAssets(this, "teamsNHL.json")
        val jsonObj = JSONObject(teamsJson)

        if (map != null) {

            val builder = LatLngBounds.Builder()

            // Iterate through selected games
            for (game in allGamesArray) {

                // IF home team/arena match is found
                if(jsonObj.has(game.home)) {

                    // Retrieve specific arena entry and value string(properties)
                    var arenaString: String? = jsonObj[game.home].toString()
                    var arenaInfo: ArrayList<String>? = arenaString?.split(":") as ArrayList<String>?

                    // Remove 1st index as not required
                    arenaInfo?.removeAt(0)

                    // Clean data - ArenaName[0]/Lat[1]/Long[2]
                    if (arenaInfo != null) {
                        for (i in arenaInfo.indices) {
                            arenaInfo[i] = arenaInfo[i].substringBefore(",").substringBefore("}").replace("\"","")
                        }
                    }

                    // Can not pass Object as Title or Snippet as it must be a String.
                    // Converting Game object to JSON string. Will be passed in as Snippet.
                    // Will be converted back to Game obj in Adapter.
                    val gson = Gson()
                    val gameInfoString: String = gson.toJson(game)

                    // Add marker to map
                    builder.include(
                        map.addMarker(
                            arenaInfo?.get(1)?.toDouble()?.let { LatLng(it, arenaInfo?.get(2).toDouble()) }?.let {
                                MarkerOptions().position(it).title(
                                    arenaInfo?.get(0)
                                ).snippet(gameInfoString)
                            }
                        ).getPosition()
                    )

                }

            } // END FOR Loop


            val bounds = builder.build()

            // Set padding so that all markers are visible when zoom is set
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            val padding = (height * 0.10).toInt()

            // Create custom marker window info
            map.setInfoWindowAdapter(CustomInfoWindowAdapter(this))

            // Move camera and zoom to calculated box
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
            map.animateCamera(cu)

        } // END IF


    } // END onMapReady()



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY) ?: Bundle().also {
            outState.putBundle(MAPVIEW_BUNDLE_KEY, it)
        }
        mapViewGoogle.onSaveInstanceState(mapViewBundle)

    } // END onSaveInstanceState()


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