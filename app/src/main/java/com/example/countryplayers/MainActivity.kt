package com.example.countryplayers

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.countryplayers.adapter.CountryAdapter
import com.example.testapp.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var countryList = arrayListOf<String>()
    lateinit var toolbar: Toolbar
    lateinit var lvCoutryView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbarCountryMain)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Choose Country"

        lvCoutryView = findViewById(R.id.lvCountryView)
        var adapter = CountryAdapter(this, countryList)

        if (ConnectionManager().checkConnection(this)) {

            try {
                val queue = Volley.newRequestQueue(this)
                val url = "http://test.oye.direct/players.json"

                val jsonObjectRequest =
                    object : JsonObjectRequest(Request.Method.GET, url, null,
                        Response.Listener { response ->
                            val data = response.keys()
                            var value = ""
                            while (data.hasNext()) {
                                countryList.add(data.next())
                            }
                            lvCoutryView.adapter = adapter
                            lvCoutryView.onItemClickListener = AdapterView.OnItemClickListener{
                                    parent, view, position, id ->
                                val selectedItem : String = parent.getItemAtPosition(position) as String
                                var intent = Intent(this, TeamDetails::class.java)
                                intent.putExtra("team_name",selectedItem)
                                startActivity(intent)
                            }

                        }, Response.ErrorListener {

                        }
                    ) {

                    }
                queue.add(jsonObjectRequest)

            } catch (e: JSONException) {
                Toast.makeText(applicationContext, "JSON Error occured", Toast.LENGTH_SHORT)
                    .show()
            }

        } else {
            val dialog =
                AlertDialog.Builder(this)
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