package com.xan.nrcdemo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun isCameraGranted(context: Context?): Boolean {
    return ContextCompat.checkSelfPermission(
        context!!,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

@RequiresApi(api = Build.VERSION_CODES.M)
fun requestUserPermissions(
    o: Any?,
    permissionId: Int,
    permissions: Array<String>
) {
    if (o is Fragment) {
        o.requestPermissions(permissions, permissionId)
    } else if (o is Activity) {
        ActivityCompat.requestPermissions((o as AppCompatActivity?)!!, permissions, permissionId)
    }
}
