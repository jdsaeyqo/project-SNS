package com.example.snsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    var googleSigninClient: GoogleSignInClient? = null
    var GOOGLE_LOGIN_CODE = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        email_login_button.setOnClickListener {
            sigininAndSignup()
        }
        google_signin_button.setOnClickListener {
            //구글 로그인 첫 단계
            googleLogin()
        }

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSigninClient = GoogleSignIn.getClient(this, gso)
    }

    fun googleLogin() {
        var signInIntent = googleSigninClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_LOGIN_CODE) {
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result!!.isSuccess) {
                var account = result.signInAccount
                //구글 로그인 두번째 단계
                firebaseAuthWithGoogle(account)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        var cridential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth?.signInWithCredential(cridential)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //로그인 성공
                moveMainPage(task.result?.user)
            } else {
                //로그인 실패시 메시지
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }


    fun sigininAndSignup() {
        auth?.createUserWithEmailAndPassword(
            email_edittext.text.toString(),
            password_edittext.text.toString()
        )?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //계정 생성
                Toast.makeText(this, "회원가입이 완료되었습니다", Toast.LENGTH_LONG).show()

            } else if (task.exception?.message.isNullOrEmpty()) {
                //에러 메시지 출력
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
            } else {
                //계정 있을 시 로그인 화면으로
                signinEmail()
            }
        }
    }

    fun signinEmail() {
        auth?.signInWithEmailAndPassword(
            email_edittext.text.toString(),
            password_edittext.text.toString()
        )?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //로그인 성공
                moveMainPage(task.result?.user)
            } else {
                //로그인 실패시 메시지
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}