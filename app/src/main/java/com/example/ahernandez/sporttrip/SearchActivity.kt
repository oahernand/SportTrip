package com.example.ahernandez.sporttrip

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ahernandez.sporttrip.model.Game
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SearchActivity : AppCompatActivity() {

    /***************
     *  VARIABLES  *
     ***************/
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var rangeSwitch: Switch
    private var rangedQuery: Boolean = false
    private lateinit var calendarView: CalendarView
    private lateinit var calendarDayPicked: String
    private var leagueName: String = "NHL"
    private var filename: String = "Games.csv"


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


        // Define available CalendarView dates
        calendarView = findViewById(R.id.calendarView)
        setMinMaxCalendarDates(calendarView)

        // CalendarView Listener for date selection
        calendarView.setOnDateChangeListener { cldrInstance, year, month, day ->

            var formattedMonth: String
            var formattedDay: String

            // Format Month
            if (month + 1 <= 9)
                formattedMonth = "0" + (month + 1).toString()
            else
                formattedMonth = (month + 1).toString()

            // Format Day
            if (day + 1 <= 9)
                formattedDay = "0" + (day).toString()
            else
                formattedDay = (day).toString()

            // Set selected date
            calendarDayPicked = "$year-${formattedMonth}-$formattedDay"

        } // END setOnDateChangeListener


        // Switch Listener for Ranged search
        rangeSwitch = findViewById(R.id.rangeSwitch)
        rangeSwitch.setOnCheckedChangeListener { compoundButton, onSwitch ->

            if (onSwitch) {
                rangeSwitch.text = "7 Days"
                rangedQuery =  true
            }

            else {
                rangeSwitch.text = "Single Day"
                rangedQuery = false
            }

        } // END setOnCheckedChangeListener


    } // END onCreate()



    private fun setMinMaxCalendarDates(calendarView: CalendarView) {

        val calendar = Calendar.getInstance()

        // Set MIN Date
        calendar.set(
                2021, // year
                0, // month june, 0 based index
                13 // day of month
        )
        calendarView.minDate = calendar.timeInMillis


        // Set MAX Date
        calendar.set(
                2021, // year
                4, // month june, 0 based index
                10 // day of month
        )
        calendarView.maxDate = calendar.timeInMillis

    } // END setMinMaxCalendarDates()



    fun onLeagueBtnClick(view: View) {

        var nhlBtn: Button = findViewById(R.id.nhlBtn)
        var mlbBtn: Button = findViewById(R.id.mlbBtn)

        // Change color of selected button for visibility reasons
        when(view.id)
        {
            R.id.nhlBtn -> {
                leagueName = (view as Button).text.toString()

                // Change button colors
                nhlBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.Blue))
                mlbBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.MediumBlack))

                filename = "Games.csv"

            }

            R.id.mlbBtn -> {
                leagueName = (view as Button).text.toString()

                // Change button colors
                nhlBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.MediumBlack))
                mlbBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.Blue))

                filename = "MLB_games.csv"

            }
            else -> throw Exception()
        }

    } // END onLeagueBtnClick()



    fun findBtnClick(view: View) {

        // Load in .csv file and retrieve games matching date only
        val utils = Utils()
        //var gameList: ArrayList<Game> = utils.parseCsvFromAssets(this, "Games.csv")!!
        var gameList: ArrayList<Game> = utils.parseCsvFromAssets(this, filename)!!

        var monthList: List<Game> = emptyList()
        var dateSet: ArrayList<String>

        // IF no date selected, set to current date
        if (!this::calendarDayPicked.isInitialized) {

            val c = Calendar.getInstance()
            val ss = SimpleDateFormat("yyyy-MM-dd")
            val date = Date()
            calendarDayPicked = ss.format(date)

        }

        // Check whether query is for single or multiple days
        if (!rangedQuery) {

            monthList = gameList.filter { s -> s.date == calendarDayPicked }
        }
        else {

            dateSet = getDateRange(calendarDayPicked)

            for (date in dateSet) {

                monthList = concatenate(monthList, gameList.filter { s -> s.date == date })
            }

            monthList.size
        }

        // Convert list to JSON to pass in Intent
        val gson = Gson()
        val dailyGamesJson: String = gson.toJson(monthList)

        // Launch MapActivity
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("league", leagueName)
        intent.putExtra("gameList", dailyGamesJson)
        this.startActivity(intent)

    } // END findBtnClick()



    fun <T> concatenate(vararg lists: List<T>): List<T> {
        val result: MutableList<T> = ArrayList()

        for (list in lists) {
            result += list
        }

        return result

    } // END concatenate()



    fun getDateRange(selectedDate: String) : ArrayList<String> {

        // Add first date to ArrayList
        var dateRangeArray: ArrayList<String> = ArrayList()
        dateRangeArray.add(selectedDate)

        // Set Calendar instance to selected date
        var sdf = SimpleDateFormat("yyyy-MM-dd")
        val c = Calendar.getInstance()
        c.setTime(sdf.parse(selectedDate));

        // Increase date by one for next 6 days
        for (i in 1..6) {

            c.add(Calendar.DATE, 1)
            sdf = SimpleDateFormat("yyyy-MM-dd")

            val resultdate = Date(c.timeInMillis)
            dateRangeArray.add(sdf.format(resultdate))
        }

        return dateRangeArray

    } // END getDateRange()



} // END SearchActivity class