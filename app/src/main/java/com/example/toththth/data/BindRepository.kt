package com.example.toththth.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.toththth.data.AuthRepository
import com.example.toththth.data.AuthRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class BindRepository {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}
