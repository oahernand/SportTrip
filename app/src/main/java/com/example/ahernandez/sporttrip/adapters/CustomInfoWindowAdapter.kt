package com.example.ahernandez.sporttrip.adapters

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.ahernandez.sporttrip.R
import com.example.ahernandez.sporttrip.model.Game
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CustomInfoWindowAdapter(context: Context) : GoogleMap.InfoWindowAdapter {

    lateinit var gameInfoObj: Game
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.custom_info_window, null)

    private fun renderWindowText(marker: Marker, view: View){

        // Variable represents Arena name
        val tvTitle = view.findViewById<TextView>(R.id.titleTextView)

        // Variables represent matchup info
        val tvDate = view.findViewById<TextView>(R.id.dateTextView)
        val tvTeam1 = view.findViewById<TextView>(R.id.team1TextView)
        val tvTeam1Goal = view.findViewById<TextView>(R.id.team1GoalTextView)
        val tvTeam2 = view.findViewById<TextView>(R.id.team2TextView)
        val tvTeam2Goal = view.findViewById<TextView>(R.id.team2GoalTextView)
        val tvEnding = view.findViewById<TextView>(R.id.endingTextView)

        // Convert JSON string contained in Snippet property back to Game object
        val gson = Gson()
        val gameInfoType = object : TypeToken<Game>() {}.type
        gameInfoObj = gson.fromJson(marker.snippet, gameInfoType)

        // Add Title
        tvTitle.text = marker.title

        // Fill placeholders using Game obj values
        tvDate.text = gameInfoObj.date
        tvTeam1.text = gameInfoObj.home
        tvTeam2.text = gameInfoObj.visitor

        // Format accordingly
        if (gameInfoObj.homeGoals.isNullOrEmpty() && gameInfoObj.visitorGoals.isNullOrEmpty()) {
            tvTeam1Goal.text = "0"
            tvTeam2Goal.text = "0"
        }
        else {
            tvTeam1Goal.text = gameInfoObj.homeGoals
            tvTeam2Goal.text = gameInfoObj.visitorGoals
        }

        // Format accordingly
        if (gameInfoObj.ending.isNullOrEmpty() && gameInfoObj.homeGoals.isNullOrEmpty() && gameInfoObj.visitorGoals.isNullOrEmpty())
            tvEnding.text = "7:00 PM (EST)"
        else
            tvEnding.text = gameInfoObj.ending


    } // END renderWindowText()



    override fun getInfoContents(marker: Marker): View {
        renderWindowText(marker, mWindow)
        return mWindow
    }



    override fun getInfoWindow(marker: Marker): View? {
        renderWindowText(marker, mWindow)
        return mWindow
    }


} // END CustomInfoWindow class