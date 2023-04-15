package com.example.dicerollingapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.dicerollingapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    //static variables
    private val placeholder: String = "Choose"
    private val localStorage: String = "Preferences"
    //list to store dice
    private var diceList : ArrayList<Any>  = arrayListOf<Any>()

    //stores the value of dice selected by user
    var selectedDice = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //call data from shared-preferences
        readStoredDie()

        //adapter for spinner, the create from resources method is used to create an dropdown using the array of created in the resource file
        val adapter = ArrayAdapter(this, R.layout.spinner_item, diceList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //assigning the adapter we created to the spinner's adapter
        binding.diceSpinner.adapter = adapter
        binding.diceSpinner.onItemSelectedListener = this

        //roll the dice once
        binding.rollOnceId.setOnClickListener(){
            //validate
            if (selectedDice != 0){
                binding.resultSection.visibility = View.VISIBLE
                binding.result2Id.visibility = View.VISIBLE
                binding.resultLabelId.text = "Random side up for dice $selectedDice is:"
                val die = Die(noOfSides = selectedDice)
                die.roll()
                binding.result1Id.text = die.randomSideUp.toString()
                binding.result2Id.visibility = View.GONE
            }else{
                Toast.makeText(this, "Please select dice to roll", Toast.LENGTH_SHORT).show()
            }
        }

        //roll the dice twice
        binding.rollTwiceId.setOnClickListener(){
            //validate
            if (selectedDice != 0){
                binding.resultSection.visibility = View.VISIBLE
                binding.result2Id.visibility = View.VISIBLE
                binding.result2Id.visibility = View.VISIBLE
                binding.resultLabelId.text = "Random side up for dice $selectedDice is:"

                val die = Die(noOfSides = selectedDice)
                die.roll()
                binding.result1Id.text = die.randomSideUp.toString()
                die.roll()
                binding.result2Id.text = die.randomSideUp.toString()

            }else{
                Toast.makeText(this, "Please select dice to roll", Toast.LENGTH_SHORT).show()
            }

        }

        //create dice by user
        binding.createDice.setOnClickListener(){
            var userDice = binding.dice.text.toString()
            if (userDice.isNotEmpty()){
                //user can add the dice only if the user given dice size is not already present
                if(!validateUserDiceSize(userDice.toInt())){
                    val key = diceList.size - 1
                    storeUserDie(key, userDice.toInt())
                    binding.dice.text.clear()
                    Toast.makeText(this, "Your dice has been added.", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Given dice size is already present.", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Please enter a dice size", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        var selectedSpinnerItem = binding.diceSpinner.selectedItem.toString()
        if(selectedSpinnerItem != placeholder){
            selectedDice = binding.diceSpinner.selectedItem.toString().toInt()
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    //SHARED PREFERENCES

    //store user created die
    private fun storeUserDie(key: Int, die: Int){
        //initialize shared preference
        val sharedPref = getSharedPreferences(localStorage, Context.MODE_PRIVATE)
        //to store the value of user created die
        val editor = sharedPref.edit()
        editor.putInt(key.toString(),die)
        editor.apply()
        diceList += die //adding die to the list
    }

    //read user created die
    private fun readStoredDie(){
        diceList.clear() // clearing list before adding the stored data
        diceList.add(placeholder) //re-adding the placeholder in the first index

        //initialize shared preference
        val sharedPref = getSharedPreferences(localStorage, Context.MODE_PRIVATE)
        //to retrieve data
        val allEntries = sharedPref.all

        if (allEntries.isEmpty()){
            addDefaultDice()
            for(i in 0..allEntries.size-1){
                val data = sharedPref.getInt(i.toString(), 0)
                //add dice from the local storage to list
                diceList.add(data)
            }
        }

        for(i in 0..allEntries.size-1){
            Log.d("allentries", allEntries.toString())

            val data = sharedPref.getInt(i.toString(), 0)
           //add dice from the local storage to list
           diceList.add(data)

        }
    }

    //to store the data when app starts for the first time
    private fun addDefaultDice(){
        var initialDiceList = arrayListOf<Int>(4,6,8,10,12,20)
        //initialize shared preference
        val sharedPref = getSharedPreferences(localStorage, Context.MODE_PRIVATE)
        //to store the value of user created die
        val editor = sharedPref.edit()
        //to store the value of when the app runs for the first time
            for ( i in initialDiceList.indices){
                editor.putInt(i.toString(), initialDiceList[i].toString().toInt())
                editor.apply()
            }

        val allEntries = sharedPref.all
        Log.d("size-after-adding", allEntries.size.toString())
    }

    //validate if the user dice size is already present
    private fun validateUserDiceSize(dice: Int): Boolean{

        var hasDice = diceList.contains(dice)

        if(hasDice){
            Toast.makeText(this, "Please enter a different dice", Toast.LENGTH_SHORT).show()
        }

        return hasDice
    }

}