package com.zfml.debtnote.di

import android.app.Application
import androidx.room.Room
import com.zfml.debtnote.data.local.DebtDao
import com.zfml.debtnote.data.local.DebtDatabase
import com.zfml.debtnote.data.repository.DebtRepositoryImpl
import com.zfml.debtnote.domain.repository.DebtRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): DebtDatabase {
       return Room.databaseBuilder(
           app,
           DebtDatabase::class.java,
           "debt.db"
       ).build()
    }

    @Provides
    @Singleton
    fun provideDebtDao(db: DebtDatabase): DebtDao {
        return db.debtDao
    }

    @Provides
    @Singleton
    fun provideDebtRepository(dao: DebtDao) : DebtRepository {
        return DebtRepositoryImpl(dao)
    }


}