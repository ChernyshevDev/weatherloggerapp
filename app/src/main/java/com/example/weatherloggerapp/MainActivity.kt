package com.example.weatherloggerapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

lateinit var permissions: Array<String>

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.require_permissions_screen)

        permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET
        )

        if (checkPermissions()) goToMainScreen()
        else requestPermissions()
    }

    private fun goToMainScreen() {
        setContentView(R.layout.nav_container)
    }

    private fun checkPermissions(): Boolean {
        for (permission in permissions) {
            if (!havePermission(permission)) return false
        }
        return true
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            permissions,
            123
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (checkPermissions()) goToMainScreen()
        else requestPermissions()
    }

    private fun havePermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

}
