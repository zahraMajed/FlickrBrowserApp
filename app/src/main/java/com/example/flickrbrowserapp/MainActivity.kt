package com.example.flickrbrowserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_view.view.*
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var etSearch:EditText
    lateinit var btnSearch:Button
    lateinit var photoList:ArrayList<photo>
    lateinit var imgView:ImageView
    lateinit var Layout:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        photoList= arrayListOf()
        rv_main.adapter=RecyclerAdapter(this,photoList)
        rv_main.layoutManager=LinearLayoutManager(this)

        etSearch=findViewById(R.id.edSearch)
        btnSearch=findViewById(R.id.btnSearch)
        imgView=findViewById(R.id.imgViewOpen)
        Layout=findViewById(R.id.LL1)

        btnSearch.setOnClickListener(){
            if (etSearch.text.isNotEmpty()){
                requestAPI()
            }else
                Toast.makeText(this, "Search field is empty", Toast.LENGTH_LONG).show()
        }
        imgView.setOnClickListener { closeImg() }
    }//end onCreate()

    fun requestAPI(){
        CoroutineScope(Dispatchers.IO).launch {
            val data= async {
                getPhoto()
            }.await()

            if (data.isNotEmpty()){
                displayPhoto(data)
            }else{
                Toast.makeText(this@MainActivity, "No Images Found", Toast.LENGTH_LONG).show()
            }
        }
    }
    ////////////
    fun getPhoto():String{
        var response=""
        try {
            //get it form API documintaion
            response= URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&per_page=10&api_key=b73090cc5f2a93a0ee864469e1b2654c&tags=${etSearch.text}&format=json&nojsoncallback=1")
                .readText(Charsets.UTF_8)
        }catch (e:Exception){
            println("Error $e")
        }
        return response
    }
    ////////////
    suspend fun displayPhoto(data:String) {

        withContext(Dispatchers.Main){

            val jsonObj = JSONObject(data)
            val photos = jsonObj.getJSONObject("photos").getJSONArray("photo")
            //
            for (i in 0 until photos.length()){
                val title = photos.getJSONObject(i).getString("title")
                //these variables helps in build photo link
                val farmID = photos.getJSONObject(i).getString("farm")
                val serverID = photos.getJSONObject(i).getString("server")
                val id = photos.getJSONObject(i).getString("id")
                val secret = photos.getJSONObject(i).getString("secret")
                //use previous variables to get photo link
                //get it form API documintaion
                val photoLink = "https://farm$farmID.staticflickr.com/$serverID/${id}_$secret.jpg"
                photoList.add(photo(title,photoLink))
            }
            rv_main.adapter?.notifyDataSetChanged()
        }
    }
    ///////////
    fun openImg(link:String){
        Glide.with(this).load(link).into(imgViewOpen)
        imgViewOpen.isVisible=true
        LL1.isVisible=false
        rv_main.isVisible=false
    }

    fun closeImg(){
        imgViewOpen.isVisible=false
        LL1.isVisible=true
        rv_main.isVisible=true
    }

}//end class