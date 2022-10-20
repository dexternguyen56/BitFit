package com.example.bitfit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView




class HealthItemAdapter(private val context: Context, private val healthItems: List<HealthItem>) :
    RecyclerView.Adapter<HealthItemAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.health_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = healthItems[position]
        holder.bind(item)
    }

    override fun getItemCount() = healthItems.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val TextView_title = itemView.findViewById<TextView>(R.id.TextView_item_title)
        private val TextView_cal = itemView.findViewById<TextView>(R.id.TextView_calories)


        fun bind(item: HealthItem) {
            TextView_title.text = item.itemTitle
            TextView_cal.text = item.calories.toString()
        }
    }

}