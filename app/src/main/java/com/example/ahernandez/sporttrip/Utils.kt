package com.example.ahernandez.sporttrip

import android.content.Context
import com.example.ahernandez.sporttrip.model.Game
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.InputStream

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
                    var aGame: Game = Game(line[0].replace("[", ""),line[1], line[2], line[3], line[4], line[5].replace("]", ""))
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



    fun loadJsonFromAssets(context: Context, filename: String): String? {
        var input: InputStream? = null
        var jsonString: String

        try {

            // Create InputStream
            input = context.assets.open(filename)

            val size = input.available()

            // Create a buffer w the size
            val buffer = ByteArray(size)

            // Read data from InputStream into the Buffer
            input.read(buffer)

            // Create a json String
            jsonString = String(buffer)


            return jsonString

        } catch (ex: Exception)
        {
            ex.printStackTrace()
        }
        finally {
            // Must close the stream
            input?.close()
        }

        return null

    }



} // END Utils class