package com.margsapp.waterorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.margsapp.iosdialog.iOSDialog;
import com.margsapp.iosdialog.iOSDialogListener;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OtpTextView;

public class OTP_verify extends AppCompatActivity {


    AppCompatButton verify;

    String phoneNo;

    TextView description;

    OtpTextView otp;

    String codeBySystem;

    SmsVerifyCatcher smsVerifyCatcher;

    private String verificationId;
    private static final String KEY_VERIFICATION_ID = "key_verification_id";

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);



        verify = findViewById(R.id.verify);

        phoneNo = getIntent().getStringExtra("phoneNo");

        description = findViewById(R.id.description);

        description.setText("An OTP has been sent to the number \n" + phoneNo);

        otp = findViewById(R.id.otpTextView);

        FirebaseAuth.getInstance().getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);

        callOTP(phoneNo);

        TextView resend = findViewById(R.id.resend);
        TextView wrongnumber = findViewById(R.id.wrong_number);


        resend.setOnClickListener(v -> {

            iOSDialog.Builder
                    .with(OTP_verify.this)
                    .setTitle("Rsend OTP")
                    .setMessage("Do you want to get OTP again?")
                    .setPositiveText("Yes")
                    .setPostiveTextColor(getApplicationContext().getResources().getColor(com.margsapp.iosdialog.R.color.red))
                    .setNegativeText("No")
                    .setNegativeTextColor(getApplicationContext().getResources().getColor(com.margsapp.iosdialog.R.color.company_blue))
                    .onPositiveClicked(new iOSDialogListener() {
                        @Override
                        public void onClick(Dialog dialog) {
                            callOTP(phoneNo);
                            Toast.makeText(OTP_verify.this, "Another has sent to" + phoneNo,Toast.LENGTH_SHORT).show();
                        }
                    })
                    .onNegativeClicked(new iOSDialogListener() {
                        @Override
                        public void onClick(Dialog dialog) {
                            //Do Nothing
                        }
                    })
                    .isCancellable(true)
                    .build()
                    .show();


        });

        wrongnumber.setOnClickListener(v -> onBackPressed());


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNextScreenFromOTP();
            }
        });

        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                otp.setOTP(message);
                verifyCode(message);

            }
        });
    }

    private void callOTP(String _phoneNumber) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(_phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);        // OnVerificationStateChangedCallbacks
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(OTP_verify.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        //otp.setOTP(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                }


            };

    private void verifyCode(String code) {
        //FirebaseAuth.getInstance().getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);

    }





    private void callNextScreenFromOTP(){
        String code = Objects.requireNonNull(otp.getOTP());
        if(!code.isEmpty()){
            verifyCode(code);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //Verification completed successfully
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        assert firebaseUser != null;
                        String userid = firebaseUser.getUid();

                        startActivity(new Intent(OTP_verify.this, Personal_InfoActivity.class));

                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(OTP_verify.this, "Verification Not Completed! Try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

}