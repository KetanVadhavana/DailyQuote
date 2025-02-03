package com.example.dailyquote.data.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.dailyquote.data.local.QuoteDatabase
import com.example.dailyquote.data.local.SharedPreferences
import com.example.dailyquote.data.remote.QuoteApiService
import com.example.dailyquote.data.repository.QuoteRepositoryImpl
import com.example.dailyquote.domain.repository.QuoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideQuoteApiService(retrofit: Retrofit): QuoteApiService {
        return retrofit.create(QuoteApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideQuoteDatabase(@ApplicationContext context: Context): QuoteDatabase {
        return Room.databaseBuilder(context, QuoteDatabase::class.java, "QuoteDb").build()
    }

    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)

    @Singleton
    @Provides
    fun provideQuoteRepository(
        quoteApi: QuoteApiService,
        quoteDatabase: QuoteDatabase
    ): QuoteRepository {
        return QuoteRepositoryImpl(quoteApi, quoteDatabase.quoteDao())
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) = SharedPreferences(context)

}
