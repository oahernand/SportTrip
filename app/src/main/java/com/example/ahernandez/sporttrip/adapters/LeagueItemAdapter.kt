package com.example.ahernandez.sporttrip.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ahernandez.sporttrip.MapActivity
import com.example.ahernandez.sporttrip.R


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
        holder.cardViewItem.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {

                // Get current item clicked
                val item = items.get(position)

                // Launch MapActivity
                val intent = Intent(context, MapActivity::class.java)
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