package com.example.top10downloader

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    private inner class DownLoadData : AsyncTask<String,Void,String>(){


    }

        

}
