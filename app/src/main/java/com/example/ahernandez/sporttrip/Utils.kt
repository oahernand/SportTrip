package com.example.ahernandez.sporttrip

import android.content.Context
import com.example.ahernandez.sporttrip.model.Game
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

class Utils {


    fun parseCsvFromAssets(context: Context, filename: String): ArrayList<Game>? {

        var gameList = ArrayList<Game>()

        try {

            // Access .csv file
            csvReader().open(context.assets.open(filename)) {
                readAllAsSequence().forEach { row: List<String> ->

                    // Split string line
                    val line = row.toString().split(",").toTypedArray()

                    // Create Game object and add to list
                    var aGame: Game = Game(line[0].replace("[", ""),line[1], line[2], line[3], line[4], line[5])
                    gameList.add(aGame)

                }
            }

            // Delete first entry containing header info
            gameList.removeAt(0)

            return gameList

        } catch (ex: Exception)
        {
            ex.printStackTrace()
        }

        return null

    } // END parseCsvFromAssets()



} // END Utils class