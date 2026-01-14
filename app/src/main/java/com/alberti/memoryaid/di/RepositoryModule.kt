package com.alberti.memoryaid.di

import com.alberti.memoryaid.data.repository.RepositorioMemoriaImpl
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepositorioMemoria(
        repositorioImpl: RepositorioMemoriaImpl
    ): RepositorioMemoria
}