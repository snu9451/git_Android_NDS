package com.example.android_nds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    val TAG = "mymymy"

    // 지도 선언
    private lateinit var mMap: GoogleMap


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // =============================== [[ 지도 구현 시작]] ===============================
        // 지도가 담길 MapView 가져오기
        val mapView = view.findViewById<MapView>(R.id.map)
        // 아래 두 코드가 없으면 뷰에 지도가 안 뜬다.
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(this)
        // =============================== [[ 지도 구현 끝]] ===============================
    }


    // 지도가 준비되었을 때 - 콜백메소드
    override fun onMapReady(googleMap: GoogleMap) {
        Log.i(TAG, "onMapReady 호출 성공")
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(37.478665, 126.878204)
        mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.setMinZoomPreference(15.0f)
    }
}