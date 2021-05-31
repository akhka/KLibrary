package com.development.file_picker

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import com.development.file_picker.Constants.PICK_IMAGE_REQUEST
import com.development.file_picker.Constants.PICK_PDF_REQUEST
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

object OnActivityResultAction {

    fun getInfo(context: Context, resultCode: Int, requestCode: Int, data: Intent, pdfPicker: PdfPicker){
        if (resultCode== AppCompatActivity.RESULT_OK && data.data!=null){
            when(requestCode){
                PICK_PDF_REQUEST -> {
                    /*data.data!!.let {
                        pdfPicker.uri = it
                        context.contentResolver.openInputStream(it)?.use { inputStream ->
                            val name = getFileName(context, it)
                            pdfPicker.fileName = name
                            val tempFile = createTempFile(context, name.split(".")[0], "pdf")
                            copyStreamToFile(inputStream, tempFile)

                            val filePath = tempFile.absolutePath
                            pdfPicker.path = filePath
                            pdfPicker.tempFile = tempFile
                        }
                    }*/

                    data.data!!.let {
                        val fileObject = FileObject()
                        fileObject.uri = it
                        context.contentResolver.openInputStream(it)?.use { inputStream ->
                            val name = getFileName(context, it)
                            fileObject.fileName = name
                            val tempFile = createTempFile(context, name.split(".")[0], "pdf")
                            copyStreamToFile(inputStream, tempFile)

                            val filePath = tempFile.absolutePath
                            fileObject.path = filePath
                            fileObject.tempFile = tempFile
                        }
                        pdfPicker.filesList.add(fileObject)
                    }

                }
                PICK_IMAGE_REQUEST -> {
                    /*data.data!!.let {
                        contentResolver.openInputStream(it)?.use { inputStream ->
                            val tempFile = createTempFile(this, "temp", "jpeg")
                            copyStreamToFile(inputStream, tempFile)

                            val filePath = tempFile.absolutePath

                            println("*******************************    $filePath    *******************************")
                            val bitmapOpt = BitmapFactory.Options()
                            var bitmap = BitmapFactory.decodeFile(filePath, bitmapOpt)
                            bitmap = Bitmap.createScaledBitmap(bitmap, image.width, image.height, true)
                            image.setImageBitmap(bitmap)
                        }
                    }*/
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createTempFile(context: Context, fileName: String, extension: String) : File {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        return File(storageDir, "$fileName-$currentDate-${System.currentTimeMillis()}.$extension")
    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File){
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4*1024)
                while (true){
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    }

    private fun getFileName(context: Context, uri: Uri) : String{
        val f = DocumentFile.fromSingleUri(context, uri)
        return f?.name!!
    }

}