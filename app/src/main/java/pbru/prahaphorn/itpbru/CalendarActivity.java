package pbru.prahaphorn.itpbru;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = (CalendarView) findViewById(R.id.calendarView);

        //Active When Click Calender
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                confirmDialog(day,month,year);

            }
        });

    }

    private void confirmDialog(final int day, int month, int year) {


        String strDate = Integer.toString(day) +
                "/"  +Integer.toString(month+1)+
                "/" + Integer.toString(year);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.bird48);
        builder.setTitle(strDate);
        builder.setMessage("คุณต้องการ บันทึก รายรับ หรือ รายจ่าย ?");
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("รายรับ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(CalendarActivity.this,UploadAccount.class);
                intent.putExtra("Login", getIntent().getStringArrayExtra("Login"));
                intent.putExtra("InOut", 0);

                startActivity(intent);



                dialog.dismiss();
            }
        });
        builder.setNeutralButton("รายจ่าย", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(CalendarActivity.this, UploadAccount.class);
                intent.putExtra("Login",getIntent().getStringArrayExtra("Login"));
                intent.putExtra("InOut", 1);
                startActivity(intent);

                dialog.dismiss();
            }
        });
        builder.show();

    }//confirmDialog
}
