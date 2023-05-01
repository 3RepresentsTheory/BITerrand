package com.example.biterrand_fix.data

import com.example.biterrand_fix.network.DemandsService
import com.example.biterrand_fix.network.UserBasicService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


/**
 * The container of this app, contain all repository of this app,
 * meanwhile it also manage all the dependency and its configuration
 */
interface AppContainer{

    val demandRepository:DemandRepository
    val userbasicinfoRepository:UserBasicRepository

    //more repository wait to add
}


/**
 * the default app container:
 *      demandRepository: using the service from network
 *      xx:xxx....
 *
 * Originally , we have such implementation link list:
 *      DefaultAppContainer(AppContainer) --> DefaultRepository(DemandRepository)
 * this way we hard code the source of network repository, for more flexibility (like
 * using different network library or use the pseudoTesting data), we can use DI,pass the
 * argument to the repository.
 *
 * Also, we remove the configuration of retrofit from api service to here, for convenience that
 * managing dependency here
 */

class DefaultAppContainer:AppContainer{
    /**
     * Retrofit api service configure, we have to make this app container be single
     */

    /**
     * Here Base_url is http, so we have to add
     *      android:usesCleartextTraffic="true"
     * into manifests,otherwise the retrofit will throw io exception preventing
     * cleartext transformation
     */

    private val BASE_URL = "http://101.43.254.234:8946/"

//    private val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"

    private val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_URL)
        .build()

//    private val retrofit: Retrofit = Retrofit.Builder()
//        .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
//        .baseUrl(BASE_URL)
//        .build()

    private val DemandApiService : DemandsService by lazy {
        retrofit.create(DemandsService::class.java)
    }
    private val UserBasicInfoService:UserBasicService by lazy{
        retrofit.create(UserBasicService::class.java)
    }

    override val demandRepository: DemandRepository by lazy{
        NetworkDemandRepository(DemandApiService)
    }
    override val userbasicinfoRepository: UserBasicRepository by lazy{
        NetworkUserBasicInfoRepository(UserBasicInfoService)
    }


}