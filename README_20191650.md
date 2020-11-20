# [모바일 프로그래밍] Project_KookTaxi
### 국민대 소프트웨어학부 
## 20191604백연선 / 20191650이한정 / 20191670조나영 / 20191686최혜원
### https://github.com/Hyewon0223/KookTaxi
---
### 설명


- 목적지가 국민대인 사람들을 모아 택시를 함께 탈 수 있도록 하는 앱이다.
- 동승하고 싶은 사람의 성별 여부, 인원수, 출발 지점(길음역, 광화문역, 홍대입구역, 동대문역사문화공원역) 등에 따라 사람들을 매칭해준다.
---
### 개발 환경
- Window OS
- Android Studio 4.1.1 (AVD: Pixel 2 API 30 / 안드로이드 폰)
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
### 프로젝트 구성
* **5개의 액티비티**
  * **LoginActivity** - 로그인을 통해 MainActivity로 연결해주고, 회원이 아닐 시 JoinActivity로 연결해주는 액티비티
  * **JoinActivity** - 회원가입을 하는 액티비티
  * **MainActivity** - GPS기반으로 지도를 띄우고 서비스를 제공하는 역의 마커를 표시, 마커를 누르면 해당 역의 SearchActivity로 연결해주는 액티비티
  * **SearchActivity** - 본인이 탑승을 원하는 시간대의 방을 개설하거나 이미 있는 방에 들어가게 해주는 액티비티. ChatActivity로 연결해줌
  * **ChatActivity** - 사용자들끼리 채팅을 나눌 수 있도록 구현, 해당 어플의 원하는 대부분의 기능 동작을 위한 액티비티

* **8개의 xml**
  * **layout**
    * **activity_login.xml**
    * **activity_join.xml**
    * **activity_main.xml**
    * **activity_search.xml**
    * **activity_chat.xml**
    * **toolbar.xml**
  * **menu**
    * **menu1.xml**
    * **menu_toolbar.xml**
---
### 실행 화면
<div>
    <img src="https://user-images.githubusercontent.com/55418359/99452281-b1200200-2966-11eb-96ea-0192e7beb21c.jpg" width="200">
    <img src="https://user-images.githubusercontent.com/55418359/99452288-b2512f00-2966-11eb-923c-83b82fc37abf.jpg" width="200">
    <img src="https://user-images.githubusercontent.com/55418359/99452289-b2e9c580-2966-11eb-9136-fec9ec33d89a.jpg" width="200">
    <img src="https://user-images.githubusercontent.com/55418359/99452292-b3825c00-2966-11eb-9f01-fbe6168c44da.jpg" width="200">
    <img src="https://user-images.githubusercontent.com/54920378/99683690-f86fd500-2ac3-11eb-87f0-1c796ebb53b8.jpg" width="200">
</div>

---
### 코드 관련 설명
**manifests**
* AndroidManifest.xml
  * 사용자의 위치 정보 접근을 위한 액세스 권한 
~~~xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
~~~
**xml**
* activity_main.xml
  * 기본 레이아웃은 RelativeLayout
  * map component 추가를 위한 Frame Layout 사용
<div>
  <img src="https://user-images.githubusercontent.com/54920378/99691191-4b4d8a80-2acc-11eb-9327-c3021f76e521.PNG" width="415">
  <img src="https://user-images.githubusercontent.com/54920378/99691196-4be62100-2acc-11eb-8f24-a3d7625142d9.PNG" width="200">
</div>

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
  
* activity_chat.xml
  * 기본 레이아웃은 LinearLayout
  * 채팅이 표시되는 부분은 ListView로 구현
  * 각종 View들을 이용하여 필요한 레이아웃 생성
  * 내가 사용하는 AVD화면에 알맞게 View들을 배치했다가 다른 조원들의 화면에서 버튼이 화면밖으로 나가는 등의 경우가 발생하여 우선 두개의 버튼을 동일한 위치에 두기 위해 gravity = right로 설정하여 오른쪽으로 정렬한 후, 옆의 CheckedTextView는 버튼으로부터의 margin을 통해 배치하였다.
