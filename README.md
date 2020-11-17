# [모바일 프로그래밍] Project_KookTaxi
### 국민대 소프트웨어학부 
## 20191604백연선 / 20191650이한정 / 20191670조나영 / 20191686최혜원
### https://github.com/Hyewon0223/KookTaxi
---
### 설명
<div>
    <img src="https://user-images.githubusercontent.com/55418359/99452281-b1200200-2966-11eb-96ea-0192e7beb21c.jpg" width="200">
    <img src="https://user-images.githubusercontent.com/55418359/99452288-b2512f00-2966-11eb-923c-83b82fc37abf.jpg" width="200">
    <img src="https://user-images.githubusercontent.com/55418359/99452289-b2e9c580-2966-11eb-9136-fec9ec33d89a.jpg" width="200">
    <img src="https://user-images.githubusercontent.com/55418359/99452292-b3825c00-2966-11eb-9f01-fbe6168c44da.jpg" width="200">
    <img src="https://user-images.githubusercontent.com/55418359/99452294-b3825c00-2966-11eb-82ac-fda58a49c196.jpg" width="200">
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
##### activity_login.xml, activity_join.xml, activity_main.xml, activity_search.xml, activity_chat.xml
- Relative Layout(Login 화면, Join 화면, Main 화면), Frame Layout(Main 화면), Linear Layout(Search 화면, Chat 화면)
- 모든 화면에 툴바 추가
~~~xml
<include
    layout="@layout/toolbar"
    android:id="@+id/toolbar"/>
~~~
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
- 실시간 변경이 가능한 데이터베이스에 있는 채팅방 목록 화면에 출력
~~~xml
<ListView
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginBottom="50dp" />
~~~
#### menu
##### menu1.xml, menu_toolbar.xml
- 각 페이지의 툴바 작성
#### Java
##### LoginActivity.java, JoinActivity.java, MainActivity.java, SearchActivity.java, CharActivity.java
- 툴바 뒤로가기 버튼 구현
~~~java
androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar);
setSupportActionBar(tb);

getSupportActionBar().setDisplayHomeAsUpEnabled(true);
...
@Override
public boolean onCreateOptionsMenu(Menu menu){
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.menu_toolbar, menu);
    return true;
}
~~~
- Firebase 설정
~~~java
FirebaseDatabase database = FirebaseDatabase.getInstance();
DatabaseReference myRef = database.getReference();
firebaseAuth = FirebaseAuth.getInstance();
~~~
- 국민대 이메일이 아닌 경우 회원가입할 수 없음
~~~java
String mailCheck[] = mail.split("@");
...
else if (!mailCheck[1].equals("kookmin.ac.kr")) alarmtext.setText("Please check the email.");
~~~
- 회원가입의 조건이 모두 만족하면 map에 넣은 후 Firebase의 "UserInfo"의 children으로 업데이트하고 Login 페이지로 이동
~~~java
...
firebaseAuth.createUserWithEmailAndPassword(mail,pw).addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        ... {
            Map<String, Object> values = toMap(pw, dp, mail, phone, gender);

            myRef.child("UserInfo").child(Id).updateChildren(values);
            
            Intent it = new Intent(JoinActivity.this, LoginActivity.class);
            startActivity(it);
        }
        ...
    }
});
...
public Map<String, Object> toMap(String pw, String dp, String mail, String phone, String gender) {
    HashMap<String, Object> result = new HashMap<>();

    result.put("Password", pw);
    result.put("Department", dp);
    result.put("Email", mail);
    result.put("Phone Number", phone);
    result.put("Gender", gender);

    return result;
}
~~~
- 회원가입의 조건이 충족되지 않는다면 해당하는 조건을 alarmtext로 출력
~~~java
private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
...
TextView alarmtext = (TextView) findViewById(R.id.alarmText);
...
joinButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ...
        String mailCheck[] = mail.split("@");

        //password 양식에 적합한지 검사
        for (int i = 1; i < 2; i++)
            if (!PASSWORD_PATTERN.matcher(info[i]).matches()) edits[i].setHint("Please adjust the format.");

        //빈 칸이 있는지 확인
        for (int i = 0; i < info.length; i++)
            if (info[i].equals("")) edits[i].setHint(alarm[i] + "was not filled.");

        if (mailCheck[1].equals("kookmin.ac.kr") && pw.equals(pwCheck) && Check.isChecked() && gender != "") {
            if (Id.length() != 0 && pw.length() != 0 && dp.length() != 0 && mail.length() != 0 && phone.length() != 0) {
                        ...
                        // 아이디(학번) 중복
                        else alarmtext.setText("ID is already existed.");
                    }
                });
            }
            else { // 하나라도 조건이 충족되지 않는다면
                for (int i = 0; i < info.length; i++)
                    if (info[i].equals("")) edits[i].setHint(alarm[i] + "was not filled.");
            }
        }
        else if (!pw.equals(pwCheck)) alarmtext.setText("Please check the password.");
        else if (!mailCheck[1].equals("kookmin.ac.kr")) alarmtext.setText("Please check the email.");
        else if (gender.equals("")) alarmtext.setText("Please check the gender.");
        else if (!Check.isChecked()) alarmtext.setText("Please check to allow personal information.");
        else alarmtext.setText("Please check the gender.");
    }
});
~~~
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
- GoogleMap을 실행하기 위해 필요한 permissiond을 String 배열에 정의
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
- 툴바에서 '뒤로가기(←)'를 클릭하였을 때 이전 화면으로 돌아갈 수 있도록 추가 코드 작성(SearchActivity.java, ChatActivity.java)
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
- ListView에 Firebase의 채팅방 리스트 실시간으로 출력
~~~java
arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr_roomList);
listView.setAdapter(arrayAdapter);
...
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot){ //데이터베이스가 변경되었을 때 호출
        Set<String> set = new HashSet <String>();
        Iterator i = dataSnapshot.child("ChatInfo").child(station).getChildren().iterator();

        while (i.hasNext()){
            set.add(((DataSnapshot) i.next()).getKey());
        }
        arr_roomList.clear();
        arr_roomList.addAll(set);

        arrayAdapter.notifyDataSetChanged();
    }
    ...
});
~~~
- 다이얼로그에서 채팅방 이름(ex."10시30분")을 만들고 채팅방 생성한 후 Firebase의 "ChatInfo"의 children으로 업데이트(사용자의 mail과 채팅방의 이름)
~~~java
btn_create.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View view){
        final TimePicker et_inDialog = new TimePicker(SearchActivity.this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setTitle("채팅방 이름 입력");
        builder.setView(et_inDialog);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                int time_hour = et_inDialog.getCurrentHour();
                int time_minute = et_inDialog.getCurrentMinute();
                str_room = time_hour+"시"+time_minute+"분";

                Map<String, Object> values = toMap(mail, str_room);
                map.put(str_room,"");
                reference.child("ChatInfo").child(station).child(str_room).updateChildren(values);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
});
...
public Map<String, Object> toMap(String mail, String strRoom) {
    HashMap<String, Object> result = new HashMap<>();

    result.put("Email", mail);
    result.put("Room", strRoom);

    return result;
}
~~~
- 채팅방의 대화가 추가되어 ListView가 갱신될 때 하단으로 자동 스크롤
~~~java
lv_chating.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
~~~
