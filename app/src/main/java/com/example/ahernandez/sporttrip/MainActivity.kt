package com.example.ahernandez.sporttrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

class MainActivity : AppCompatActivity() {

    var gameList = ArrayList<Game>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        csvReader().open(this.assets.open("Games.csv")) {
            readAllAsSequence().forEach { row: List<String> ->
                //Do something
                println(row) //[a, b, c]
                val strs = row.toString().split(",").toTypedArray()
                println(strs.size.toString())

                var aGame: Game = Game(strs[0].replace("[", ""),strs[1], strs[3])

                gameList.add(aGame)

            }
        }

        gameList.removeAt(0)
        println("The total # of loaded games is " + gameList.size.toString())



    }



}