package de.schroeder.androidconsumer

import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.function.Consumer
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


class RetrofitSuperheroRequest(val tableService: TableService) : Callback<List<Superhero>> {

    companion object{
        const val BASE_URL = "http://10.0.2.2:8080"
    }

    fun getSuperheroes() {

        val providerClient: ProviderClient = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                .setLenient()
                .create()))
            .build().create(ProviderClient::class.java)
        val call: Call<List<Superhero>> = providerClient.getSuperheroes()
        call.enqueue(this)
    }

    override fun onResponse(call: Call<List<Superhero>>, response: Response<List<Superhero>>) {
        if (response.isSuccessful) {
            response.body().forEach{superhero -> tableService.addTableRow(superhero)}
        } else {
            println(response.errorBody())
        }
    }

    override fun onFailure(call: Call<List<Superhero>>?, t: Throwable) {
        t.printStackTrace()
    }
}

interface ProviderClient {

    @GET("/superheroes")
    fun getSuperheroes(): Call<List<Superhero>>
}

data class Superhero(
    val name: String,
    val secretIdentity: String,
    val affiliation: String
) : JSONObject()



class TableService(val tableLayout: TableLayout) {


    fun addTableRow(hero: Superhero) {

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