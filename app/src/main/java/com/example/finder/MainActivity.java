package com.example.finder;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private String enteredPhoneNumber,mVerificationId;
    private EditText enter;
    Button sendcode;
    private static String token;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enter = (EditText)  findViewById(R.id.phone);
        sendcode = (Button) findViewById(R.id.btn);
        sendcode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                setUpTheCallBacks();
                verifyPhoneNumber();
            }
        });
    }
    private void setUpTheCallBacks()
    {

//        This is the code that automatically sets the code to the edit text
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {

                final String code = phoneAuthCredential.getSmsCode();

                if(code!= null)
                {
                     enter.setText(code);
//                   Show the pop-up dialog showing authentication
                    verifyVerificationCode(code);
                }

            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                Toast.makeText(MainActivity.this,"Verification failed " + e.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("VerificationFailed","This is the message " + e.getMessage());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
            {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;

//                This is the code that is being used when one needs code to be resent
                resendToken = forceResendingToken;
            }
        };

    }


    public void verifyPhoneNumber()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber
                (
                        enteredPhoneNumber,        // Phone number to verify
                        120,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        this,               // Activity (for callback binding)
                        mCallBacks);


    }

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {


                            Toast.makeText(MainActivity.this,"Successful " , Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, select.class);
                            intent.putExtra("Phonenumber",enteredPhoneNumber);
                            startActivity(intent);


                        }
                        else
                        {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException)
                            {
                                // The verification code entered was invalid
                                Log.d("ErrorVerfiying","This is the error " + task.getException());
                                Log.d("ErrorVerfying","There is an error verrfying the user " + task.getException());
                            }

                            Toast.makeText(MainActivity.this,"Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }


}
