package com.example.ahernandez.sporttrip

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ahernandez.sporttrip.adapters.LeagueItemAdapter
import com.example.ahernandez.sporttrip.model.Game
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson


class SearchActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


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


        // Set the LayoutManager used by the RecyclerView
        var recycler_view_items: RecyclerView = findViewById(R.id.recycler_view_items)
        recycler_view_items.layoutManager = LinearLayoutManager(this)

        // Initialize Adapter class and pass in content list
        val itemAdapter = LeagueItemAdapter(this, getItemsList())

        // Inflate items
        recycler_view_items.adapter = itemAdapter
        

    } // END onCreate()


    private fun getItemsList(): ArrayList<String> {

        val list = ArrayList<String>()

        // TODO: Thinking of setting league name and corresponding icon in json doc
        // Hardcoded leagues for now
        list.add("NHL")
        list.add("MLB")
        list.add("NBA")
        list.add("NFL")
        list.add("MLS")
        list.add("CFL")
        list.add("PGA")

        return list

    } // END getItemsList()



}