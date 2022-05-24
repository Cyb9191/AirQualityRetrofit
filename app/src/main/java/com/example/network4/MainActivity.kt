package com.example.network4
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import okhttp3.Interceptor
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.http.Query
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Path
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import retrofit2.Response
//import com.example.network4.MainActivity.RecipesService
import androidx.navigation.findNavController
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.widget.TextView




import com.example.network4.*
import com.example.network4.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class RepoResult() : ArrayList<JvmType.Object>()
interface RecipesService {
    @GET("current/airquality")
    suspend fun getAirQuality(
        @Query("lat") recipe: String,
        @Query("lon") recipeb: String,
    ): Repo
}



const val E_BASE_URL = "https://air-quality.p.rapidapi.com/"
const val API_ID = "YourApiID"
const val API_KEY = "19e7b8759amshe6b93bd55c131c7p120076jsn06452ac17b99"
const val RAPIDAPI_KEY= "06a7614749msh19cff85cd7e9993p11616djsnda0c69ab3a46"

//Here you add your url interceptor
//"app_id" and "app_key" might be different, depending on your API


//FINALLY build your retrofit



//You can now declare your interfaces with your REST methods as usual, for example GET which will return your object


data class items(
    val mold_level: Int,
    val aqi: Int,
    val pm10: Number,
    val co:Number,
    val o3:Number,
    val predominant_pollen_type:String,
    val so2:Int,
    val pollen_level_tree: Int,
    val pollen_level_weed:Int,
    val no2: Number,
    val pm25: Number,
    val pollen_level_grass:Int
    )



data class Repo(
    val city_name: String,
    val lon: Number,
    val timezone:String,
    val lat:Number,
    val country_code:String,
    val state_code:String,
    val data: Array<items>


)





class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(R.layout.activity_main)
        retrieveRepos()




    }




//.addQueryParameter("app_id", API_ID)
//Finally you create your object (Singleton in Java) which generates your service via lazy delegate
    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request()
            val headers = request.headers
                .newBuilder()
                .add("X-RapidAPI-Key", RAPIDAPI_KEY)
                .build()

            val requestWithHeaders = request.newBuilder().headers(headers).build()
            return chain.proceed(requestWithHeaders)


    }}


    //Add the logger interceptor optional:
    val logger = HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BASIC) }

    //Build your OkHttpClient - here you add the api_interceptor and logger
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://air-quality.p.rapidapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val recipesService = retrofit.create(RecipesService::class.java)

    fun retrieveRepos() {
            val progress = findViewById<ContentLoadingProgressBar>(R.id.repo_loading_indicator)

            lifecycleScope.launch {
                try {
                    progress.show()
                    val repos: Repo = recipesService.getAirQuality("40.71427", "-73.00597")
                    progress.hide()
                    showRepos(repos)
                } catch (e: Exception) {
                    Log.e("MainActivity", "error retrieving repos: $e")
                    progress.hide()
                    Snackbar.make(
                        findViewById(R.id.main_view),
                        "Error retrieving repos",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("Retry") { retrieveRepos() }.show()
                }
    }}

    fun showRepos(repoResults: Repo) {
        Log.d("MainActivity", "list of repos received, size: $repoResults")
        val firsttext=findViewById(R.id.textview_first) as TextView
        firsttext.text= repoResults.city_name + "\n\n" + repoResults.lon.toString()+
                "\n\n"+repoResults.timezone+"\n\n"+repoResults.lat.toString()+"\n\n"+
                repoResults.country_code+"\n\n"+repoResults.state_code+"\n\n"+repoResults.data[0].mold_level+
                "\n\n"+ repoResults.data[0].aqi+"\n\n"+repoResults.data[0].pm10+"\n\n"+repoResults.data[0].co+
                "\n\n"+ repoResults.data[0].o3+"\n\n"+repoResults.data[0].predominant_pollen_type+
                "\n\n"+repoResults.data[0].so2+"\n\n"+repoResults.data[0].pollen_level_tree+"\n\n"+
                repoResults.data[0].pollen_level_weed+"\n\n"+repoResults.data[0].no2+
                "\n\n"+repoResults.data[0].pm25+"\n\n"+repoResults.data[0].pollen_level_grass



        /*{val list = findViewById<RecyclerView>(R.id.repo_list)
            list.visibility = View.VISIBLE
            val adapter = RepoAdapter(repoResults)
            list.adapter = adapter
            list.layoutManager = LinearLayoutManager(applicationContext)
            */
    }


    //Build your json converter - in this example MOSHI
    //val moshi = Moshi.Builder()
        //.add(KotlinJsonAdapterFactory())
        //.build()


    //FINALLY build your retrofit




    //You can now declare your interfaces with your REST methods as usual, for example GET which will return your object








    //Finally you create your object (Singleton in Java) which generates your service via lazy delegate






    }

