package com.example.snsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        email_login_button.setOnClickListener {
            sigininAndSignup()
        }
    }
    fun sigininAndSignup(){
        auth?.createUserWithEmailAndPassword(email_edittext.text.toString(),password_edittext.text.toString())?.addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                //계정 생성
                Toast.makeText(this,"회원가입이 완료되었습니다",Toast.LENGTH_LONG).show()

            }else if (task.exception?.message.isNullOrEmpty()){
                //에러 메시지 출력
                Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
            }else{
                //계정 있을 시 로그인 화면으로
                signinEmail()
            }
        }
    }
    fun signinEmail(){
        auth?.signInWithEmailAndPassword(email_edittext.text.toString(),password_edittext.text.toString())?.addOnCompleteListener {
                task ->
            if(task.isSuccessful){
                //로그인 성공
                moveMainPage(task.result?.user)
            }else{
                //로그인 실패시 메시지
                Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
            }
        }
    }
    fun moveMainPage(user : FirebaseUser?){
        if(user != null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}