<div>
  <img src="https://user-images.githubusercontent.com/54920378/99692850-18a49180-2ace-11eb-8bdb-16f5ce5c67b1.PNG" width="415">
  <img src="https://user-images.githubusercontent.com/54920378/99692854-193d2800-2ace-11eb-9be3-e2d1c2a3cac1.PNG" width="200">
</div>

~~~xml
<ListView
    android:id="@+id/lv_chating"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_marginBottom="100dp"></ListView>
    
<CheckedTextView
    android:id="@+id/checkedTextView1"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginStart="5dp"
    android:layout_marginLeft="5dp"
    android:checkMark="?android:attr/listChoiceIndicatorMultiple"
    android:checked="false"
    android:text="사용자1" />
<EditText
    android:id="@+id/et_send"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:privateImeOptions="defaultInputmode=korean" />
~~~

* menu1.xml
  * menu - item 구조로 옵션 메뉴를 생성
  * group의 checkableBehavior를 이용하여 체크 기능을 이용하려 하였으나, 체크가 되지 않아(이유를 찾지 못함) 이용하지 못함. 아직 강퇴하기 메뉴에는 남아있음. 대신에 activity_chat.xml에 CheckedTextView를 이용함.
<div>
    <img src="https://user-images.githubusercontent.com/54920378/99696826-776c0a00-2ad2-11eb-96a4-4392bdee931c.PNG" width="230">
</div>

##### 방장의 경우
<div>
    <img src="https://user-images.githubusercontent.com/54920378/99698153-c9615f80-2ad3-11eb-806d-4d9246415963.PNG" width="220">
    <img src="https://user-images.githubusercontent.com/54920378/99696819-75a24680-2ad2-11eb-9727-14f25ac02c69.PNG" width="230">
    <img src="https://user-images.githubusercontent.com/54920378/99696820-75a24680-2ad2-11eb-8029-60862e0b1e55.PNG" width="235">
    <img src="https://user-images.githubusercontent.com/54920378/99696821-763add00-2ad2-11eb-9072-d85f17fd17d7.PNG" width="232">
</div>

##### 사용자의 경우
<div>
    <img src="https://user-images.githubusercontent.com/54920378/99697853-74254e00-2ad3-11eb-8058-9862849a4b5b.PNG" width="255">
    <img src="https://user-images.githubusercontent.com/54920378/99697851-738cb780-2ad3-11eb-9c42-8788fb7b5d34.PNG" width="230">
</div>


