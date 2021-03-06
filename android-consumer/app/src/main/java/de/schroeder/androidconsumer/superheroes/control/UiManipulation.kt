package de.schroeder.androidconsumer.superheroes.control

import android.graphics.Color
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import de.schroeder.androidconsumer.superheroes.enitity.SuperheroResource
import java.util.*


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