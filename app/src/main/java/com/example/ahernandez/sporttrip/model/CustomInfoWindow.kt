package com.example.ahernandez.sporttrip.model

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.ahernandez.sporttrip.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindow(context: Context) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.custom_info_window, null)

    private fun rendowWindowText(marker: Marker, view: View){

        // Variable represents Arena name
        val tvTitle = view.findViewById<TextView>(R.id.titleTextView)

        // Variables represent matchup info
        val tvTeam1 = view.findViewById<TextView>(R.id.team1TextView)
        val tvTeam1Goal = view.findViewById<TextView>(R.id.team1GoalTextView)
        val tvTeam2 = view.findViewById<TextView>(R.id.team2TextView)
        val tvTeam2Goal = view.findViewById<TextView>(R.id.team2GoalTextView)
        val tvEnding = view.findViewById<TextView>(R.id.endingTextView)


        // Fill placeholders
        tvTitle.text = marker.title

        // TODO: REPLACE HARDCODED VALUES - USE LIST
        tvTeam1.text = "St. Louis Blues"
        tvTeam1Goal.text = "4"
        tvTeam2.text = "Colorado Avalanche"
        tvTeam2Goal.text = "1"
        tvEnding.text = "OT"

    } // END rendowWindowText()



    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }



    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }


} // END CustomInfoWindow class