### Java
#### MainActivity.java
* 구글맵 객체 선언, 현재 마커 선언
~~~java
private GoogleMap mMap;
private Marker currentMarker = null;
~~~
* 구글맵 실행을 위해 필요한 퍼미션 정의
~~~java
String[] REQUIRED_PERMISSIONS  = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };
~~~
* 마커 생성할 네개의 역 배열을 생성
~~~java
private String station[] = {"길음역","광화문역","동역사역","홍대입구역"};
~~~
* 구글맵이 사용될 준비가 되었을 때(GoogleMap 객체를 파라미터로 제공할 수 있을 때) 자동으로 호출되는 메소드
~~~java
public void onMapReady(final GoogleMap googleMap) {  //map이 사용할 준비가 되었을 때(GoogleMap 객체를 파라미터로 제공할 수 있을 때) 호출되는 메소드
    mMap = googleMap;
    setDefaultLocation();  //초기위치 국민대로 이동
    ...
~~~
* 위치 퍼미션을 가지고 있는지 확인
~~~java
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
~~~
* 좌표 객체(주요 4개 역)를 생성한다. 추후에는 더 많은 좌표로 늘릴 수 있다.
    * 구글맵을 통해 각 역의 (위도, 경도)를 받아왔으나, 약간의 오차는 발생한다.
~~~java
LatLng Gwanghwamun = new LatLng(37.5707456,126.973708); //(위도, 경도)
LatLng Hongdae = new LatLng(37.557527,126.9222782);
LatLng Gileum = new LatLng(37.6086541,127.0136683);
LatLng DDP = new LatLng(37.5644,127.0055713);
~~~
* 좌표 객체를 position으로, 위치를 title로 하는 markerOptions 객체 4개를 만든 후 배열에 넣는다. 
~~~java
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
    .title("동역사역");
for(int i=0; i<4; i++){
    mMap.addMarker(markerOptions[i]); //addMarker()를 통해 GoogleMap객체(mMap)에 추가하면 지도에 표시된다.
}
~~~
* 최근 위치를 받아오는 함수
    * locationList에 Location을 받아서 계속해서 넣고 (locationList.size()-1) 번째 Location객체(가장 최근 Location)를 location으로 설정한다.
    * 현재 경도 위도를 담고 있는 Lating을 currentPosition이라고 한다
    * 이 위치를 setCurrentLocation()의 인자로 보내는데 AVD를 이용할 경우 GPS가 잡히지 않아 초기 위치가 미국으로 잡힌다. 따라서 AVD에서 이 어플을 이용하기 위해서는 setCurrentLocation(location, markerTitle, markerSnippet);를 주석처리한 후 밑의 국민대로 설정한 DEFAULT_LOCATION을 이용하여 카메라를 움직인다.
~~~java
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
~~~
* 현재 주소를 받아오는 함수. 지오코더 클래스를 이용하여 위도와 경도로부터 주소를 알아냄.
~~~java
public String getCurrentAddress(LatLng latlng) {
    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
    List<Address> addresses;

    try {
        addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude,1);
    } catch (IOException ioException) {
        Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
        return "지오코더 서비스 사용불가";
...
}
~~~
* LocationManager를 통해 GPS와 네트워크가 사용 가능한지 체크해주는 함수. 네트워크도 켜져있어야 어플 사용이 가능함
~~~java
public boolean checkLocationServicesStatus() { 
    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
}
~~~
* 현재 위치로 카메라(어플상에 보이는 화면)을 이동시켜주는 함수
    * AVD에서 이용할 시에는 이 함수를 이용할 수 없고 아래의 setDefaultLocation() 함수를 이용해야함.
    * 마커를 누르면 마커의 옵션을 보여주지만, 현재는 마커 클릭시 바로 다음 액티비티로 넘어가서 볼 수 없음
~~~java
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
~~~
* 디폴트 카메라 위치를 설정해주는 함수
    * 우리의 경우 역 마커들의 대부분 국민대 주변에 있으므로 국민대를 디폴트 카메라 위치로 설정함
    * newLatingZoom() 함수를 이용하여 카메라의 줌 정도를 설정해봄
~~~java
public void setDefaultLocation() {  //Default MarkerOptions를 설정해주는 함수(위치정보 제공이 허용되어 있지 않은 경우)
        LatLng DEFAULT_LOCATION = new LatLng(37.6119, 126.9955);  //첫 위치를 서울(국민대 부근)으로 설정
        String markerTitle = "위치정보 가져올 수 없음"; //setCurrentLocation과 다르게 인자로 넘어오지 않으므로 직접 설정해줘야함
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
~~~
* 런타임 퍼미션 처리 메소드들 (소스파일에 주석 작성함)
~~~java
private boolean checkPermission() {
}
public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
}
~~~
* GPS 활성화를 위한 메소드들 (소스파일에 주석 작성함)
~~~java
private void showDialogForLocationServiceSetting() {
}
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
}
~~~
* 마커 클릭 이벤트를 처리하는 함수
    * 마커를 클릭하면 메일과 역 정보를 담아 intent를 이용하여 SearchActivity로 전달함.
