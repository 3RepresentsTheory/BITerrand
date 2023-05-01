package com.example.biterrand_fix.data

import com.example.biterrand_fix.model.Demand
import com.example.biterrand_fix.model.MarsPhoto
import com.example.biterrand_fix.network.DemandsService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.jetbrains.annotations.TestOnly
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.Part
import java.io.File

/**
 *  The Repository for Demands
 */
interface DemandRepository {
    suspend fun getDemandsList():List<Demand>

    suspend fun getDemandsListAfterId(lastId:Long):List<Demand>

    suspend fun getDemandById(id:Long):Demand

    suspend fun uploadDemand(demand:Demand):Response<RequestBody>

    @TestOnly
//   use for test the sample data
    suspend fun getTestMarsPhotos():List<MarsPhoto>
}



/**
 * Network data source Repository, for uncoupling, we can use DI instead of hard
 * coding the ApiService in the NetworkRepository (Perhaps we use local data source/ different
 * network library)
 */
class NetworkDemandRepository(val demandApiService: DemandsService):DemandRepository{
    override suspend fun getDemandsList(): List<Demand> {
        return demandApiService.getLatestOrders()
    }

    override suspend fun getDemandsListAfterId(lastId: Long): List<Demand> {
        return demandApiService.getOrdersBelowLaterID(lastId)
    }

    override suspend fun getDemandById(id: Long): Demand {
        return demandApiService.getOrderById(id)
    }

    override suspend fun uploadDemand(demand: Demand): Response<RequestBody> {
        var imagePart: MultipartBody.Part? = null
        if(demand.imageUrl!=null){
            val imageFile = File(demand.imageUrl!!)
            /*TODO 只支持一个图片，格式固定，后续可改*/
            val imageRequestBody = imageFile.asRequestBody(
                "image/jpg".toMediaTypeOrNull()
            )
            imagePart = MultipartBody.Part.createFormData(
                "image",imageFile.name,imageRequestBody)
        }
        val demandbody = Gson().toJson(demand).toRequestBody(
            "application/json".toMediaTypeOrNull()
        )
        return demandApiService.uploadForm(demandbody,imagePart)
    }

    @TestOnly
    override suspend fun getTestMarsPhotos(): List<MarsPhoto> {
        return demandApiService.getTestPhotos()
    }
}

