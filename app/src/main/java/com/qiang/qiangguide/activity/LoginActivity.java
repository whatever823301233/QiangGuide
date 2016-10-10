package com.qiang.qiangguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.IUserLoginView;
import com.qiang.qiangguide.bean.User;
import com.qiang.qiangguide.presenter.UserLoginPresenter;
import com.qiang.qiangguide.util.KeyBoardUtil;
import com.qiang.qiangguide.util.Utility;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActivityBase implements IUserLoginView {


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private UserLoginPresenter mUserLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         mUserLoginPresenter = new UserLoginPresenter(this);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        if (mEmailSignInButton != null) {
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUserLoginPresenter.login();
                }
            });
        }
        Button mBtnClear = (Button) findViewById(R.id.mBtnClear);
        if (mBtnClear != null) {
            mBtnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUserLoginPresenter.clear();
                }
            });
        }
    }

    @Override
    void errorRefresh() {

    }


    @Override
    public String getUserName() {
        return mEmailView.getText().toString();
    }

    @Override
    public String getPassword() {
        return mPasswordView.getText().toString();
    }

    @Override
    public void clearUserName() {
        mEmailView.setText("");
    }

    @Override
    public void clearPassword() {
        mPasswordView.setText("");
    }

    @Override
    public void showLoading() {
        mProgressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressView.setVisibility(View.GONE);
    }

    @Override
    public void hideKeyboard() {
        KeyBoardUtil.hideKeyBoard(getActivity());
    }

    @Override
    public void toMainActivity( User user ) {
        Intent intent = new Intent( this,MainActivity.class );
        intent.putExtra( User.INTENT_USER,user );
        Utility.startActivity(getActivity(),intent);
        showToast("登录成功");
    }

    @Override
    public void showFailedError() {
        showToast("登录失败");
    }
}

