package com.development.file_picker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import androidx.core.app.ActivityCompat
import com.development.file_picker.Constants.PICK_PDF_REQUEST
import java.io.File

class PdfPicker {

    var uri: Uri? = null
    var path: String? = null
    var fileName: String? = null
    var tempFile: File? = null

    val permissionRequired = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION
    )

    fun checkPermissions(context: Context, activity: Activity){

        if (!askPermission(context, permissionRequired)){
            ActivityCompat.requestPermissions(
                activity,
                permissionRequired,
                PackageManager.PERMISSION_GRANTED
            )
        }

    }

    private fun askPermission(context: Context, permissions: Array<String>) : Boolean{

        for (p in permissions){
            if (ActivityCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }

        return true
    }


    fun setPdfPickerListener(activity: Activity, view: View){
        view.setOnClickListener {
            val intent = Intent()
            intent.apply {
                type = "application/pdf"
                action = Intent.ACTION_OPEN_DOCUMENT
                addCategory(Intent.CATEGORY_OPENABLE)
                flags = flags or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            activity.startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST)
        }
    }

}