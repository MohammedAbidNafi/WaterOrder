package com.margsapp.waterorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    EditText phone_no;

    CountryCodePicker phone_code;

    AppCompatButton send_otp;

    LottieAnimationView otp_send_anim;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        phone_code = findViewById(R.id.ccp);
        phone_no = findViewById(R.id.phone_number);
        send_otp = findViewById(R.id.send_otp);
        otp_send_anim = findViewById(R.id.otpSendAnim);






        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                send_otp.setVisibility(View.GONE);
                otp_send_anim.setVisibility(View.VISIBLE);
                otp_send_anim.playAnimation();

                String number = getNumber();

                new Handler().postDelayed(() -> {


                    Intent intent = new Intent(SignUpActivity.this, OTP_verify.class);
                    intent.putExtra("phoneNo", number);
                    startActivity(intent);





                }, 2300);

            }
        });
    }

    private String getNumber() {
        String PhoneNumber = Objects.requireNonNull(phone_no.getText().toString());


        if (PhoneNumber.charAt(0) == '0') {
            PhoneNumber = PhoneNumber.substring(1);
        }

        return "+"+phone_code.getSelectedCountryCode()+PhoneNumber;

    }



}