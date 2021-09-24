package com.example.countryplayers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.countryplayers.R

class CountryAdapter(private val context: Context, private val countryList: ArrayList<String>): BaseAdapter(){

    private val inflater:LayoutInflater =context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return countryList.size
    }

    override fun getItem(p0: Int): Any {
        return countryList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.country_single_row, p2, false)
        val tvCountryName =rowView.findViewById(R.id.tvCountryName) as TextView
        tvCountryName.text = getItem(p0) as String
        return rowView
    }

}