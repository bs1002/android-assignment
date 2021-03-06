package com.mahfuznow.android_assignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.mahfuznow.android_assignment.R
import com.mahfuznow.android_assignment.model.country.Country

class CountryAdapterDelegate(private val context: Context) :
    AdapterDelegate<ArrayList<Any>>() {

    public override fun isForViewType(items: ArrayList<Any>, position: Int) =
        items[position] is Country

    public override fun onCreateViewHolder(parent: ViewGroup) =
        CountryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        )

    public override fun onBindViewHolder(
        items: ArrayList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val country = items[position]
        country as Country
        holder as CountryViewHolder
        holder.textView.text = country.name
        Glide.with(holder.itemView.context)
            .load(country.flags.png)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.imageView)

        holder.itemView.setOnClickListener() {
            Toast.makeText(context, "You have clicked country: " + country.name, Toast.LENGTH_SHORT).show()
        }
    }

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}