package cr.ac.una.controlarterial.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.ac.una.controlarterial.AuthInterceptor
import cr.ac.una.controlarterial.DAO.TomaArterialDAO
import cr.ac.una.controlarterial.entity.TomaArterial
import cr.ac.una.controlarterial.entity.TomasArteriales
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TomaArterialViewModel: ViewModel() {
    private var _tomasArteriales: MutableLiveData<List<TomaArterial>> = MutableLiveData()
    var tomasArteriales : LiveData<List<TomaArterial>> = _tomasArteriales
    private lateinit var apiService : TomaArterialDAO

    suspend fun deleteItem(uuid:String?){
        init()
        if (uuid != null) {
            apiService.deleteItem(uuid)
        }
    }

    suspend fun getItems(){
        init()
        val list = apiService.getItems()
        _tomasArteriales.postValue(list.items)
    }

    suspend fun addItem(distolica : Int, sistolica : Int, ritmo : Int){
        init()
        var item = TomaArterial(_uuid = null, distolica = distolica, sistolica = sistolica, ritmo = ritmo)
        var items = ArrayList<TomaArterial>()
        items.add(item)
        apiService.createItem(items)
    }

    fun init() {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor("WuwB3r4rJZ57hFwOmBTTip5tYaXvlMfQmST0bR5Edo54z5IJ3w"))
            // .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://crudapi.co.uk/api/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(TomaArterialDAO::class.java)
    }

}