package de.schroeder.androidconsumer

import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import de.schroeder.androidconsumer.superheroes.control.SuperheroTableManager
import de.schroeder.androidconsumer.superheroes.control.TableService
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class SuperheroApplication : Application() {}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var retrofitSuperheroClient: RetrofitSuperheroClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    /**
     * on Click on Button (defined in xml)
     */
    fun retrieveSuperheroes(view: View) {
        val tableService = TableService(findViewById(R.id.superhero_table))
        RetrofitRequests(SuperheroTableManager(tableService), retrofitSuperheroClient).getSuperheroes()
    }

}