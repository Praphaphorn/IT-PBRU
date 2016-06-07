package pbru.prahaphorn.itpbru;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class MainActivity extends AppCompatActivity {


    private MyManager myManager;
    private static final String urlJSON = "http://swiftcodingthai.com/pbru2/get_user_master.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myManager = new MyManager(this);
        //test Add Neq\w User
        //myManager.addNewUser("123","name","sur","user","pass");

        //Delete All SQLite
        deleteAllSQLite();

        mySynJSON();
    } // Maim Method

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
