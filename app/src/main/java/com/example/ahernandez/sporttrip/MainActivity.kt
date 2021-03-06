package com.example.ahernandez.sporttrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ahernandez.sporttrip.model.Game
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var gameList: ArrayList<Game>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load in .csv file
        val utils = Utils()
        gameList = utils.parseCsvFromAssets(this, "Games.csv")!!


        if (gameList != null) {
            println("The total # of loaded games is " + gameList.size.toString())
        }


        // Set Main as selected navigation item
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setSelectedItemId(R.id.navigationHome)

        // Check which item in custom navigator is selected
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.navigationHome -> {
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigationSearch -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()

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





} // END MainActivity class