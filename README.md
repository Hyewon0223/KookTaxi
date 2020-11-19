# [모바일 프로그래밍] Project_KookTaxi
### 국민대 소프트웨어학부 
## 20191604백연선 / 20191650이한정 / 20191670조나영 / 20191686최혜원
### https://github.com/Hyewon0223/KookTaxi
---
### 설명
<div>
    <img src="https://user-images.githubusercontent.com/55418359/99452281-b1200200-2966-11eb-96ea-0192e7beb21c.jpg" width="180">
    <img src="https://user-images.githubusercontent.com/55418359/99452288-b2512f00-2966-11eb-923c-83b82fc37abf.jpg" width="180">
    <img src="https://user-images.githubusercontent.com/55418359/99452289-b2e9c580-2966-11eb-9136-fec9ec33d89a.jpg" width="180">
    <img src="https://user-images.githubusercontent.com/55418359/99452292-b3825c00-2966-11eb-9f01-fbe6168c44da.jpg" width="180">
    <img src="https://user-images.githubusercontent.com/55418359/99452294-b3825c00-2966-11eb-82ac-fda58a49c196.jpg" width="180">
</div>

- 목적지가 국민대인 사람들을 모아 택시를 함께 탈 수 있도록 하는 앱입니다.
- 동승하고 싶은 사람의 성별 여부, 인원수, 출발 지점(길음역, 광화문역, 홍대입구역, 동대문역사문화공원역) 등에 따라 사람들을 매칭해준다.
---
### 개발 환경
- Window OS
- Android Studio 4.0 (AVD: Pixel 2 API 30 / 안드로이드 폰)
---
### 사용 기술
- xml
- Java
- Firebase
<div>
    <img src="https://user-images.githubusercontent.com/55418359/99450379-0d355700-2964-11eb-9fe4-c4183249f090.JPG" width="200">
    <img src="https://user-images.githubusercontent.com/55418359/99450386-0e668400-2964-11eb-86d9-01762b75efb5.JPG" width="200">
</div>

- Google Cloud Platform(Maps SDK for Android API)
<div>
    <img src="https://user-images.githubusercontent.com/55418359/99349262-e41eb300-28de-11eb-8bd4-0cb2368d9b30.JPG" width="200">
    <img src="https://user-images.githubusercontent.com/55418359/99349264-e54fe000-28de-11eb-8448-44150c172a0d.JPG" width="200">
    <img src="https://user-images.githubusercontent.com/55418359/99349265-e5e87680-28de-11eb-80e5-358142a3bca6.JPG" width="200">
</div>

---
### 코드 관련 설명
#### manifests
##### AndroidManifest.xml
- 사용자의 위치 정보 접근을 위한 액세스 권한 
~~~xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
~~~
#### layout
##### activity_main.xml, activity_chat.xml
- Relative Layout(Main 화면), Frame Layout(Main 화면), Linear Layout(Chat 화면)
- map component 추가를 위한 Frame Layout 사용
~~~xml
<FrameLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/container">

    <fragment
        android:id="@+id/map"
        android:name="com.google.android.gms.maps.SupportMapFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MapsActivity" />

</FrameLayout>
~~~
#### Java
##### MainActivity.java, CharActivity.java
- 로그인의 조건이 모두 만족하면 Main 페이지로 이동 및 사용자의 정보(mail) 전달
~~~java
firebaseAuth.signInWithEmailAndPassword(mail,pw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("mail", mail);
            startActivity(intent);
        }
        ...
    }
});
~~~
- GoogleMap을 실행하기 위해 필요한 permission들을 String 배열에 정의
~~~java
String[] REQUIRED_PERMISSIONS  = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };
~~~
- GoogleMap 객체를 파라미터로 제공할 수 있을 때(사용할 준비가 되었을 때) onMapReady() 메소드 호출
~~~java
SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
mapFragment.getMapAsync(this);
...
@Override
public void onMapReady(final GoogleMap googleMap) {
    mMap = googleMap;
    setDefaultLocation();  //초기위치 서울로 이동

    // 위치 퍼미션을 가지고 있는지 확인
    int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

    if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
        startLocationUpdates();
    } else {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
            Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions( MainActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        }
    }
    mMap.getUiSettings().setMyLocationButtonEnabled(true);
    ...
}
~~~
- 4개의 지하철역 좌표 객체 생성 후 마커 추가
~~~java
LatLng Gwanghwamun = new LatLng(37.5707456,126.973708); //(위도, 경도)
LatLng Hongdae = new LatLng(37.557527,126.9222782);
LatLng Gileum = new LatLng(37.6086541,127.0136683);
LatLng DDP = new LatLng(37.5644,127.0055713);

MarkerOptions[] markerOptions = new MarkerOptions[4];
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
    mMap.addMarker(markerOptions[i]);
}
~~~
- 위치 정보 제공이 허용되어 있지 않은 경우 default 위치(국민대 부근, (37.6119, 126.9955)) 보여주는 setDefaultLocation() 메소드 호출
~~~java
public void setDefaultLocation() {
    LatLng DEFAULT_LOCATION = new LatLng(37.6119, 126.9955);
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
    
    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
    mMap.moveCamera(cameraUpdate);
}
~~~
- 마커 클릭하면 해당 지하철 역의 activity 실행
~~~java
private String mail;
private String station[] = {"길음역","광화문역","동역사역","홍대입구역"};
...
Intent intent = getIntent();
mail = intent.getStringExtra("mail");
...
@Override
public void onMapReady(final GoogleMap googleMap) {
    ...
    mMap.setOnMarkerClickListener(this);
}
...
@Override
public boolean onMarkerClick(Marker marker) {
    for (int i=0;i<station.length;i++) {
        if (marker.getTitle().equals(station[i])) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("mail", mail);
            intent.putExtra("station", station[i]);
            startActivity(intent);
        }
    }
    return true;
}
~~~
- 툴바에서 '뒤로가기(←)'를 클릭하였을 때 이전 화면으로 돌아갈 때 intent값 전달 추가 코드 작성(SearchActivity.java, ChatActivity.java)
~~~java
tb.setNavigationOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v){
        Intent intent = new Intent(this, 이동하고 싶은 activity의 class);
        intent.putExtra("mail", 사용자의 메일);
        intent.putExtra("station", 선택한 역);
        startActivity(intent);
    }
});
~~~
- 채팅방의 대화가 추가되어 ListView가 갱신될 때 하단으로 자동 스크롤
~~~java
lv_chating.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
~~~
