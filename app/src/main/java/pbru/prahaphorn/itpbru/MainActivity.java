package pbru.prahaphorn.itpbru;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    private MyManager myManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myManager = new MyManager(this);
        //test Add Neq\w User
        //myManager.addNewUser("123","name","sur","user","pass");

        //Delete All SQLite
        deleteAllSQLite();
    } // Maim Method

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
