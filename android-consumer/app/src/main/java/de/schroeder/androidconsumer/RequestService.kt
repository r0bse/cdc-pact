package de.schroeder.androidconsumer

import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject


class RequestService(val tableService: TableService) {

    val gsonMapper = GsonBuilder().create()

    fun callProvider(tableLayout: TableLayout) {
        val queue = Volley.newRequestQueue(tableLayout.context)
        val url = "http://10.0.2.2:8080/superheroes"

        // Request a string response from the provided URL.
        val request = JsonArrayRequest(Request.Method.GET, url, null,
                {
                    val list = gsonMapper.fromJson(it.toString(), Array<Superhero>::class.java).toList()
                    list.forEach { entry -> tableService.addTableRow(tableLayout, entry) }
                },
                { error ->
                    Log.d("", error.message.toString())
                })
        queue.add(request)
    }
}

class TableService{


    fun addTableRow(tableLayout: TableLayout, hero: Superhero) {

        val row = TableRow(tableLayout.context)

        val nameCell = TextView(tableLayout.context)
        nameCell.text = hero.name
        row.addView(nameCell)

        val identityCell = TextView(tableLayout.context)
        identityCell.text = hero.identity
        row.addView(identityCell)

        val affiliationCell = TextView(tableLayout.context)
        affiliationCell.text = hero.affiliiation
        row.addView(affiliationCell)

        tableLayout.addView(row)
    }
}

data class Superhero(
    val name: String,
    val identity: String,
    val affiliiation: String
) : JSONObject()