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

    private FirebaseAuth mAuth;
    private DatabaseReference storeUserDefaultDataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);


        final Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        final String id   = intent.getExtras().getString("id");

       // mAuth = FirebaseAuth.getInstance();


        TextView member_name = findViewById(R.id.member_name);
        member_name.setText(name);
        Button PAID = findViewById(R.id.paid_check);

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
                int year = cal.get(Calendar.YEAR);

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

        int month = cal.get(Calendar.MONTH); // 5
        int year = cal.get(Calendar.YEAR);
        // String current_user_Id = mAuth.getCurrentUser().getUid();
        storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("paid").child(year+"_"+month);

        storeUserDefaultDataReference.child("id").setValue(id);
        storeUserDefaultDataReference.child("year").setValue(year);
        storeUserDefaultDataReference.child("month").setValue(month);
       // storeUserDefaultDataReference.child("taka").setValue(taka);
        Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();

    }


    }
