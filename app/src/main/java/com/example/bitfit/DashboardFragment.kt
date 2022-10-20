package com.example.bitfit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private val healthItems = mutableListOf<HealthItem>()
    private val RequestCode = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val maxCalTextView = view.findViewById<TextView>(R.id.TextView_max)
        val minCalTextView = view.findViewById<TextView>(R.id.TextView_min)
        val avgCalTextView = view.findViewById<TextView>(R.id.TextView_avg)

        view?.findViewById<Button>(R.id.button_add_item)?.setOnClickListener{
            val intent = Intent(activity, SecondActivity::class.java)
            startActivityForResult(intent, RequestCode)
        }

        lifecycleScope.launch {
            (activity?.application as HealthApplication).db.healthDao().getAll().collect { databaseList ->
                databaseList.map { item ->
                    HealthItem(
                        itemTitle = item.item,
                        calories = item.calories

                    )
                }.also { mappedList ->
                    healthItems.clear()

                    Log.i("HealthFragment", mappedList.toString())
                    healthItems.addAll(mappedList)


                    var max_cal  =0.0
                    for (calories in healthItems.map{it.calories} ){
                        if(max_cal < calories!!) {
                            max_cal = calories
                        }
                    }
                    maxCalTextView.text = max_cal.toString()


                    var min_cal = max_cal
                    for (calories in healthItems.map{it.calories} ){
                        if(min_cal > calories!!) {
                            min_cal = calories
                        }
                    }
                    minCalTextView.text = min_cal.toString()


                    var total = 0.0

                    for (calories in healthItems.map{it.calories}){
                        if (calories != null) {
                            total += calories
                        }
                    }

                    var numItem = if (healthItems.size >= 1) healthItems.size else 1

                    var avgCal = total/numItem
                    avgCalTextView.text = String.format("%.1f", avgCal)

                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == RequestCode && resultCode == Activity.RESULT_OK) {

            intentData?.let{data ->
                lifecycleScope.launch(Dispatchers.IO) {
                    (activity?.application as HealthApplication).db.healthDao().insertAll(
                        HeathEntity(
                            id = 0,
                            item = data?.getStringExtra(SecondActivity.EXTRA_FOOD),
                            calories = data?.getStringExtra(SecondActivity.EXTRA_CALORIES)?.toDouble()
                        )
                    )
                }
            }

        } else {
            Toast.makeText(
                context,
                "Unsaved",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}