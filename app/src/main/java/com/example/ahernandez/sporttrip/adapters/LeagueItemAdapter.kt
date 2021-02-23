package com.example.ahernandez.sporttrip.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ahernandez.sporttrip.MapActivity
import com.example.ahernandez.sporttrip.R
import com.example.ahernandez.sporttrip.Utils
import com.example.ahernandez.sporttrip.model.Game
import com.google.gson.Gson
import com.google.gson.GsonBuilder


class LeagueItemAdapter(val context: Context, val items: ArrayList<String>):
    RecyclerView.Adapter<LeagueItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.league_item_layout, parent, false
                )
        )

    } // END onCreateViewHolder()



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)

        // TODO: Possibly set specific image for item
        // Set Item name
        holder.tvItem.text = item

        // TODO: Possibly remove
        if (position % 2 == 0) {
            holder.cardViewItem.setBackgroundColor(
                    ContextCompat.getColor(
                            context, R.color.SlateGrey
                    )
            )
        } else {
            holder.cardViewItem.setBackgroundColor(
                    ContextCompat.getColor(
                            context, R.color.SlateGrey
                    )
            )
        }


        // TODO: CHANGE TO TRIGGERING CalenderView
        // TODO: REMOVE HARDCODED DATA PASSED TO ACTIVITY
        holder.cardViewItem.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {

                // Get current item clicked
                val item = items.get(position)

                // Get league name
                var leagueName: String = item

                // Load in .csv file and retrieve games matching date only
                val utils = Utils()
                var gameList: ArrayList<Game> = utils.parseCsvFromAssets(context, "Games.csv")!!
                var monthList: List<Game> = gameList.filter { s -> s.date == "2021-01-13" }

                // Convert list to JSON to pass in Intent
                val gson = Gson()
                val dailyGamesJson: String = gson.toJson(monthList)

                // Launch MapActivity
                val intent = Intent(context, MapActivity::class.java)
                intent.putExtra("league", leagueName)
                intent.putExtra("gameList", dailyGamesJson)
                context.startActivity(intent)

            }
        })

    } // END setOnClickListener



    override fun getItemCount(): Int {
        return items.size

    } // END getItemCount()



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextViews that each item will be added to
        val tvItem: TextView = view.findViewById(R.id.tv_item_name)
        val cardViewItem: CardView = view.findViewById(R.id.card_view_item)

    } // END ViewHolder class



} // END LeagueItemAdapter class