~~~java
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

#### ChatActivity.java (옵션 메뉴 담당)
* 액티비티가 시작될 때 호출되는 함수. 단 한 번만 호출되기 때문에 MenuItem 생성과 초기화를 모두 이 함수에서 해야함. MenuInflater를 통해 메뉴.xml에 정의된 메뉴를 파싱하여 Menu 객체를 생성함. 
    * menu1.xml에서 옵션메뉴를 처음 누르면 방장/사용자로 구분되는데 이를 각자 방장에게만, 사용자에게만 보이게 하기 위해 데이터베이스의 이메일 값을 이용하여 VISIBLE/INVISIBLE을 설정해주었다.
~~~java
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.menu1, menu);
    getMenuInflater().inflate(R.menu.menu_toolbar, menu);

    MenuItem item_master = menu.findItem(R.id.item_master);
    MenuItem item_user = menu.findItem(R.id.item_user);

    if (str_user_mail.equals(master_mail)) {
        item_master.setVisible(true);
        item_user.setVisible(false);
    }
    else {
        item_master.setVisible(false);
        item_user.setVisible(true);

        check_text1.setVisibility(View.INVISIBLE);
        check_text2.setVisibility(View.INVISIBLE);
        check_text3.setVisibility(View.INVISIBLE);
    }

    return true;
}
~~~
* 옵션 메뉴의 아이템들이 눌렸을 때의 이벤트를 switch문으로 설정하는 함수(가장 아쉬움이 남는 부분)
    * 원래의 목적
        * 매칭 완료 아이템을 누르면 사용자들을 채팅방에서 나가지 못하도록 함(OUT 버튼이 사라짐,데이터 베이스에 데이터 남겨놓음) & activity_search.xml에서 매칭완료된 채팅방은 invisivle하게 만듦.
        * 입금 확인 메뉴에서 입금한 사용자 아이템을 누르면(현재는 사용자1,2,3으로 구현되어 있지만 데이터베이스에서 id값이나 email값을 받아와서 들어온 순서대로 item의 title을 설정하고 싶음) 해당 사용자의 CheckedTextView에 체크가 되고 그 사용자는 OUT 버튼이 다시 생겨서 나갈 수 있음. 또한 해당 사용자의 정보를 데이터베이스에서 삭제함
        * 강퇴하기 메뉴에서 특정 사용자 아이템을 누르면 그 사용자는 이 액티비티에서 나가짐
        * 사용자 메뉴의 입금하기 아이템을 누르면 송금할 수 있도록 방법을 찾음
        * 방 인원을 최대 4명으로 하고 현재 몇명이 채팅방에 있는지 activity_search.xml의 채팅방 이름에 보이게 함
    * 위의 대부분의 경우 소켓프로그래밍을 이용해야하는데 이 부분을 모른채 왜 방장에게만 버튼이 없어지는지를 오래 고민함. 
    * 현재는 방장에게만 OUT버튼이 INVISIVLE 되도록 코드가 작성되어 있음.
~~~java
@Override
public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch(item.getItemId()) {
        case R.id.item_matched:
            int num = Integer.parseInt(user_cnt);
            if(num == 4){
                btn_out.setVisibility(View.INVISIBLE);
            }
            matched = true;
            return true;
        case R.id.item_user1:
            confirm_cnt++;
            check_text1.setChecked(true);
            return true;
...
        // 강퇴하기
        case R.id.item_user_1:
            int user_idx = Arrays.asList(user_list).indexOf(str_user_mail);
            if (user_idx == 1) {
                user_list[user_idx] = "";
                cnt_user--;

                Intent intent = new Intent(ChatActivity.this, SearchActivity.class);
                intent.putExtra("mail", str_user_mail);
                intent.putExtra("station", station);
                startActivity(intent);
            }
            return true;
...
    }
    return true;
}

~~~

