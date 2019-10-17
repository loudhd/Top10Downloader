package com.example.top10downloader

import android.content.Context
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import kotlin.properties.Delegates

class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageURL: String = ""

    override fun toString(): String {
        return """
            name = $name
            artist = $artist
            releaseDate = $releaseDate
            imageURL = $imageURL
        """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    private  val downloadData = DownloadData(this, xmlListView)

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        downloadUrl("http://ax.itumes.appie.com/WebObjects/M25toreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG, "onCreate: done")
    }
    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    private fun downloadUrl(feedURL: String){
        Log.d(TAG,"downlaodUrl starting AsyncTask")
        downloadData.execute(feedURL)
        Log.d(TAG,"downloadUrl done")
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val feedUrl: String
        when(item.itemId){
            R.id.mnuFree->
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml"
            R.id.mnuPaid->
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=10/xml"
            R.id.mnuSongs->
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=10/xml"
            else ->
                return super.onOptionsItemSelected(item)
        }
        downloadData(feedUrl)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    override fun onDestroy() {
        super.onDestroy()
        downloadData.cancel(true)
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.CUPCAKE)
        private class DownloadData(
            context: Context,
            ListView: ConstraintLayout
        ) : AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"
            var propContext:Context by Delegates.notNull()
            var propListView: ListView by Delegates.notNull()

            init {
                propContext = context
                propListView = ListView
            }

            @RequiresApi(Build.VERSION_CODES.CUPCAKE)
            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                val parseApplications = ParseApplications()
                parseApplications.parse(result)

                val feedAdapter = FeedAdapter(propContext, R.layout.list_item,parseApplications.applications)
                propListView.adapter = feedAdapter
            }

            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "doInBackground: starts with ${url[0]}")
                val rssFeed = downloadXML(url[0])
                if (rssFeed.isEmpty()) {
                    Log.d(TAG, "doInBackground:Eror downloading")
                }
                return rssFeed
            }

            private fun downloadXML(urlPath: String?): String {
            return URL(urlPath).readText()
            }
        }
    }
}







