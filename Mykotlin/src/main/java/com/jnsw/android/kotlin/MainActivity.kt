package com.jnsw.android.kotlin

import android.app.Activity
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.jnsw.android.kotlin.R;
import com.jnsw.core.CustomApplication
import org.jetbrains.anko.*
import com.jnsw.core.data;
import com.jnsw.core.data.Message
import org.apache.http.client.methods.HttpPutHC4
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.Header
import org.apache.http.util.EntityUtilsHC4
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.lang.Throwable
import java.net.URI
import java.net.URL

public class MainActivity : Activity() {
    val MESSAGE = ""

    private var TAG: String = ""

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = getLocalClassName()
        setContentView(R.layout.activity_main)
        //        findViewById(R.id.)
        val handler = Handler()
        var pic: ByteArray
        var img: ImageView
        verticalLayout {
            padding = dip(16)
            scrollY
            scrollX

            val tex = editText("result :") {
                textSize = 18f
            }.layoutParams { verticalMargin = dip(4) }
            val draw = getResources().getDrawable(R.drawable.ic_launcher)
            Thread(Runnable {
                runOnUiThread {
                    img.setImageDrawable(draw)
                }
            }).start()
            val app = CustomApplication.getInstance()
            button("dowmload bytes", {
                textSize = 20f
                onClick {
                    tex.append("clicked")

                    Thread(Runnable({
                        try {
                            val result = app.httpClient.downloadBitmap("http://10.80.5.222/img/girl_2.jpg")
                            pic = app.httpClient.downloadBytes("http://10.80.5.222/img/girl_2.jpg")
                            runOnUiThread {
                                tex.append("\n get result\n result: $result \n")
                                tex.append("\n result length: ${result.getByteCount()} \n")
                            }
                            if (result != null) {
                                runOnUiThread { tex.append("\n length :${result.getByteCount()}\n") }
                                runOnUiThread { img.setImageBitmap(result) }
                            } else {
                                runOnUiThread { tex.setText("download faild") }
                            }
                        } catch(e: Exception) {
                            runOnUiThread {
                                tex.append(
                                        "\n Exception: ${e.getMessage()} \n cause: ${e.getCause()} "
                                )
                            }
                        }
                    })).start()
                }
            })
            button("upload", {
                onClick {
                    Log.e(TAG,"clicked")
                    try {
                        val cache = img.getDrawingCache()
                        var msg = ""
                        Thread(Runnable {
                            try {
                                val ops = ByteArrayOutputStream()
                                //                                if (cache == null) {
                                ////                                    msg += "cache is null!\n"
                                //                                    val result = app.httpClient.upload("http://10.80.5.222/", pic)
                                ////                                    msg += "upload bytes:$result"
                                //                                } else {
                                //                                    cache.compress(Bitmap.CompressFormat.PNG, 70, ops)
                                val hc = app.httpClient.getHttpClient()
                                val put = HttpPutHC4(URI("http://10.80.5.222:8080/"))
//                                put.addHeader("Host", "10.80.5.222:8080")
                                val mb = app.httpClient.getDefaultmultipartEntityBuilder()
                                mb.addBinaryBody("file", pic, ContentType.MULTIPART_FORM_DATA,"t")
                                put.setEntity(mb.build())
                                msg += "\n  ${put.toString() } \n"
                                val rsp = hc.execute(put)
                                for ( h in put.headerIterator()) {
                                    msg += "\n ${h.toString()}"
                                }
                                //                                    val result = app.httpClient.upload("http://10.80.5.222/", ops.toByteArray())
                                msg += "\nresult:${EntityUtilsHC4.toString(rsp.getEntity())}"
                                //                                }
                                runOnUiThread { tex.append(msg) }
                            } catch(e: kotlin.Throwable) {
                                Log.e(TAG,TAG);
//                                handler.post(Runnable { tex?.append(e?.getMessage()) })
//                                    Log.e(TAG,"exception is null")
//                                    Log.e(TAG,e.getMessage())
//                                    Log.e(TAG,msg)
                                Log.e(TAG,e.toString())
                            }
                        }).start()
                    } catch(e: Exception) {
                        handler.post(Runnable { tex.append("exception :${e.getMessage()}")  })
                    }
                }
            })
            img = imageView()
        }/*.layoutParams { topMargin = dip(8) }*/
    }
}
