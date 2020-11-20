# [모바일 프로그래밍] Project_KookTaxi
### 국민대 소프트웨어학부 
## 20191604백연선 / 20191650이한정 / 20191670조나영 / 20191686최혜원
### https://github.com/Hyewon0223/KookTaxi
#### (20191604 백연선의 README 파일)
---
### 설명
<div>
    <img src="https://user-images.githubusercontent.com/55418359/99452281-b1200200-2966-11eb-96ea-0192e7beb21c.jpg" width="180">
    <img src="https://user-images.githubusercontent.com/55418359/99452288-b2512f00-2966-11eb-923c-83b82fc37abf.jpg" width="180">
    <img src="https://user-images.githubusercontent.com/55418359/99452289-b2e9c580-2966-11eb-9136-fec9ec33d89a.jpg" width="180">
    <img src="https://user-images.githubusercontent.com/55418359/99452292-b3825c00-2966-11eb-9f01-fbe6168c44da.jpg" width="180">
    <img src="https://user-images.githubusercontent.com/55418359/99683573-d70ee900-2ac3-11eb-8c53-8fae631ba652.jpg" width="180">
</div>

- 목적지가 국민대인 사람들을 모아 택시를 함께 탈 수 있도록 하는 앱입니다.
- 출발 지점(길음역, 광화문역, 홍대입구역, 동대문역사문화공원역)과 시간에 따라 사람들을 매칭해준다.
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
- 'SEND' 버튼 클릭시 key에 값 요청하고 updateChildren을 호출하여 database 업데이트(name, message)
~~~java
btn_send.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View view) {
        key = reference.push().getKey();
        DatabaseReference root = reference.child(key);

        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("name", str_user_mail);
        objectMap.put("message", et_send.getText().toString());

        root.updateChildren(objectMap);

        et_send.setText("");
    }
});
~~~
- 'OUT' 버튼 활성화 되어 있을 때 클릭시 사용자의 메일은 배열에서 삭제 후 데이터베이스의 'COUNT'값 1 감소하고 SearchActivity화면으로 돌아가기
~~~java
btn_out.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("ChatInfo").child(station).child(str_room_name);
        if (matched == false) { // 활성화 되어 있을 때
            if (Arrays.asList(user_list).contains(chat_user)) {
                int idx = Arrays.asList(user_list).indexOf(str_user_mail);
                user_list[idx] = ""; // 사용자의 이메일 저장하는 배열에서 'OUT'버튼 누른 사용자의 이메일 지우기
                cnt_user--; // 현재 채팅방에 있는 사용자의 수 1 감소
            }

            String cnt = Integer.toString(cnt_user);
            myRef.child("ChatInfo").child(station).child(str_room_name).child("COUNT").setValue(cnt); // 데이터베이스에 업데이트

            Intent intent = new Intent(ChatActivity.this, SearchActivity.class); // 화면전환하기
            intent.putExtra("mail", str_user_mail);
            intent.putExtra("station", station);
            startActivity(intent);
        }
        ...
    }
});
~~~
- 방장의 메일 및 현재 채팅방의 사용자의 수 받아오기(방장과 사용자의 메뉴바를 다르게 구현할 때 사용하기 위해 채팅창이 생성되었을 때 방장의 메일값을 같이 데이터베이스에 저장함)
~~~java
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        master_mail = snapshot.child("Email").getValue(String.class);
        user_list[0] = master_mail;

        user_cnt = snapshot.child("COUNT").getValue(String.class);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
    }
});
~~~
- 메뉴바 만들기
~~~java
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.menu1, menu);
    getMenuInflater().inflate(R.menu.menu_toolbar, menu);

    MenuItem item_master = menu.findItem(R.id.item_master);
    MenuItem item_user = menu.findItem(R.id.item_user);

    if (str_user_mail.equals(master_mail)) { // master의 메일 주소와 사용자의 메일 주소가 같을 경우 방장이므로
        item_master.setVisible(true); // 방장의 item만 메뉴바에서 볼 수 있음
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
- id값이 'item_matched'인 메뉴바의 item을 클릭하였을 때 전체 인원수(4명 제한)와 동일하면 'OUT'버튼 숨기기
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
        ...
    }
    return true;
}
~~~
- addChildEventListener를 통해 실제 데이터베이스에 변경된 값이 있을 때 채팅내용을 보여주는 ListView의 값 업데이트하기
~~~java
private void chatConversation(DataSnapshot dataSnapshot) {
    Iterator i = dataSnapshot.getChildren().iterator();
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("ChatInfo").child(station).child(str_room_name);

    while(i.hasNext()) {
        chat_message = (String) ((DataSnapshot) i.next()).getValue();
        chat_user = (String) ((DataSnapshot) i.next()).getValue();

        if (!Arrays.asList(user_list).contains(chat_user)) {
            user_list[cnt_user] = chat_user;
            cnt_user++;

            String cnt = Integer.toString(cnt_user);
            myRef.child("ChatInfo").child(station).child(str_room_name).child("COUNT").setValue(cnt);
        }

        arrayAdapter.add(chat_user + " : " + chat_message);
    }

    arrayAdapter.notifyDataSetChanged();
}
~~~
