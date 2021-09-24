package com.example.countryplayers

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.countryplayers.adapter.PlayerAdapter
import com.example.countryplayers.model.Player
import com.example.testapp.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class TeamDetails : AppCompatActivity() {

    var team: String = "NOT FOUND"
    lateinit var toolbar: Toolbar
    lateinit var lvPlayerView: ListView
    var teamList : ArrayList<Player> = arrayListOf<Player>()
    lateinit var btnFirst:Button
    lateinit var btnSecond:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_details)

        btnFirst = findViewById(R.id.btnSortFirst)
        btnSecond = findViewById(R.id.btnSortSecond)

        toolbar = findViewById(R.id.toolbarPlayer)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Players"

        lvPlayerView = findViewById(R.id.lvPlayerView)
        var adapter = PlayerAdapter(this, teamList)

        if (intent != null) {
            team = intent.getStringExtra("team_name").toString()
        } else {
            Toast.makeText(this, "Intent is NUll", Toast.LENGTH_SHORT).show()
        }

        if (team == "NOT FOUND") {
            finish()
            Toast.makeText(this, "Product Not Found", Toast.LENGTH_SHORT).show()
        }

        if (ConnectionManager().checkConnection(this)) {
            val queue = Volley.newRequestQueue(this)
            val url = "http://test.oye.direct/players.json"

            try {
                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener { response ->
                        val data = response.getJSONArray(team)

                        for (i in 0 until data.length()) {
                            val temp : JSONObject = data[i] as JSONObject
                            val name = temp.getString("name").split(" ")
                            var captain = false
                            try {
                                captain = temp.getBoolean("captain")
                            }catch(e : Exception ){ }

                            val tem : Player
                            if(name.size == 2)
                                tem = Player(name[0], name[1], captain)
                            else
                                tem = Player(name[0], " ", captain)
                            teamList.add(tem)
                        }
                        lvPlayerView.adapter = adapter

                        btnFirst.setOnClickListener{
                            val list = teamList.sortedBy { player -> player.first  }
                            teamList.clear()
                            teamList.addAll(list)
                            lvPlayerView.adapter = adapter
                        }
                        btnSecond.setOnClickListener{
                            val list = teamList.sortedBy { player -> player.second  }
                            teamList.clear()
                            teamList.addAll(list)
                            lvPlayerView.adapter = adapter
                        }


                    }, Response.ErrorListener {
                        Toast.makeText(
                            applicationContext,
                            "Rsponse Error occured",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    override fun getHeaders(): MutableMap<String, String> {
                        return super.getHeaders()
                    }
                }
                queue.add(jsonObjectRequest)


            } catch (e: JSONException) {
                Toast.makeText(applicationContext, "JSON Error occured", Toast.LENGTH_SHORT).show()
            }

        } else {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Failed!")
            dialog.setMessage("Network is not connected")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()

            }
            dialog.setNegativeButton("Exit App") { text, listener ->
                finishAffinity()
            }

            dialog.create()
            dialog.show()
        }


    }
}