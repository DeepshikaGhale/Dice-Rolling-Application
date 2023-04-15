package com.example.dicerollingapplication

import android.content.Context
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.dicerollingapplication.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private val placeholder: String = "Choose"
    private var diceList = arrayListOf<Any>()
    var selectedDice = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //call data from shared-preferences
        readStoredDie()
//        storeUserDie()

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

                val die = Die(noOfSides = selectedDice)
                die.roll()
                binding.result1Id.text = die.randomSideUp.toString()
                die.roll()
                binding.result2Id.text = die.randomSideUp.toString()

            }else{
                Toast.makeText(this, "Please select dice to roll", Toast.LENGTH_SHORT).show()
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
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        //to store the value of user created die
        val editor = sharedPref.edit()
        editor.putString(key.toString(),die.toString())
        editor.apply()
    }

    //read user created die
    private fun readStoredDie(): ArrayList<Any>{
        diceList.clear() // clearing list before adding the stored data
        diceList.add(placeholder) //re-adding the placeholder in the first index

        //initialize shared preference
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        //to retrieve data
        val allEntries = sharedPref.all

        if (allEntries.isEmpty()){
            addDefaultDice()
        }

        for(i in 0..allEntries.size-1){
           val data = sharedPref.getString(i.toString(), "")
           //add dice from the local storage to list
           diceList.add(data.toString().toInt())
           Log.d("entries-data", data.toString())
        }

        Log.d("data-size", allEntries.size.toString())
        return diceList
    }

    //to store the data when app starts for the first time
    private fun addDefaultDice(){
        var initialDiceList = arrayListOf<Int>(4,6,8,10,12,20)
        //initialize shared preference
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        //to store the value of user created die
        val editor = sharedPref.edit()
        //to store the value of when the app runs for the first time
            for ( i in initialDiceList.indices){
                editor.putInt(i.toString(), initialDiceList[i].toString().toInt())
            }
    }

}