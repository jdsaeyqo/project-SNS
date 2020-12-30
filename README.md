# 프로젝트 소개
- 제목 : Sstargram
- 구분 : 개인 프로젝트
- 개발환경 - 안드로이드 스튜디오
- 사용 언어 - Kotlin
- 내용  
인스타그램 클론앱   
사진 업로드, 좋아요, 팔로우, 댓글 구현

---
# 개요
로그인

![image1](https://github.com/jdsaeyqo/project-SNS/blob/master/app/src/main/res/drawable/sns_image_1.jpg)  

- Firebase 이메일/구글 로그인
- Facebook 로그인

메인 | 리스트 | 계정
:------:|:-------:|:------:|
![image3](https://github.com/jdsaeyqo/project-SNS/blob/master/app/src/main/res/drawable/sns_image_3.jpg)|![image2](https://github.com/jdsaeyqo/project-SNS/blob/master/app/src/main/res/drawable/sns_image_2.jpg)|![image5](https://github.com/jdsaeyqo/project-SNS/blob/master/app/src/main/res/drawable/sns_image_5.jpg)

- 메인 RecyclerView
- FirebaseFirestore, Storage
- GridLayoutManager(리스트, 계정)
- Glide
- 메인에 타 계정 게시글 프로필 사진 통해 해당 계정 프래그먼트 접근 가능 
- 타 계정 시 팔로우 버튼 , 자신 계정 시 로그아웃 버튼  
---
# 오류
64K 메서드 제한 오류 발생  

해당 앱 minSdkVersion이21이상으로 설정된 경우 멀티덱스가 기본적으로 사용 설정.  
하지만 minSdkVersion이 20이하 일 경우 멀티덱스 지원 라이브러리 추가, 혹은 21 이상으로 설정해야 함.  

---
# Library
~~~kotlin  
  apply plugin: 'com.google.gms.google-services'

 //Firebase
   implementation 'com.google.firebase:firebase-auth:20.0.1'
    implementation 'com.google.firebase:firebase-storage:19.2.1'
    implementation 'com.google.firebase:firebase-firestore:22.0.1'
    implementation 'com.google.firebase:firebase-messaging:21.0.1'
    
//Facebook
    implementation 'com.facebook.android:facebook-android-sdk:4.42.0'
//Google
    implementation 'com.google.android.gms:play-services-auth:19.0.0'
  
// glide
    implementation 'com.github.bumptech.glide:glide:4.11.0
    
//multidex
    implementation 'androidx.multidex:multidex:2.0.1'
//support
    implementation 'com.google.android.material:material:1.3.0-beta01'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
~~~







