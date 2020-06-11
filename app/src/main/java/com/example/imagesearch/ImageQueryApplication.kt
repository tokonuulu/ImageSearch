package com.example.imagesearch

import android.app.Application
import com.example.imagesearch.data.db.FavoritesDatabase
import com.example.imagesearch.data.network.ImageApiService
import com.example.imagesearch.data.network.ImageNetworkDataSource
import com.example.imagesearch.data.network.ImageNetworkDataSourceImpl
import com.example.imagesearch.data.network.response.ConnectivityInterceptor
import com.example.imagesearch.data.network.response.ConnectivityInterceptorImpl
import com.example.imagesearch.data.repository.QueryRepository
import com.example.imagesearch.data.repository.QueryRepositoryImpl
import com.example.imagesearch.ui.image.query.ImageQueryViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ImageQueryApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ImageQueryApplication))

        bind() from singleton { FavoritesDatabase(instance()) }
        bind() from singleton { instance<FavoritesDatabase>().favoriteDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ImageApiService(instance()) }
        bind<ImageNetworkDataSource>() with singleton { ImageNetworkDataSourceImpl(instance()) }
        bind<QueryRepository>() with singleton { QueryRepositoryImpl(instance(), instance(), instance()) }
        bind() from provider { ImageQueryViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
    }
}