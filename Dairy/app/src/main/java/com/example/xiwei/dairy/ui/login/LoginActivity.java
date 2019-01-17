package com.example.xiwei.dairy.ui.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.xiwei.dairy.MainActivity;
import com.example.xiwei.dairy.R;
import com.example.xiwei.dairy.WriteCalender;
import com.example.xiwei.dairy.ui.login.LoginViewModel;
import com.example.xiwei.dairy.ui.login.LoginViewModelFactory;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.xiwei.dairy.ui.login.ServerIP.LOGURL;
import static com.example.xiwei.dairy.ui.login.ServerIP.SIGNURL;
import static com.example.xiwei.dairy.ui.login.SharedPreferenceHelper.setLoggingStatus;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private LoginViewModel loginViewModel;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button logButton;
    private Button signButton;
    private String username = "404";
    private SharedPreferences sharedPreferences;
    private CardView cardView;
    public int sTheme = WriteCalender.sTheme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (sTheme!=0) {
            //设置主题
            setTheme(sTheme);
        }
        setContentView(R.layout.signin);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        logButton = findViewById(R.id.login);
        signButton = findViewById(R.id.register);

        logButton.setOnClickListener(this);//或许只有this
        signButton.setOnClickListener(this);

        /*loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final ProgressBar loadingProgressBar = findViewById(R.id.loading);*/

        /*loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                logButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });*/

        /*logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });*/
    }

    @Override
    public void onClick(View v){
        String userName = usernameEditText.getText().toString();
        username = userName;
        String passWord = passwordEditText.getText().toString();
        System.out.println(userName+" "+passWord);
        if(userName.equals("")||passWord.equals(""))
        {
            showWarnSweetDialog("账号密码不能为空");
            System.out.println("账号密码不能为空");
            return;
        }

        if(Character.isDigit(userName.charAt(0)))
        {
            showWarnSweetDialog("账号不能以数字开头");
            System.out.println("账号不能以数字开头");
            return;
        }
        switch (v.getId())
        {
            case R.id.login:
                String url = LOGURL;/*在此处改变你的服务器地址*/
                getCheckFromServer(url,userName,passWord);
                break;
            case R.id.register:
                String url2 = SIGNURL;/*在此处改变你的服务器地址SIGNURL*/
                registeNameWordToServer(url2,userName,passWord);
                break;
        }
    }

    private void getCheckFromServer(String url,final String userName,String passWord)
    {
        OkHttpClient client = new OkHttpClient(); //set up an HTTP connection
        //FromBody：表单数据提交
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", userName); //add the username and password
        formBuilder.add("password", passWord);
        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        Call call = client.newCall(request);
        //将call加入调度队列
        //等待任务执行完成，我们在callback中即可得到结果
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showWarnSweetDialog("服务器错误");
                        System.out.println("服务器错误");
                    }
                });

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                //请求成功返回结果
                //希望返回的是字符串
                final String res = response.body().string();
                //注意，此时的线程不是ui线程，
                //如果此时我们要用返回的数据进行ui更新，操作控件，就要使用相关方法
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (res.equals("0")) //从服务器得到的response
                        {
                            showWarnSweetDialog("无此账号,请先注册");
                            System.out.println("无此账号,请先注册");
                        }
                        else if(res.equals("1")) //从服务器得到的response
                        {
                            showWarnSweetDialog("密码不正确");
                            System.out.println("密码不正确");
                        }
                        else//成功
                        {
                            showSuccessSweetDialog(res);
                            sharedPreferences = getSharedPreferences("UserIDAndPassword", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", userName);
                            editor.apply();
                            // finish();
                        }

                    }
                });
            }
        });

    }

    /**
     * 将用户名与密码发送给服务器进行注册活动
     * @param url 服务器地址
     * @param userName 用户名
     * @param passWord 密码
     */
    private void registeNameWordToServer(String url,final String userName,String passWord)
    {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", userName);
        formBuilder.add("password", passWord);
        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showWarnSweetDialog("服务器错误");
                        System.out.println("服务器错误");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException
            {
                final String res = response.body().string();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (res.equals("0"))
                        {
                            showWarnSweetDialog("该用户名已被注册");
                            System.out.println("该用户名已被注册");
                        }
                        else
                        { // 成功注册
                            showSuccessSweetDialog(res);
                            sharedPreferences = getSharedPreferences("UserIDAndPassword", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", userName);
                            editor.apply();
                        }

                    }
                });
            }
        });

    }

    private void showWarnSweetDialog(String info)
    {
        SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(info);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    private void showSuccessSweetDialog(String info)
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(info);
        pDialog.setCancelable(true);
        pDialog.show();
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
        {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog)
            {
                pDialog.dismiss();
                playAndIntent(cardView);
            }
        });
    }

    private void playAndIntent(View view)
    {
//        DBOpenHelper dbHelper = new DBOpenHelper(this, "friends.db", null, 1,username);
//        dbHelper.getWritableDatabase();
        setLoggingStatus(this,true);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY",-1000f);
        animator.setDuration(800);
        animator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);//will change
                intent.putExtra("connectServer", true);
                startActivity(intent);
                new Thread(new Runnable()//在后台线程中关闭此活动
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(0);
                            LoginActivity.this.finish();
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        animator.start();

    }

    /**
     * 记录用户是否登录
     */
    private void saveLogStatus()
    {
        SharedPreferences sps = getSharedPreferences("userLogStatus",MODE_PRIVATE);
        SharedPreferences.Editor editor = sps.edit();
        editor.putBoolean("LogStatus", true);
        editor.apply();

    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public String getUsername(){
        String get_username = username;
        return get_username;
    }
}
