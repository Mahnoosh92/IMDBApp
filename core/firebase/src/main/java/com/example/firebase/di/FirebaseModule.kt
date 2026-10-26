package com.example.firebase.di

import com.example.firebase.crashlytics.CrashReportingRepository
import com.example.firebase.crashlytics.DefaultCrashReportingRepository
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.performance
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModule {

    @Binds
    @Singleton
    abstract fun bindCrashReportingRepository(impl: DefaultCrashReportingRepository): CrashReportingRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseApp(): FirebaseApp {
            return FirebaseApp.getInstance()
        }

        @Provides
        @Singleton
        fun provideFirebaseCrashlytics(): FirebaseCrashlytics {
            return FirebaseCrashlytics.getInstance()
        }

        @Provides
        @Singleton
        fun provideFirebasePerformance(): FirebasePerformance {
            return Firebase.performance
        }
    }
}
