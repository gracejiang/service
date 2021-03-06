package com.example.charibee.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.service.R;
import com.parse.ParseUser;

/*
 *  First screen that users see
 *  Option to Login or Register
 *  Redirects to main page if user is already logged in
 */

public class WelcomeActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;
    private ImageView ivIcon;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // check if user is already logged in
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            goMainActivity();
        }

        // bind ui views
        btnLogin = findViewById(R.id.welcome_login_btn);
        btnRegister = findViewById(R.id.welcome_register_btn);
        ivIcon = findViewById(R.id.welcome_icon);
        tvTitle = findViewById(R.id.welcome_title);

        // animate screen
        animateScreen();

        // set icon
        Drawable iconDrawable = getResources().getDrawable(R.drawable.ic_app_icon);
        ivIcon.setImageDrawable(iconDrawable);

        // when login button clicked
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginActivity();
            }
        });

        // when register button clicked
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegisterActivity();
            }
        });
    }

    private void animateScreen() {
        // hide login buttons on screen
        tvTitle.setAlpha(0);
        btnLogin.setAlpha(0);
        btnRegister.setAlpha(0);

        // animate icon
        ObjectAnimator iconBounceAnim = ObjectAnimator.ofFloat(ivIcon, "y", 250);
        iconBounceAnim.setDuration(1000);
        iconBounceAnim.setInterpolator(new BounceInterpolator());

        // fade in login buton
        ObjectAnimator titleFadeAnim = ObjectAnimator.ofFloat(tvTitle, "alpha", 0f, 1f);
        titleFadeAnim.setDuration(600);

        ObjectAnimator loginFadeAnim = ObjectAnimator.ofFloat(btnLogin, "alpha", 0f, 1f);
        loginFadeAnim.setDuration(600);

        ObjectAnimator registerFadeAnim = ObjectAnimator.ofFloat(btnRegister, "alpha", 0f, 1f);
        registerFadeAnim.setDuration(600);

        // animation order
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(iconBounceAnim).before(loginFadeAnim);
        animatorSet.play(loginFadeAnim).with(registerFadeAnim);
        animatorSet.play(loginFadeAnim).with(titleFadeAnim);
        animatorSet.start();

    }

    // goes to main activity
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();
    }

    // goes to login activity
    private void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    // goes to register activity
    private void goToRegisterActivity() {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

}