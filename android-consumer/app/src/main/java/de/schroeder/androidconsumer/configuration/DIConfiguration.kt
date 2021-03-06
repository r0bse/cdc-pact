package de.schroeder.androidconsumer.configuration

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import de.schroeder.androidconsumer.RetrofitSuperheroClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun converterFactory(): Converter.Factory{
        return GsonConverterFactory.create(
            GsonBuilder()
                .setLenient()
                .create())
    }

    @Provides
    @Singleton
    fun retrofitBuilder(converterFactory: Converter.Factory): Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    fun providerCLient(retrofitBuilder: Retrofit): RetrofitSuperheroClient {
        return retrofitBuilder.create(RetrofitSuperheroClient::class.java)
    }
}



//@Module
//@InstallIn(ApplicationComponent::class)
//class TableModule {
//
//    @Provides
//    @Singleton
//    fun converterFactory(): Converter.Factory{
//        return GsonConverterFactory.create(
//            GsonBuilder()
//                .setLenient()
//                .create())
//    }
//}