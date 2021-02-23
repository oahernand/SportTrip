package com.example.ahernandez.sporttrip.model

class Game {

    var date: String? = null
    var visitor: String? = null
    var visitorGoals: String? = null
    var home: String? = null
    var homeGoals: String? = null
    var ending: String? = null

    constructor(date: String?, visitor: String?, visitorGoals: String?, home: String?, homeGoals: String?, ending: String?) {
        this.date = date
        this.visitor = visitor
        this.visitorGoals = visitorGoals
        this.home = home
        this.homeGoals = homeGoals
        this.ending = ending
    }


} // END Game class