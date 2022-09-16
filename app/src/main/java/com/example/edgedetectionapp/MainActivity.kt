package com.example.edgedetectionapp

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc


class MainActivity : AppCompatActivity() {
    private lateinit var detectEdgesImageView: ImageView
    private lateinit var imageView: ImageView
    lateinit var context: Context

    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                SUCCESS -> {
                    Log.i("OpenCV", "OpenCV loaded successfully")
                    val uri = Uri.parse(
                        ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                                resources.getResourcePackageName(R.drawable.image1) + '/' +
                                resources.getResourceTypeName(R.drawable.image1) + '/' +
                                resources.getResourceEntryName(R.drawable.image1)
                    )
                    try {
                        BitmapHelper.readBitmapFromPath(context, uri)?.let { detectEdges(it) }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detect_edges)
        context = this
        detectEdgesImageView = findViewById(R.id.detect_edges_image_view)
        imageView = findViewById(R.id.image_view)
    }

    private fun detectEdges(bitmap: Bitmap) {
        val rgba = Mat()
        Utils.bitmapToMat(bitmap, rgba)
        val edges = Mat(rgba.size(), CvType.CV_8UC1)
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4)
        Imgproc.Canny(edges, edges, 80.0, 100.0)
        BitmapHelper.showBitmap(bitmap, imageView)
        val resultBitmap: Bitmap =
            Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(edges, resultBitmap)
        BitmapHelper.showBitmap(resultBitmap, detectEdgesImageView)
    }
}

