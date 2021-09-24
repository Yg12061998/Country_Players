package com.example.countryplayers.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextClock
import android.widget.TextView
import com.example.countryplayers.R
import com.example.countryplayers.model.Player

class PlayerAdapter(private val context: Context, private val teamList: ArrayList<Player>) :
    BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return teamList.size
    }

    override fun getItem(p0: Int): Any {
        return teamList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.player_single_row, p2, false)
        val tvFirst = rowView.findViewById(R.id.tvFirstName) as TextView
        val tvSecond = rowView.findViewById(R.id.tvSecondName) as TextView
        val player:Player = getItem(p0) as Player

        tvFirst.text = player.first
        tvSecond.text = player.second

        var captain:Boolean = false
        captain = player.captain

        if(captain){
            tvFirst.typeface = Typeface.DEFAULT_BOLD
            tvSecond.typeface = Typeface.DEFAULT_BOLD
        }
        return rowView
    }


}