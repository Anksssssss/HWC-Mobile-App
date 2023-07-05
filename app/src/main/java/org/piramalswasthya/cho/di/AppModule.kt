package org.piramalswasthya.cho.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.piramalswasthya.cho.database.room.InAppDb
import org.piramalswasthya.cho.database.room.dao.LanguageDao
import org.piramalswasthya.cho.database.room.dao.RegistrarMasterDataDao
import org.piramalswasthya.cho.database.room.dao.LoginSettingsDataDao
import org.piramalswasthya.cho.database.room.dao.StateMasterDao
import org.piramalswasthya.cho.database.room.dao.UserAuthDao
import org.piramalswasthya.cho.database.room.dao.UserDao
import org.piramalswasthya.cho.database.room.dao.VaccinationTypeAndDoseDao
import org.piramalswasthya.cho.database.room.dao.VisitReasonsAndCategoriesDao
import org.piramalswasthya.cho.database.shared_preferences.PreferenceDao
import org.piramalswasthya.cho.network.AmritApiService
//import org.piramalswasthya.cho.network.AmritApiService
//import org.piramalswasthya.sakhi.network.AbhaApiService
//import org.piramalswasthya.sakhi.network.AmritApiService
//import org.piramalswasthya.sakhi.network.D2DApiService
import org.piramalswasthya.cho.network.interceptors.ContentTypeInterceptor
import org.piramalswasthya.cho.network.interceptors.TokenInsertTmcInterceptor
//import org.piramalswasthya.sakhi.network.interceptors.TokenInsertAbhaInterceptor
//import org.piramalswasthya.sakhi.network.interceptors.TokenInsertD2DInterceptor
//import org.piramalswasthya.sakhi.network.interceptors.TokenInsertTmcInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val baseD2DUrl = "http://d2dapi.piramalswasthya.org:9090/api/"
    //"http://117.245.141.41:9090/api/"

    private const val baseTmcUrl = // "http://assamtmc.piramalswasthya.org:8080/"
    "http://uatamrit.piramalswasthya.org:8080/"

    private const val baseAbhaUrl = "https://healthidsbx.abdm.gov.in/api/"
    private const val sanjeevaniApi = "https://preprod.esanjeevaniopd.xyz/uat/aus/api/ThirdPartyAuth/"

    private val baseClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(ContentTypeInterceptor())
            .build()

    @Singleton
    @Provides
    fun provideMoshiInstance(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

//    @Singleton
//    @Provides
//    @Named("logInClient")
//    fun provideD2DHttpClient(): OkHttpClient {
//        return baseClient
//            .newBuilder()
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(30, TimeUnit.SECONDS)
//            .addInterceptor(TokenInsertD2DInterceptor())
//            .build()
//    }
//
    @Singleton
    @Provides
    @Named("uatClient")
    fun provideTmcHttpClient(): OkHttpClient {
        return baseClient
            .newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(TokenInsertTmcInterceptor())
            .build()
    }
//
//    @Singleton
//    @Provides
//    @Named("abhaClient")
//    fun provideAbhaHttpClient(): OkHttpClient {
//        return baseClient
//            .newBuilder()
//            .connectTimeout(20, TimeUnit.SECONDS)
//            .readTimeout(20, TimeUnit.SECONDS)
//            .writeTimeout(20, TimeUnit.SECONDS)
//            .addInterceptor(TokenInsertAbhaInterceptor())
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideD2DApiService(
//        moshi: Moshi,
//        @Named("logInClient") httpClient: OkHttpClient
//    ): D2DApiService {
//        return Retrofit.Builder()
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            //.addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(baseD2DUrl)
//            .client(httpClient)
//            .build()
//            .create(D2DApiService::class.java)
//    }
//
    @Singleton
    @Provides
    fun provideAmritApiService(
        moshi: Moshi,
        @Named("uatClient") httpClient: OkHttpClient
    ): AmritApiService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(sanjeevaniApi)
            .client(httpClient)
            .build()
            .create(AmritApiService::class.java)
    }
//
//    @Singleton
//    @Provides
//    fun provideAbhaApiService(
//        moshi: Moshi,
//        @Named("abhaClient") httpClient: OkHttpClient
//    ): AbhaApiService {
//        return Retrofit.Builder()
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            //.addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(baseAbhaUrl)
//            .client(httpClient)
//            .build()
//            .create(AbhaApiService::class.java)
//    }

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) = InAppDb.getInstance(context)

    @Singleton
    @Provides
    fun provideUserDao(database : InAppDb) : UserDao = database.userDao

    @Singleton
    @Provides
    fun provideUserAuthDao(database : InAppDb) : UserAuthDao = database.userAuthDao

    @Singleton
    @Provides
    fun provideLoginSettingsDataDao(database : InAppDb) : LoginSettingsDataDao = database.loginSettingsDataDao

    @Singleton
    @Provides
    fun provideLanguageDao(database : InAppDb) : LanguageDao = database.languageDao

    @Singleton
    @Provides
    fun provideVisitReasonsAndCategoriesDao(database : InAppDb) : VisitReasonsAndCategoriesDao = database.visitReasonsAndCategoriesDao

    @Singleton
    @Provides
    fun provideRegistrarMasterDataDao(database : InAppDb) : RegistrarMasterDataDao = database.registrarMasterDataDao

    @Singleton
    @Provides
    fun provideStateMasterDao(database : InAppDb) : StateMasterDao = database.stateMasterDao
    @Singleton
    @Provides
    fun provideVaccinationTypeAndDoseDao(database : InAppDb) : VaccinationTypeAndDoseDao = database.vaccinationTypeAndDoseDao
//    @Singleton
//    @Provides
//    fun provideBenIdDao(database : InAppDb) : BeneficiaryIdsAvailDao = database.benIdGenDao
//
//    @Singleton
//    @Provides
//    fun provideCbacDao(database : InAppDb) : CbacDao = database.cbacDao
//
//    @Singleton
//    @Provides
//    fun provideVaccineDao(database : InAppDb) : ImmunizationDao = database.vaccineDao
//
//    @Singleton
//    @Provides
//    fun provideMaternalHealthDao(database : InAppDb) : MaternalHealthDao = database.maternalHealthDao

    @Singleton
    @Provides
    fun providePreferenceDao(@ApplicationContext context: Context) = PreferenceDao(context)


}