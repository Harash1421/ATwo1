package com.example.atwo1

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        }else{
            findLocation()
        }
        buOpenMaps.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] ==
                    PackageManager.PERMISSION_GRANTED){
                findLocation()
            }
        }
    }
   @SuppressLint("MissingPermission")
   private fun findLocation(){
       var lManger = getSystemService(LOCATION_SERVICE) as LocationManager
       var lListener = object : LocationListener{
           override fun onLocationChanged(p0: Location) {
               var geoCoder = Geocoder(applicationContext)
               var list = geoCoder.getFromLocation(p0.latitude, p0.longitude, 1)
               var a:String = ""
               if(list.size > 0){
                   a += list[0].countryName + "\n"
                   a += list[0].locality
               }
               buFindLocation.setOnClickListener {
                   tvMyLocation.text = a.toString()
               }
           }
       }
       lManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0f, lListener)
   }
}