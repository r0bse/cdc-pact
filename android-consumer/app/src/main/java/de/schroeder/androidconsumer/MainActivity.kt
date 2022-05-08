package de.schroeder.androidconsumer

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import de.schroeder.androidconsumer.configuration.RetrofitModule
import de.schroeder.androidconsumer.superheroes.boundary.SuperheroView
import de.schroeder.androidconsumer.superheroes.boundary.SuperheroViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class SuperheroApplication : Application() {

    private lateinit var koinApplication: KoinApplication

    override fun onCreate() {
        super.onCreate()
        koinApplication = startKoin {
            androidContext(this@SuperheroApplication)
            fragmentFactory()
            modules(RetrofitModule.invoke())
        }
    }
}

class MainActivity : KoinComponent, ComponentActivity() {

    private val superheroViewModel: SuperheroViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperheroView(superheroViewModel)
        }
    }
}