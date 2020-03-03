package test.com.badman.masque;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class AddSubAdminActivity extends AppCompatActivity
{

    private   String Area ;
    private android.support.v7.widget.Toolbar mToolbar;
    private EditText Name,Code,Email,AmountofMoney,Mobile;
    private Button submitButton;

    private FirebaseAuth mAuth;
    private DatabaseReference storeUserDefaultDataReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_admin);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //  extras = extras.getBundle("KEY_EXTRAS");
            String status = extras.getString("area");
            // String code = extras.getString("Code");
            Area=status;
            //  AreaCode=code;
        }


        mToolbar=findViewById(R.id.add_sub_admin_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sub Admin Info");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true)


        submitButton = findViewById(R.id.submit_button);
        Name = findViewById(R.id.sub_user_name);
        Code = findViewById(R.id.sub_user_code);


        mAuth = FirebaseAuth.getInstance();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String NAME = Name.getText().toString();
                final String CODE = Code.getText().toString();



                sendValue(NAME,CODE);


                // Intent applyIntent = new Intent(AddNewMemberActivity.this,AddProfileImage.class);
                Intent intent = new Intent(AddSubAdminActivity.this,MainActivity.class);
//                intent.putExtra("name",NAME);
//                intent.putExtra("mobile",MOBILE);
//                intent.putExtra("area",Area);
                startActivity(intent);

            }
        });



    }

    private void sendValue(String userName, String userCode)
    {
        String sub_area= Area;



        long currentTime = Calendar.getInstance().getTimeInMillis();
        String s2 = Long.toString(currentTime);
        // String current_user_Id = mAuth.getCurrentUser().getUid();
       // String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("subadmin").child(s2+userCode+userName);

        storeUserDefaultDataReference.child("name").setValue(userName);
        storeUserDefaultDataReference.child("code").setValue(userCode);
        storeUserDefaultDataReference.child("area").setValue(sub_area);
        storeUserDefaultDataReference.child("id").setValue(s2+userCode+userName);



    }


}
