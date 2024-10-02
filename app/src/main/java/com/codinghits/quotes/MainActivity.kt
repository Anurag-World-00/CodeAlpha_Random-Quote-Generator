package com.codinghits.quotes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {
    lateinit var textView : TextView
    lateinit var button : Button
    lateinit var share : Button
    val collectDataInSB = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textView = findViewById(R.id.textView)
        button = findViewById(R.id.button)
        share = findViewById(R.id.Share)

        button.setOnClickListener{
            retroData()
        }
        share.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,"$collectDataInSB")
            val chooser = Intent.createChooser(intent,"Share this quote using...")
            startActivity(chooser)
        }


    }
    private fun retroData(){


        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://zenquotes.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.geData()
        retrofitData.enqueue(object : Callback<List<QuoteData>?> {
            override fun onResponse(
                call: Call<List<QuoteData>?>,
                response: Response<List<QuoteData>?>
            ) {
                val productList = response.body()!!

                for(myData in productList){
                    collectDataInSB.replace(0,collectDataInSB.length,myData.q + " ")
                }
                textView.text = collectDataInSB

            }

            override fun onFailure(call: Call<List<QuoteData>?>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Failure${t.message}",Toast.LENGTH_LONG).show()
            }
        })

    }
}