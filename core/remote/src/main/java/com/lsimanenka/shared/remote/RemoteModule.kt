package com.lsimanenka.shared.remote

import com.lsimanenka.shared.datastore.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.lsimanenka.shared.remote.auth.AuthApi
import dagger.Binds

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteModule {

    @Binds
    @Singleton
    abstract fun bindsWorkoutSyncManager(
        workoutSyncManagerImpl: WorkoutSyncManagerImpl
    ) : WorkoutSyncManager

    companion object {

        @Provides
        @Singleton
        fun provideAuthInterceptor(tokenManager: TokenManager): Interceptor {
            return Interceptor { chain ->
                val requestBuilder = chain.request().newBuilder()

                val token = tokenManager.getToken()
                if (!token.isNullOrBlank()) {
                    requestBuilder.addHeader("user-token", token)
                }

                requestBuilder.addHeader("Content-Type", "application/json")

                chain.proceed(requestBuilder.build())
            }
        }

        @Provides
        @Singleton
        fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun provideAuthApi(retrofit: Retrofit): AuthApi {
            return retrofit.create(AuthApi::class.java)
        }

        @Provides
        @Singleton
        fun provideWorkoutApi(retrofit: Retrofit): WorkoutApi {
            return retrofit.create(WorkoutApi::class.java)
        }
    }
}