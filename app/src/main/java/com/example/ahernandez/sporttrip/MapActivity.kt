package com.example.ahernandez.sporttrip

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ahernandez.sporttrip.adapters.CustomInfoWindowAdapter
import com.example.ahernandez.sporttrip.model.Game
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.util.stream.Collectors


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    /***************
     *  VARIABLES  *
     ***************/
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mapViewGoogle: MapView
    private lateinit var allGamesArray: ArrayList<Game>
    private lateinit var backupAllGames: ArrayList<Game>
    private lateinit var filename: String
    private var mMap: GoogleMap? = null

    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog

    private lateinit var teamFromSpinner: String
    private lateinit var dateFromSpinner: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Hide status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        // Retrieve data passed in w Intent
        var leagueName: String? = getIntent().getStringExtra("league")
        var jsonList: String? = getIntent().getStringExtra("gameList")

        // Set .json file to access
        if (leagueName == "MLB") {
            filename = "teamsMLB.json"
        }
        else {
            filename = "teamsNHL.json"
        }


        // Use GSON library to convert json to ArrayList
        val gson = Gson()
        val arrayTutorialType = object : TypeToken<ArrayList<Game>>() {}.type
        allGamesArray = gson.fromJson(jsonList, arrayTutorialType)
        backupAllGames = allGamesArray

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

        // Initialize GoogleMap object to allow for redraw later
        mMap = map;

        // Load the Teams data and store as a JSON Object
        val utils = Utils()
        val teamsJson = utils.loadJsonFromAssets(this, filename)
        val jsonObj = JSONObject(teamsJson)

        if (map != null) {

            map.clear()
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
                            arenaInfo[i] = arenaInfo[i].substringBefore(",").substringBefore("}").replace("\"", "")
                        }
                    }

                    // Can not pass Object as Title or Snippet as it must be a String.
                    // Converting Game object to JSON string. Will be passed in as Snippet.
                    // Will be converted back to Game obj in Adapter.
                    val gson = Gson()
                    val gameInfoString: String = gson.toJson(game)

                    // Convert image for use as custom pin marker
                    var icon: BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.hockey_pin)

                    if (filename == "teamsMLB.json") {
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.baseball_pin)
                    }



                    // Add marker to map
                    builder.include(
                            map.addMarker(
                                    arenaInfo?.get(1)?.toDouble()?.let { LatLng(it, arenaInfo?.get(2).toDouble()) }?.let {
                                        MarkerOptions().position(it).title(
                                                arenaInfo?.get(0)
                                        ).snippet(gameInfoString).icon(icon)
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


    fun onFabClick(view: View) {

        createDialog()

    } // END onFabClick()

    fun createDialog() {

        // Create popup window
        dialogBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.filter_layout, null)

        dialogBuilder.setView(view)
        dialog = dialogBuilder.create()
        dialog.show()

        // Get a unique list of teams and dates for Spinners
        var teamList: ArrayList<String> = ArrayList()
        var dateList: ArrayList<String> = ArrayList()

        for (game in allGamesArray) {
            teamList.add(game.home.toString())
            dateList.add(game.date.toString())
        }

        // Add a blank entry to prevent app from selecting first team/date by default
        var uniqueList: MutableList<String>? = teamList.stream().distinct().collect(Collectors.toList())
        teamList.clear()
        teamList.add("")

        // Get Team List
        if (uniqueList != null) {
            for (item in uniqueList) {
                teamList.add(item)
            }
        }

        // Add a blank entry to prevent app from selecting first team/date by default
        uniqueList = dateList.stream().distinct().collect(Collectors.toList())
        dateList.clear()
        dateList.add("")

        // Get Date List
        if (uniqueList != null) {
            for (item in uniqueList) {
                dateList.add(item)
            }
        }



        val teamSpinner: Spinner? = dialog.findViewById(R.id.teamsSpinner)
        val dateSpinner: Spinner? = dialog.findViewById(R.id.dateSpinner)

        // Set 'Teams' spinner adapter
        val teamAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, teamList)
        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)

        if (teamSpinner != null) {
            teamSpinner.adapter = teamAdapter
        }

        if (teamSpinner != null) {
            teamSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")

                } // onNothingSelected()

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {

                    if (parent != null) {
                        teamFromSpinner = parent.getItemAtPosition(pos).toString()
                    }

                } // END onItemSelected()

            } // END onItemSelectedListener

        }



        // Set 'Date' spinner adapter
        val dateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dateList)
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)

        if (dateSpinner != null) {
            dateSpinner.adapter = dateAdapter
        }

        if (dateSpinner != null) {
            dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")

                } // END onNothingSelected()

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                    if (parent != null) {
                        dateFromSpinner = parent.getItemAtPosition(pos).toString()
                    }

                } // END onItemSelected()

            } // END onItemSelectedListener

        }


    } // END createDialog()



    fun filterBtnOnClick(view: View) {

        // Filter results depending on Spinner selection
        // Reset selection after filter applied
        if (teamFromSpinner != "" && dateFromSpinner != "") {
            allGamesArray = allGamesArray.filter {
                it.date == dateFromSpinner && it.home == teamFromSpinner
            } as ArrayList<Game>

            teamFromSpinner = ""
            dateFromSpinner = ""

        }
        else if (teamFromSpinner != "" && dateFromSpinner == "") {
            allGamesArray = allGamesArray.filter {
                it.home == teamFromSpinner || it.visitor == teamFromSpinner } as ArrayList<Game>
            teamFromSpinner = ""

        }
        else if (teamFromSpinner == "" && dateFromSpinner != "") {
            allGamesArray = allGamesArray.filter { s -> s.date == dateFromSpinner } as ArrayList<Game>
            dateFromSpinner = ""

        }

        if (allGamesArray.size == 0) {

            Toast.makeText(this, "No Matches Found! Resetting Filter.", Toast.LENGTH_SHORT).show()
            allGamesArray = backupAllGames
            dialog.dismiss()
            createDialog()
        }
        else {
            // Redraw map
            onMapReady(mMap)
            dialog.dismiss()
        }

    } // END filterBtnOnClick()



    fun resetFilterBtnOnClick(view: View) {
        allGamesArray = backupAllGames

        dialog.dismiss()
        createDialog()

    } // END resetFilterBtnOnClick()



} // END MapActivity class