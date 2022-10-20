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
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class HealthFragment : Fragment() {

    private val RequestCode = 1
    private val healthItems = mutableListOf<HealthItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvItems)
        val adapter = HealthItemAdapter(requireContext(),healthItems)
        recyclerView?.adapter = adapter

        recyclerView?.layoutManager = LinearLayoutManager(requireContext()).also {
            val dividerItemDecoration = DividerItemDecoration(requireContext(), it.orientation)
            recyclerView?.addItemDecoration(dividerItemDecoration)
        }

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
                    //for (food in mappedList){
                    Log.i("FoodFragment", mappedList.toString())
                    healthItems.addAll(mappedList)
                    adapter.notifyDataSetChanged()
                }
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == RequestCode && resultCode == Activity.RESULT_OK) {

            intentData?.let{data ->
                lifecycleScope.launch(IO) {
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
