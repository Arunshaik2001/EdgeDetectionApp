package com.example.edgedetectionapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import java.io.*

class BitmapHelper {
    companion object{

        fun showBitmap(bitmap: Bitmap, imageView: ImageView?) {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
            val data: ByteArray = baos.toByteArray()
            val bmp = BitmapFactory.decodeByteArray(data, 0, data.size)
            imageView!!.setImageBitmap(bmp)
        }

        @Throws(Exception::class)
        fun readBitmapFromPath(context: Context, path: Uri?): Bitmap? {
            val stream: InputStream? = path?.let { context.contentResolver.openInputStream(it) }
            val bitmap: Bitmap = BitmapFactory.decodeStream(stream)
            stream?.close()
            return bitmap
        }

        fun createFile(){
            val fileInputStream: FileInputStream?

            val file = File("yourfile")

            val byteArray = ByteArray(file.length().toInt())

            try {
                //convert file into array of bytes
                fileInputStream = FileInputStream(file)
                fileInputStream.read(byteArray)
                fileInputStream.close()

                //convert array of bytes into file
                val fileOuputStream = FileOutputStream("C:\\testing2.txt")
                fileOuputStream.write(byteArray)
                fileOuputStream.close()
                println("Done")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

    }
}