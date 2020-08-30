package com.dicoding.kotlin.mybackgroundthread

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), MyAsyncCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val demoAsync = DemoAsync(this)
        demoAsync.execute(INPUT_STRING)
    }

    override fun onPreExecute() {
        tv_status.setText(R.string.status_pre)
        tv_desc.text = INPUT_STRING
    }

    override fun onPostExecute(result: String) {
        tv_status.setText(R.string.status_post)
        tv_desc.text = result
    }

    companion object {
        private const val INPUT_STRING = "Halo ini demo AsyncTask!!"
    }

    private class DemoAsync(myListener: MyAsyncCallback): AsyncTask<String, Void, String>() {

        private val myListener: WeakReference<MyAsyncCallback>

        init {
            this.myListener = WeakReference(myListener)
        }

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d(LOG_ASYNC, "status: onPreExecute")

            val myListener = myListener.get()
            myListener?.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {
            Log.d(LOG_ASYNC, "status: doInBackground")

            var output: String? = null

            try {
                val input = params[0]
                output = "$input Selamat Belajar!!"
                Thread.sleep(2000)
            } catch (e: Exception) {
                Log.d(LOG_ASYNC, e.message.toString())
            }
            return output.toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.d(LOG_ASYNC, "status: onPostExecute")

            val myListener = this.myListener.get()
            myListener?.onPostExecute(result.toString())
        }

        companion object {
            private val LOG_ASYNC = "DemoAsync"
        }

    }
}

internal interface MyAsyncCallback {
    fun onPreExecute()
    fun onPostExecute(text: String)
}