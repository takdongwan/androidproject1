package com.example.mure;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity  extends AppCompatActivity {

    private static final String TAG = "SignUpActivity ";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //버튼누를 때 예외처리리
        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.signUpButton:
                    Log.e("z클릭", "클릭");

                    //클릭 됐을시 실행
                    signUp();
                    break;
            }
        }
    };

    //회원관리 부분을 처리 함수로 만들어서관리.
    private void signUp() {
        String email = ((EditText) findViewById((R.id.emailEditText))).getText().toString();//스트링값이 아니기에 스트링값으로 변환 함.,입력한 텍스트를 받아옴
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        String passwordCheck = ((EditText) findViewById(R.id.passwordCheckEditText)).getText().toString();

        //이메일 입력을 안했을 경우 예외 처리
        if(email.length()>0 && password.length() > 0 && passwordCheck.length()>0 ) {
            if (password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {  //성공했을때 ui
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startToast("회원가입에 성공하였습니다.");
                                    //     Log.v(TAG, "createUserWithEmail:failure", task.getException());
                                } else {                                //실패  ui
                                    //   Log.v(TAG, "createUserWithEmail:failure", task.getException());
                                    if (task.getException() != null) {
                                        startToast(task.getException().toString());
                                    }
                                }
                                // ...
                            }
                        });
            } else {
                //비밀번호가 틀렸을경우 toast 창s
                startToast("비밀번호가 일치하지 않습니다.");
            }
        }else{
            startToast("이메일 또는 비밀번호를 입력해주세요");
        }
    }


    private void startToast(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
}
