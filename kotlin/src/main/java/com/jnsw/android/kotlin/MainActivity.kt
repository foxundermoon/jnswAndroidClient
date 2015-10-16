package com.jnsw.android.kotlin

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import com.google.common.eventbus.Subscribe
import com.jnsw.core.CustomApplication
import org.jetbrains.anko.*
import com.jnsw.core.event.DownloadEvent
import com.jnsw.core.data.FileMessage
import com.jnsw.core.event.UploadEvent
import com.jnsw.core.event.UploadedEvent
import com.jnsw.core.util.L
import org.apache.http.client.methods.HttpPutHC4
import org.apache.http.entity.ContentType
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.lang.Throwable
import java.net.URI
import java.net.URL
import java.util.Random

public class MainActivity : Activity() {
    val MESSAGE = ""
    val textViewTag = "information"
    var textView: EditText? = null
    private var TAG: String = ""
    var info: Int = -1

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = getLocalClassName()
        setContentView(R.layout.activity_main)
    }
}
//        //        findViewById(R.id.)
//        val handler = Handler()
//        var pic: ByteArray
//        var img: ImageView
//        verticalLayout {
//            padding = dip(16)
//            scrollY
//            scrollX
//            val textView = editText("result :") {
//                textSize = 18f
//                setTag(textViewTag)
//                setId(Random().nextInt())
//            }
//            info = textView.getId()
//            val draw = getResources().getDrawable(R.drawable.abc_ic_voice_search_api_mtrl_alpha)
//            Thread(Runnable {
//                runOnUiThread {
//                    img.setImageDrawable(draw)
//                }
//            }).start()
//            val app = CustomApplication.getInstance()
//            button("dowmload bytes", {
//                textSize = 20f
//                onClick {
//                    textView.append("clicked")
//
//                    Thread(Runnable({
//                        try {
//                            val result = app.httpClient.downloadBitmap("http://10.80.5.222/img/girl_2.jpg")
//                            pic = app.httpClient.downloadBytes("http://10.80.5.222/img/girl_2.jpg")
//                            runOnUiThread {
//                                textView.append("\n get result\n result: $result \n")
//                                textView.append("\n result length: ${result.getByteCount()} \n")
//                            }
//                            if (result != null) {
//                                runOnUiThread { textView.append("\n length :${result.getByteCount()}\n") }
//                                runOnUiThread { img.setImageBitmap(result) }
//                            } else {
//                                runOnUiThread { textView.setText("download faild") }
//                            }
//                        } catch(e: Exception) {
//                            runOnUiThread {
//                                textView.append(
//                                        "\n Exception: ${e.getMessage()} \n cause: ${e.getCause()} "
//                                )
//                            }
//                        }
//                    })).start()
//                }
//            })
//            button("upload", {
//                onClick {
//                    Log.e(TAG,"clicked")
//                    try {
//                        val cache = img.getDrawingCache()
//                        var msg = ""
//                        Thread(Runnable {
//                            try {
//                                val ops = ByteArrayOutputStream()
//                                //                                if (cache == null) {
//                                ////                                    msg += "cache is null!\n"
//                                //                                    val result = app.httpClient.upload("http://10.80.5.222/", pic)
//                                ////                                    msg += "upload bytes:$result"
//                                //                                } else {
//                                //                                    cache.compress(Bitmap.CompressFormat.PNG, 70, ops)
//                                val hc = app.httpClient.getHttpClient()
//                                val put = HttpPutHC4(URI("http://10.80.5.222:8080/"))
////                                put.addHeader("Host", "10.80.5.222:8080")
//                                val mb = app.httpClient.getDefaultmultipartEntityBuilder()
//                                mb.addBinaryBody("file", pic, ContentType.MULTIPART_FORM_DATA,"t")
//                                put.setEntity(mb.build())
//                                msg += "\n  ${put.toString() } \n"
//                                val rsp = hc.execute(put)
////                                for ( h in put.headerIterator()) {
////                                    msg += "\n ${h.toString()}"
////                                }
////                                //                                    val result = app.httpClient.upload("http://10.80.5.222/", ops.toByteArray())
////                                msg += "\nresult:${EntityUtilsHC4.toString(rsp.getEntity())}"
////                                //                                }
////                                runOnUiThread { textView.append(msg) }
//                            } catch(e: kotlin.Throwable) {
//                                Log.e(TAG,TAG);
////                                handler.post(Runnable { textView?.append(e?.getMessage()) })
////                                    Log.e(TAG,"exception is null")
////                                    Log.e(TAG,e.getMessage())
////                                    Log.e(TAG,msg)
//                                Log.e(TAG,e.toString())
//                            }
//                        }).start()
//                    } catch(e: Exception) {
//                        handler.post(Runnable { textView.append("exception :${e.getMessage()}")  })
//                    }
//                }
//            })
//            var file = FileMessage()
//            file.setId(1)
//            button("download",{
//                onClick {
//                    app.eventBus.post(DownloadEvent(file))
//                    runOnUiThread {
//                        textView?.append("download.....")
//                    }
//                }
//            })
//            img = imageView()
//        }/*.layoutParams { topMargin = dip(8) }*/
//    }
////    @Subscribe public  fun onUpload (event: UploadedEvent) {
////        var file = event.getEventData()
////        if(file.isUploaded()){
////            runOnUiThread {
////                L.e(TAG,"upload success,the file md5 is ${file.getMd5()}")
////                L.e(TAG,"textView ID :$info")
////                L.e( "findViewById(info):"+findViewById(info))
////                textView?.append("upload success,the file md5 is ${file.getMd5()}")
////            }
////        }else{
////            runOnUiThread {
////                textView?.append("upload failed:${file.getErrorMessage()}")
////            }
////        }
////
////
////    }
//
////    @Subscribe public  fun onDownload(e:DownloadEvent){
////        var file = e.getEventData()
////        if(file.isDownloaded()){
////            runOnUiThread {
////                textView?.append("download success! name:${file.getFileName()} md5:${file.getMd5()}")
////            }
////            file.setFileName(file.getFileName() +".new")
////            CustomApplication.getInstance().eventBus.post(UploadEvent(file))
////
////        }else{
////            runOnUiThread {
////                textView?.append("download failed :${file.getErrorMessage()}")
////            }
////        }
////
////    }
//}
