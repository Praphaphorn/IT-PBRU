package pbru.prahaphorn.itpbru;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    private MyManager myManager;
    private static final String urlJSON = "http://swiftcodingthai.com/pbru2/get_user_master.php";
    private EditText userEditText, passwordEditText;
    private String userString, passwordString; //มีสติงในการรับค่า
    private String[] loginStrings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        userEditText = (EditText) findViewById(R.id.editText5);
        passwordEditText = (EditText) findViewById(R.id.editText6);

        myManager = new MyManager(this);
        //test Add Neq\w User
        //myManager.addNewUser("123","name","sur","user","pass");

        //Delete All SQLite
        deleteAllSQLite();

        mySynJSON();
    } // Maim Method

    public void clickSignIn(View view) {

        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        if (userString.equals("")|| passwordString.equals("")) {
            MyAlert myAlert =new MyAlert();
            myAlert.myDialog(this, "Have Space","Please Fill All Every Blank");

        } else {

            CheckUserAnPassword();
        }

    }// ClickSignIn

    private void CheckUserAnPassword() {

        try {

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);

            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM userTABLE WHERE User= " + "'" + userString + "'", null);
            cursor.moveToFirst();

            loginStrings = new String[cursor.getColumnCount()];
            for (int i =0 ; i<cursor.getColumnCount(); i++) {

                loginStrings[i] = cursor.getString(i);
            }
            cursor.close();
            //check Password
            if (passwordString.equals(loginStrings[4])) {
                Toast.makeText(this, "Welcome" + loginStrings[1] + " " + loginStrings[2],
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                intent.putExtra("Login", loginStrings);
                startActivity(intent);
                finish();

            } else {
                MyAlert myAlert = new MyAlert();
                myAlert.myDialog(this,"Password False","Please Try Again Password False");
            }

        } catch (Exception e) {
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this,"ไม่มี User นี้ ?","ไม่มี" + userString + "ในฐานข้อมูลของเรา");

        }
    }//CheckUser

    private void mySynJSON() {
        ConnectUserTABLE connectUserTABLE = new ConnectUserTABLE(this);
        connectUserTABLE.execute();
    }

    //Create Inner Class
    private class ConnectUserTABLE extends AsyncTask<Void, Void, String> {
        private Context context;
        private ProgressDialog progressDialog;


        public ConnectUserTABLE(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(context,"Synchronize Server",
                    "Please Wait...Process Synchronize");


        }// onPre

        @Override
        protected String doInBackground(Void... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlJSON).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("7June","error DoIn ==>" + e.toString());
                return null;
            }

        } // DoInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                progressDialog.dismiss();
                Log.d("7June","JSON ==> " + s);


                JSONArray jsonArray = new JSONArray(s);

                String[] idStrings = new String[jsonArray.length()];
                String[] nameStrings = new String[jsonArray.length()];
                String[] surnameStrings = new String[jsonArray.length()];
                String[] userStrings = new String[jsonArray.length()];
                String[] passwordStrings = new String[jsonArray.length()];

                for (int i=0; i<jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    idStrings[i] = jsonObject.getString("id");
                    nameStrings[i] = jsonObject.getString(MyManager.column_name);
                    surnameStrings[i] = jsonObject.getString(MyManager.column_surname);
                    userStrings[i] = jsonObject.getString(MyManager.column_user);
                    passwordStrings[i] = jsonObject.getString(MyManager.column_password);

                    myManager.addNewUser(idStrings[i],nameStrings[i],surnameStrings[i],
                            userStrings[i],passwordStrings[i]);

                } // for

            } catch (Exception e) {
                e.printStackTrace();
            }
        }//onPost
    }//connected Class


    private void deleteAllSQLite() {

        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                MODE_PRIVATE, null);
        sqLiteDatabase.delete(MyManager.user_table, null, null);


    }




    public void ClickSignUpMain(View view){
        startActivity(new Intent(MainActivity.this,
                SignUpActivity.class));


    }
} //Main Class
