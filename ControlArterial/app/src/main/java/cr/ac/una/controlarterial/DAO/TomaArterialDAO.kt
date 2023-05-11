package cr.ac.una.controlarterial.DAO

import cr.ac.una.controlarterial.entity.TomaArterial
import cr.ac.una.controlarterial.entity.TomasArteriales
import retrofit2.http.*


interface TomaArterialDAO {



    @GET("tomas")
    suspend fun getItems(): TomasArteriales

    @GET("tomas/{uuid}")
    suspend fun getItem(@Path("uuid") uuid: String): TomaArterial

    @POST("tomas")
    suspend fun createItem( @Body items: List<TomaArterial>): TomasArteriales

    @PUT("tomas/{uuid}")
    suspend fun updateItem(@Path("uuid") uuid: String, @Body item: TomaArterial): TomaArterial

    @DELETE("tomas/{uuid}")
    suspend fun deleteItem(@Path("uuid") uuid: String)
}
