package test.com.badman.masque;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewMemberActivity extends AppCompatActivity
{

    private   String Area ;
    private android.support.v7.widget.Toolbar mToolbar;
    private EditText Name,Address,Email,AmountofMoney,Mobile;
    private Button submitButton;

    private FirebaseAuth mAuth;
    private DatabaseReference storeUserDefaultDataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_member);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //  extras = extras.getBundle("KEY_EXTRAS");
            String status = extras.getString("area");
           // String code = extras.getString("Code");
            Area=status;
          //  AreaCode=code;
        }


            mToolbar=findViewById(R.id.apply2_toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("parsonal Info");
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true)


            submitButton = findViewById(R.id.submit_button);
            Name = findViewById(R.id.user_name);
            Address = findViewById(R.id.user_address);
            Email = findViewById(R.id.user_email);
            AmountofMoney =findViewById(R.id.amount_of_money);
            Mobile =findViewById(R.id.mobile);

            mAuth = FirebaseAuth.getInstance();

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String NAME = Name.getText().toString();
                    final String ADDESSS = Address.getText().toString();
                    final String EMAIL = Email.getText().toString();
                    final String AMOUNT = AmountofMoney.getText().toString();

                    final String MOBILE = Mobile.getText().toString();


                    if (TextUtils.isEmpty(NAME))
                    {
                        Toast.makeText(AddNewMemberActivity.this, "please fill the name", Toast.LENGTH_SHORT).show();
                    }

                    else if(TextUtils.isEmpty(AMOUNT))
                    {
                        Toast.makeText(AddNewMemberActivity.this,"please fill amount",Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(MOBILE))
                    {
                        Toast.makeText(AddNewMemberActivity.this,"please fill the mobile numbet",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        sendValue(NAME,ADDESSS,EMAIL,AMOUNT,MOBILE);


                        // Intent applyIntent = new Intent(AddNewMemberActivity.this,AddProfileImage.class);
                        Intent intent = new Intent(AddNewMemberActivity.this,AddProfileImage.class);
                        intent.putExtra("name",NAME);
                        intent.putExtra("mobile",MOBILE);
                        intent.putExtra("area",Area);
                        startActivity(intent);
                    }



                }
            });



        }

        private void sendValue(String userName, String userAddress, String userEmail, String userAmount, String userMobile)
        {
            String sub_area= Area;
           // String current_user_Id = mAuth.getCurrentUser().getUid();
            storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("area_member").child(sub_area).child(userName+"_"+userMobile);

            storeUserDefaultDataReference.child("name").setValue(userName);
            storeUserDefaultDataReference.child("address").setValue(userAddress);
            storeUserDefaultDataReference.child("email").setValue(userEmail);
            storeUserDefaultDataReference.child("paid").setValue("not paid");
            storeUserDefaultDataReference.child("amountofmoney").setValue(userAmount);
            storeUserDefaultDataReference.child("id").setValue(userName+"_"+userMobile);
            storeUserDefaultDataReference.child("mobile").setValue(userMobile);



        }


    }
