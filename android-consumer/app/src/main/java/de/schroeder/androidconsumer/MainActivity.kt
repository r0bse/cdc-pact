package de.schroeder.androidconsumer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity() : AppCompatActivity() {

    val requestService: RequestService = RequestService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun retrieveSuperheroes(view: View) {

        requestService.callProvider(findViewById(R.id.superhero_table))
    }

}