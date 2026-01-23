package com.alberti.memoryaid.di

import com.alberti.memoryaid.data.repository.RepositorioMemoriaImpl
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

/**
 * Módulo de Hilt encargado de la abstracción de repositorios.
 *
 * Utiliza la anotación [Binds] para asociar las interfaces de la capa de dominio
 * con sus implementaciones concretas en la capa de datos. Esto permite que el
 * resto de la aplicación dependa de abstracciones, respetando el principio
 * de inversión de dependencias (D de SOLID).
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Vincula la implementación [RepositorioMemoriaImpl] con la interfaz [RepositorioMemoria].
     *
     * Al usar [Binds] en lugar de Provides, Hilt genera código más eficiente ya que no
     * necesita crear una instancia del módulo ni ejecutar lógica de creación;
     * simplemente delega la construcción al constructor inyectado de la clase concreta.
     *
     * @param repositorioImpl La implementación concreta del repositorio.
     * @return La abstracción [RepositorioMemoria] que será inyectada en Use Cases o ViewModels.
     */
    @Binds
    @Singleton
    abstract fun bindRepositorioMemoria(
        repositorioImpl: RepositorioMemoriaImpl
    ): RepositorioMemoria
}
