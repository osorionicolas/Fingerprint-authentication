package com.nosorio.fingerprintauth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView msg_text = findViewById(R.id.txt_msg);
        Button login_btn = findViewById(R.id.login_btn);

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                msg_text.setText("You can use the fingerprint sensor to login");
                msg_text.setTextColor(Color.parseColor("#Fafafa"));
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msg_text.setText("The device don't have a fingerprint sensor");
                login_btn.setVisibility(View.GONE);
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msg_text.setText("The biometric sensor is currently unavailable");
                login_btn.setVisibility(View.GONE);
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msg_text.setText("Your device don't have any fingerprint saved, please check your security settings");
                login_btn.setVisibility(View.GONE);
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);
        final BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("Use your fingerprint to login to your app")
                .setNegativeButtonText("Cancel")
                .build();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }
}
