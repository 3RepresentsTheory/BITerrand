package com.example.biterrand_fix.data

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.example.biterrand_fix.BITerrandApplication
import com.example.biterrand_fix.getAppContext
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
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.coroutineContext

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
        demand.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        var imagePart: MultipartBody.Part? = null
        if(demand.imageUrl!=null){
//            val imageFile = File(
//                demand.imageUrl!!
//            )
            val imageFile = fileFromContentUri(getAppContext(),demand.imageUrl!!.toUri())
            /*TODO 只支持一个图片，格式固定，后续可改*/
            val imageRequestBody = imageFile.asRequestBody(
                ("image/"+imageFile.extension).toMediaTypeOrNull()
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

fun fileFromContentUri(context: Context, contentUri: Uri): File {
    // Preparing Temp file name
    val fileExtension = getFileExtension(context, contentUri)
    val fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""

    // Creating Temp file
    val tempFile = File(context.cacheDir, fileName)
    tempFile.createNewFile()

    try {
        val oStream = FileOutputStream(tempFile)
        val inputStream = context.contentResolver.openInputStream(contentUri)

        inputStream?.let {
            copy(inputStream, oStream)
        }

        oStream.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return tempFile
}

private fun getFileExtension(context: Context, uri: Uri): String? {
    val fileType: String? = context.contentResolver.getType(uri)
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
}

@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
    val buf = ByteArray(8192)
    var length: Int
    while (source.read(buf).also { length = it } > 0) {
        target.write(buf, 0, length)
    }
}