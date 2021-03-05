package com.example.login1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;

public class MainActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;

   // public void List_Cat(View view) {
     //   startActivity(new Intent(MainActivity.this,ListCategory.class));
    //}

    class SignupTask extends AsyncTask<String,Void,Boolean>
    {

        @Override
        protected Boolean doInBackground(String... params) {
            String username=params[0];
            String password=params[1];

            try{
                String result=ServiceClient.get("/Signup?username="+username+"&password="+password);
                JSONObject object=new JSONObject(result);
                String status=object.getString("status");
                if("success".equals(status))
                {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;

        }
        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {

                //start next activity
                Toast.makeText(MainActivity.this,"SignUp Successful",Toast.LENGTH_LONG).show();
            }
            else
            {
                //show error message
                Toast.makeText(MainActivity.this,"SignUp Failed",Toast.LENGTH_LONG).show();
            }
        }
    }
    class  LoginTask extends AsyncTask<String,Void,Boolean>
    {

        @Override
        protected Boolean doInBackground(String... params) {
            String username=params[0];
            String password=params[1];

            try {
                String result=ServiceClient.get("/Login?username="+username+"&password="+password);
                System.out.println("LOGIN RESULT"+result);
                JSONObject object=new JSONObject(result);
                String status=object.getString("status");
                if("success".equals(status))
                {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {

                //start next activity
                Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, ListCategory.class);
                startActivity(intent);
            }
            else
            {
                //show error message
                Toast.makeText(MainActivity.this,"Login Failed",Toast.LENGTH_LONG).show();
            }
        }
    }

    Button button;
    Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.button2);
        button = (Button) findViewById(R.id.button);
        et_username=(EditText)findViewById(R.id.et_username);
        et_password=(EditText)findViewById(R.id.et_password);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"SignUp",Toast.LENGTH_LONG).show();
                String username=et_username.getText().toString();
                String password=et_password.getText().toString();
                if(Patterns.EMAIL_ADDRESS.matcher(username).matches())
                {
                    new SignupTask().execute(username,password);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Enter valid email",Toast.LENGTH_LONG).show();
                }

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this,"Loggin In",Toast.LENGTH_LONG).show();

                String username=et_username.getText().toString();
                String password=et_password.getText().toString();
                new LoginTask().execute(username,password);
                //new List_cat().execute();

            }

           /* public void login(String username, String password) {
                // can be launched in a separate asynchronous job
//        Result<LoggedInUser> result = loginRepository.login(username, password);
                Log.d("Login", "Logging in");
                try {
                    String response = ServiceClient.get("/login?username=" + username + "&password=" + password);
                    Log.d("Login", response);
                    JSONObject ob = new JSONObject(response);
                    String status = ob.getString("status");
                    //  if (status.equalsIgnoreCase("success")) {
                    //       loginResult.setValue(new LoginResult(new LoggedInUserView(username)));
                    //  } else {
                    //      loginResult.setValue(new LoginResult(R.string.login_failed));
                    // }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }*/
        });
    }
}