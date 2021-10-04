package com.example.atwo1

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.atwo1.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        if (Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION),2)
        }else{
            findLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun findLocation(){
        var lManger = getSystemService(LOCATION_SERVICE) as LocationManager
        var lListener = object : LocationListener{
            override fun onLocationChanged(p0: Location) {
                val a = LatLng(p0.latitude, p0.longitude)
                mMap.addMarker(MarkerOptions().position(a).title(a.toString()))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(a))
            }
        }
        lManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0f, lListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 2){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] ==
                PackageManager.PERMISSION_GRANTED){
                findLocation()
            }
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        mMap.setOnMapClickListener {
            var geocoder = Geocoder(this)
            var list = geocoder.getFromLocation(it.latitude, it.longitude, 1)
            var a:String = ""
            if (list.size > 0){
                a += list[0].countryName + "\n"
                a += list[0].locality + "\n"
                a += list[0].subLocality
            }
            Toast.makeText(this, a.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}