package de.schroeder.androidconsumer

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    val providerClient: ProviderClient = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .create()
            )
        )
        .build().create(ProviderClient::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    /**
     * on Click on Button (defined in xml)
     */
    fun retrieveSuperheroes(view: View) {
        val tableService = TableService(findViewById(R.id.superhero_table))
        RetrofitRequests(SuperheroTableManager(tableService), providerClient).getSuperheroes()
    }

}

class SuperheroTableManager(val tableService: TableService){

    fun addSuperheroToTable(heroes: List<SuperheroResource>){
        tableService.clearTable()
        heroes.forEach{ superhero -> tableService.addHeroRow(superhero)}
    }
}

class TableService(val tableLayout: TableLayout) {

    fun clearTable(){
        tableLayout.removeAllViews()
        setRandomBackgroundColor()
    }

    fun addHeroRow(hero: SuperheroResource) {

        val row = TableRow(tableLayout.context)

        val nameCell = TextView(tableLayout.context)
        nameCell.text = hero.name
        row.addView(nameCell)

        val identityCell = TextView(tableLayout.context)
        identityCell.text = hero.secretIdentity
        row.addView(identityCell)

        val affiliationCell = TextView(tableLayout.context)
        affiliationCell.text = hero.affiliation
        row.addView(affiliationCell)

        tableLayout.addView(row)
    }

    fun setRandomBackgroundColor() {
        val rnd = Random()
        tableLayout.setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)))
    }
}