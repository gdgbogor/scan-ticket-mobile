package id.gdev.regist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.gdev.regist.data.RegistrationRepository
import id.gdev.regist.data.source.remote.FireStoreEvent
import id.gdev.regist.data.source.remote.RemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    fun provideFireStoreEvent() = FireStoreEvent()

    @Provides
    fun provideRemoteDataSource(
        fireStoreEvent: FireStoreEvent
    ) = RemoteDataSource(fireStoreEvent)

    @Provides
    fun provideRepository(
        remoteDataSource: RemoteDataSource
    ) = RegistrationRepository(remoteDataSource)
}