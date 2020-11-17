/*
파일명: MainActivity.java
개발자 이름: 백연선, 이한정
 */
package com.example.kooktaxi;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.kooktaxi.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private Marker currentMarker = null;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  //1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; //0.5초
    private static final int PERMISSIONS_REQUEST_CODE = 100; //onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됨.
    boolean needRequest = false; //위와 동일

    String[] REQUIRED_PERMISSIONS  = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };  //앱을 실행하기 위해 필요한 퍼미션을 정의

    Location mCurrentLocatiion;
    LatLng currentPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;
    private View mLayout;

    private String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        Intent intent = getIntent();
        mail = intent.getStringExtra("mail");

        mLayout = findViewById(R.id.layout_main);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);  //레이아웃의 프래그먼트의 핸들을 가져옴
        mapFragment.getMapAsync(this);  //호출되면 onMapReady 콜백이 실행됨

//        // 버튼 페이지 연결
//        Button button = (Button) findViewById(R.id.btn_gil);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
//                intent.putExtra("mail", mail);
//                startActivity(intent);
//            }
//        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {  //map이 사용할 준비가 되었을 때(GoogleMap 객체를 파라미터로 제공할 수 있을 때) 호출되는 메소드
        mMap = googleMap;
        setDefaultLocation();  //초기위치 서울로 이동

        // 위치 퍼미션을 가지고 있는지 확인
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {  //퍼미션을 가지고 있으면
            startLocationUpdates();  //위치 업데이트 시작
        } else {  //퍼미션 요청을 허용한 적 없다면
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {  //사용자가 퍼미션을 거부한 적 있는 경우에는
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {  //snackbar(이유를 보여주고, 사용자가 확인을 클릭을 해야 사라짐)로 허용을 요청함. 요청 결과는 onRequestPermissionResult에서 수신.
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions( MainActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
            } else {  //사용자가 퍼미션 거부를 한 적 없으면
                ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);  //바로 퍼미션 요청을 함
            }
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //좌표 객체 생성(우선 4개의 역 좌표 객체를 생성함)
        LatLng Gwanghwamun = new LatLng(37.5707456,126.973708); //(위도, 경도)
        LatLng Hongdae = new LatLng(37.557527,126.9222782);
        LatLng Gileum = new LatLng(37.6086541,127.0136683);
        LatLng DDP = new LatLng(37.5644,127.0055713);

        MarkerOptions[] markerOptions = new MarkerOptions[4];  //마커옵션 배열 생성. 각 마커의 위치와 타이틀을 설정해줌.
        markerOptions[0] = new MarkerOptions()
                .position(Gwanghwamun)
                .title("광화문역");
        markerOptions[1] = new MarkerOptions()
                .position(Hongdae)
                .title("홍대입구역");
        markerOptions[2] = new MarkerOptions()
                .position(Gileum)
                .title("길음역");
        markerOptions[3] = new MarkerOptions()
                .position(DDP)
                .title("동대문역사문화공원역");

        for(int i=0; i<4; i++){
            mMap.addMarker(markerOptions[i]);  //addMarker()를 통해 GoogleMap객체(mMap)에 추가하면 지도에 표시된다.
        }

        mMap.setOnMarkerClickListener(this);
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            super.onLocationResult(locationResult);
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude()) + " 경도:" + String.valueOf(location.getLongitude());
                //setCurrentLocation(location, markerTitle, markerSnippet);
                mCurrentLocatiion = location;
            }
        }
    };



    private void startLocationUpdates() {
        if (!checkLocationServicesStatus()) {  //위치설정이 되지 않았으면
            showDialogForLocationServiceSetting();  //위치설정을 한다
        } else {
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||  hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            if (checkPermission())
                mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkPermission()) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            if (mMap!=null)
                mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public String getCurrentAddress(LatLng latlng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude,1);
        } catch (IOException ioException) {
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    public boolean checkLocationServicesStatus() { //locationManager를 통해 GPS와 네트워크가 사용 가능한지  체크해주는 함수
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        if (currentMarker != null) currentMarker.remove(); //마커가 존재하면 마커를 지운다
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude()); //현재 위치

        MarkerOptions markerOptions = new MarkerOptions(); //마커 옵션 설정
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        currentMarker = mMap.addMarker(markerOptions); //해당 마커 추가

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng); //카메라를 현재 위치로 이동
        mMap.moveCamera(cameraUpdate);
    }

    public void setDefaultLocation() {  //Default MarkerOptions를 설정해주는 함수(위치정보 제공이 허용되어 있지 않은 경우)
        LatLng DEFAULT_LOCATION = new LatLng(37.6119, 126.9955);  //첫 위치를 서울(국민대 부근)으로 설정
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";

        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);  //두번째 인자(15)는 카메라 줌의 정도를 나타냄
        mMap.moveCamera(cameraUpdate);
    }

    //런타임 퍼미션 처리 메소드들
    private boolean checkPermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) { //ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드.
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) { //요청 코드가 PERMISSIONS_REQUEST_CODE이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            for (int result : grandResults) { //모든 퍼미션을 허용했는지 체크한다.
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            if (check_result) { //모든 퍼미션을 허용했으면
                startLocationUpdates(); //위치 업데이트를 시작한다
            }
            else { //거부한 퍼미션이 있다면
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) //REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) { //사용자가 거부만 선택한 경우
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() { //앱을 다시 실행해서 허용을 선택하면 된다는 Snackbar를 띄운다
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                }else { //"다시 묻지 않음"을 선택한 경우
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", //설정에서 직접 퍼미션을 허용해야 한다는 Snackbar를 띄운다.
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                }
            }
        }
    }

    //GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //AlertDialog는일부 화면만 가리는 윈도우. 정보 입력받을 수 있음
        builder.setTitle("위치 서비스 비활성화"); //Dialog 제목
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다."); //메세지
        builder.setCancelable(true); //배경이나 뒤로가기 키 누르면 취소 가능
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() { //positive button을 설정버튼으로 설정
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() { //negative button을 취소 버튼으로 설정
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                if (checkLocationServicesStatus()) { //사용자가 GPS 활성 시켰는지 검사
                    needRequest = true;
                    return;
                }
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getTitle().equals("길음역")){
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("mail", mail);
            intent.putExtra("station", "길음역");
            startActivity(intent);
        }
        if(marker.getTitle().equals("홍대입구역")){
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("mail", mail);
            intent.putExtra("station", "홍대입구역");
            startActivity(intent);
        }
        if(marker.getTitle().equals("광화문역")){
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("mail", mail);
            intent.putExtra("station","광화문역");
            startActivity(intent);
        }
        if(marker.getTitle().equals("동대문역사문화공원역")){
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("mail", mail);
            intent.putExtra("station", "동대문역사문화공원역");
            startActivity(intent);
        }

        return true;
    }
}