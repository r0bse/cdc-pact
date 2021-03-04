package de.schroeder.androidconsumer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    /**
     * on Click
     */
    fun retrieveSuperheroes(view: View) {
        val tableService = TableService(findViewById(R.id.superhero_table))
        RetrofitSuperheroRequest(tableService).getSuperheroes()
//        val requestService = RequestService(tableService, view.context)
//        requestService.callProvider()
    }

}