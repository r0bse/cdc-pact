package de.schroeder.androidconsumer.configuration

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.schroeder.androidconsumer.superheroes.boundary.SuperheroViewModel
import de.schroeder.androidconsumer.superheroes.control.SuperheroClient
import de.schroeder.androidconsumer.superheroes.control.SuperheroDeserializer
import de.schroeder.androidconsumer.superheroes.control.SuperheroRepository
import de.schroeder.androidconsumer.superheroes.control.SuperheroesDeserializer
import de.schroeder.androidconsumer.superheroes.enitity.SuperheroResource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


object RetrofitModule {

    private const val PROVIDER_SERVICE_IP = "192.168.178.30"
    private const val PROVIDER_SERVICE_PORT = 8081

    fun invoke(): Module {
        return module {
            single{ SuperheroRepository(get()) }
            viewModel{ SuperheroViewModel(get())}
            factory { retrofitBuilder(androidContext()).create(SuperheroClient::class.java) }
        }
    }

    private fun retrofitBuilder(context: Context): Retrofit{
        val superHeroesListType: Type = object : TypeToken<List<SuperheroResource?>?>() {}.type
        return Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor()) // adds possibility to view requests in log
                    .addInterceptor(
                        ChuckerInterceptor.Builder(context).build()
                    ).build()
            ) // adds possibility to view requests on device
            .baseUrl("http://$PROVIDER_SERVICE_IP:$PROVIDER_SERVICE_PORT") // points to a locally running backend service
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(superHeroesListType, SuperheroesDeserializer())
                        .registerTypeAdapter(SuperheroResource::class.java, SuperheroDeserializer())
                        .setLenient()
                        .create()
                )
            )
            .build()
    }
}