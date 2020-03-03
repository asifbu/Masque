package test.com.badman.masque;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class MemberProfileActivity extends AppCompatActivity {

    private String Area;
    private FirebaseAuth mAuth;
    private DatabaseReference storeUserDefaultDataReference;
    private DatabaseReference secondDataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);


        final Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        final String id   = intent.getExtras().getString("id");
        String area   = intent.getExtras().getString("area");
        Area=area;

       // mAuth = FirebaseAuth.getInstance();


        TextView member_name = findViewById(R.id.member_name);
        member_name.setText(name);
        Button PAID = findViewById(R.id.paid_check);
        Button Check = findViewById(R.id.check);
        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MemberProfileActivity.this,PaidCheckActivity.class));
            }
        });

        PAID.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Date today = Calendar.getInstance().getTime();
                Date today = new Date(); // Fri Jun 17 14:54:28 PDT 2016
                Calendar cal = Calendar.getInstance();
                cal.setTime(today); // don't forget this if date is arbitrary e.g. 01-01-2014

               // int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 6
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH); // 17
               // int dayOfYear = cal.get(Calendar.DAY_OF_YEAR); //169

                int month = cal.get(Calendar.MONTH); // 5
                int year =  cal.get(Calendar.YEAR);


                if (dayOfMonth>20 || dayOfMonth<10)
                {
                    Toast.makeText(MemberProfileActivity.this, id, Toast.LENGTH_SHORT).show();
                    sendValue(id);


                }
                else
                {
                    Toast.makeText(MemberProfileActivity.this, "error", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void sendValue(String id) {


        Date today = new Date(); // Fri Jun 17 14:54:28 PDT 2016
        Calendar cal = Calendar.getInstance();
        cal.setTime(today); // don't forget this if date is arbitrary e.g. 01-01-2014

        // int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 6
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH); // 17
        // int dayOfYear = cal.get(Calendar.DAY_OF_YEAR); //169

        int month = cal.get(Calendar.MONTH);
       // String month = String.valueOf(cal.get(Calendar.MONTH)); // 5
        String year = String.valueOf(cal.get(Calendar.YEAR));

        String monthString;
        switch (month) {
            case 0:  monthString = "Jan";        break;
            case 1:  monthString = "Feb";        break;
            case 2:  monthString = "Mar";        break;
            case 3:  monthString = "Apr";        break;
            case 4:  monthString = "May";        break;
            case 5:  monthString = "Jun";        break;
            case 6:  monthString = "Jul";        break;
            case 7:  monthString = "Aug";        break;
            case 8:  monthString = "Sep";        break;
            case 9: monthString = "Oct";        break;
            case 10: monthString = "Nov";        break;
            case 11: monthString = "Dec";        break;
            default: monthString = "Invalid";    break;
        }

        Toast.makeText(this, monthString, Toast.LENGTH_SHORT).show();
        // String current_user_Id = mAuth.getCurrentUser().getUid();
        storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("paid")
                .child(year+"_"+monthString).child(year+"_"+monthString+"_"+id);
        secondDataStore = FirebaseDatabase.getInstance().getReference().child("area_member").child(Area).child(id);

        storeUserDefaultDataReference.child("id").setValue(id);
        storeUserDefaultDataReference.child("year").setValue(year);
        storeUserDefaultDataReference.child("month").setValue(monthString);
        storeUserDefaultDataReference.child("taka").setValue("paid");
        secondDataStore.child("paid").setValue("Paid_"+year+"_"+monthString);
       // storeUserDefaultDataReference.child(""balance").setValue(taka);
        Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();

    }


    }
