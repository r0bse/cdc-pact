package de.schroeder.androidconsumer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

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
        RetrofitRequests(tableService, "http://10.0.2.2:8080").getSuperheroes()
    }

}

class TableService(val tableLayout: TableLayout) {


    fun addTableRow(hero: SuperheroResource) {

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
}