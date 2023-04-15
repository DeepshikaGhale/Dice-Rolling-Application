package com.example.dicerollingapplication

import kotlin.random.Random

class Die {
    //define variables
    var noOfSides : Int = 0
    var randomSideUp : Int = 0

    //primary constructor with zero arguments
    constructor(){
        noOfSides = 6
        roll()
    }
    //secondary constructors
    //takes the number of sides value from the user
    constructor(noOfSides: Int): this(){
        this.noOfSides = noOfSides
        roll()
    }

    //provides random value to randomSideUp
    fun roll(){
        randomSideUp = Random.nextInt(1, noOfSides+1)
    }
}