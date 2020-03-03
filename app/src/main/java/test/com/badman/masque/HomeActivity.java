package test.com.badman.masque;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity
{
    private CardView Search ;
    private TextView addMember;
    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Member, SubViewHolder> mPeopleRVAdapter;
//    Dialog dialog;
    private String Area;
    private String AreaCode;
    private String ADMIN="a";
    Dialog dialog;
    private String ClearPaid;
    final int Request_Call = 1;
    String Sub_admin_name;
    private  String Sub_admin_id;
    private String finalAmountofMoney;
    private String finalAmountofMoney1;

   // private String Area;
    private FirebaseAuth mAuth;
    private DatabaseReference storeUserDefaultDataReference;
    private DatabaseReference secondDataStore,paid_history;
    private DatabaseReference totalpaidinmonth,subadminhistory;


  //  private FirebaseAuth mAuth;
    private DatabaseReference getUserDataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




        Bundle extras = getIntent().getExtras();
        if (extras != null) {
          //  extras = extras.getBundle("KEY_EXTRAS");
            String status = extras.getString("area");
            String code = extras.getString("Code");
            String admin = extras.getString("admin");
            final String sub_admin_name = extras.getString("sub_admin_name");
            final String sub_admin_id = extras.getString("sub_admin_id");
            Area=status;
            AreaCode=code;
            ADMIN =admin;
            Sub_admin_name=sub_admin_name;
            Sub_admin_id=sub_admin_id;

        }


        Toast.makeText(this, Area, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, Sub_admin_id, Toast.LENGTH_LONG).show();

//        final Intent intent = getIntent();
//        if (intent != null)
//        {
//            String name = intent.getExtras().getString("name");
//            final String id = intent.getExtras().getString("id");
//        }
        mToolbar=findViewById(R.id.home_toolbar);
        mToolbar.setTitle("Members");
        setSupportActionBar(mToolbar);


        mAuth = FirebaseAuth.getInstance();

        addMember = findViewById(R.id.add_member);
        Search =findViewById(R.id.members_search);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(HomeActivity.this,SearchActivity.class));
                Intent intent = new Intent(HomeActivity.this,SearchActivity.class);
                intent.putExtra("area", Area);
                startActivity(intent);
            }
        });
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(HomeActivity.this,AddNewMemberActivity.class));
                if (ADMIN.equals("sub_admin"))
                {
                    Intent intent = new Intent(HomeActivity.this,AddNewMemberActivity.class);
                    intent.putExtra("area", Area);
                    startActivity(intent);
                }
                else
                    {
                        dialog = new Dialog(HomeActivity.this);
                        dialog.setContentView(R.layout.subadmin_member);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Button  newSubamdin =      dialog.findViewById(R.id.new_subadmin);
                        Button newMember =     dialog.findViewById(R.id.new_member);

                        dialog.show();

                        newSubamdin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(HomeActivity.this,AddSubAdminActivity.class);
                                intent.putExtra("area", Area);
                                startActivity(intent);
                            }
                        });
                        newMember.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeActivity.this,AddNewMemberActivity.class);
                                intent.putExtra("area", Area);
                                startActivity(intent);
                            }
                        });
                       // Toast.makeText(HomeActivity.this, "admin", Toast.LENGTH_SHORT).show();

                    }

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("area_member").child(Area);
        mDatabase.keepSynced(true);

        mPeopleRV =  findViewById(R.id.home_recycleview);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("area_member").child(Area);
        Query personsQuery = personsRef.orderByKey();

        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Member>().setQuery(personsQuery, Member.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Member, HomeActivity.SubViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(HomeActivity.SubViewHolder holder, final int position, final Member model) {
                holder.setName(model.getName());
                holder.setAddress(model.getAddress());
                holder.setId(model.getAmountofmoney());
                holder.setMobile(model.getMobile());
                holder.setImage(model.getImage());




                Date today = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(today);

                int month = cal.get(Calendar.MONTH);
              //  String month = String.valueOf(cal.get(Calendar.MONTH));
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

                String PAid ="Paid_"+year+"_"+monthString;
                if (PAid.equals(model.getPaid()))
                {
                   // holder.setEx(model.getPaid());
                    ClearPaid = model.getPaid();
                    holder.setReceived(model.getReceivedby());
                }
                else
                {
                    ClearPaid = "Unpaid";
                }
                holder.setEx(ClearPaid);
              //  final String current_user_Id = mAuth.getCurrentUser().getUid();
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        final String Name = model.getName();
                      final   String Id = model.getId();
                        String address =model.getAddress();
                        final String mobile=model.getMobile();
                        String image =model.getImage();

                      final   String AmountofMoney=model.getAmountofmoney();
                      //  String Area =model.

//                        Intent intent = new Intent(HomeActivity.this,MemberProfileActivity.class);
//                        intent.putExtra("name", Name);
//                        intent.putExtra("id",Id);
//                        intent.putExtra("area", Area);
//                        startActivity(intent);


                        if (ADMIN.equals("sub_admin"))
                        {
                            dialog = new Dialog(HomeActivity.this);
                            dialog.setContentView(R.layout.dialog_member);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView dialog_name =      dialog.findViewById(R.id.dialog_name);
                            Button PAID_MONEY =     dialog.findViewById(R.id.money_paid);
                            TextView  dialog_des =      dialog.findViewById(R.id.dialog_des);
                            Button  dialog_mobile =   dialog.findViewById(R.id.dialog_mobile);
                            ImageView   dialog_pic =    dialog.findViewById(R.id.dialog_pic);
                            final EditText addextramonet =         dialog.findViewById(R.id.add_extra_monet);


                            dialog_name.setText(Name);
                            // dialog_email.setText(ClearPaid);
                            dialog_des.setText(mobile);
                            // dialog_mobile.setText(mobile);
                            Picasso.get().load(image).into(dialog_pic);
                            dialog.show();

                            dialog_mobile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(HomeActivity.this,PaidHistoryActivity.class);
                                    intent.putExtra("area", Area);
                                    intent.putExtra("id", Id);
                                    startActivity(intent);
                                }
                            });

                            dialog_des.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ContextCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED)
                                    {
                                        ActivityCompat.requestPermissions((Activity) HomeActivity.this,new String[]{Manifest.permission.CALL_PHONE},Request_Call);
                                    }
                                    else {
                                        Intent intent1 =new Intent(Intent.ACTION_CALL);
                                        intent1.setData(Uri.parse("tel:"+mobile));
                                        startActivity(intent1);
                                    }
                                }
                            });




                            PAID_MONEY.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    dialog = new Dialog(HomeActivity.this);
                                    dialog.setContentView(R.layout.yes_no);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    Button  newYes =      dialog.findViewById(R.id.new_yes);
                                    Button newNo =     dialog.findViewById(R.id.new_no);


                                    dialog.show();
                                    final String ExtraAMOUNT = addextramonet.getText().toString();
                                    if (TextUtils.isEmpty(ExtraAMOUNT))
                                    {
                                        Toast.makeText(HomeActivity.this, " No Extra Money", Toast.LENGTH_SHORT).show();
                                        finalAmountofMoney = AmountofMoney;
                                    }
                                    else {
                                        int i = Integer.parseInt(ExtraAMOUNT);
                                        int j = Integer.parseInt(AmountofMoney);

                                        int k =i+j;
                                        String s=String.valueOf(k);
                                       // AmountofMoney=s;
                                         finalAmountofMoney = s;

                                    }


                                    newNo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    newYes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {



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
                                                // Toast.makeText(MemberProfileActivity.this, id, Toast.LENGTH_SHORT).show();
                                                sendValue(Id);
                                                dialog.dismiss();


                                            }
                                            else
                                            {
                                                Toast.makeText(HomeActivity.this, "error", Toast.LENGTH_SHORT).show();
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
                                    final String year = String.valueOf(cal.get(Calendar.YEAR));

                                    final String monthString;
                                    switch (month) {
                                        case 0:
                                            monthString = "Jan";
                                            break;
                                        case 1:
                                            monthString = "Feb";
                                            break;
                                        case 2:
                                            monthString = "Mar";
                                            break;
                                        case 3:
                                            monthString = "Apr";
                                            break;
                                        case 4:
                                            monthString = "May";
                                            break;
                                        case 5:
                                            monthString = "Jun";
                                            break;
                                        case 6:
                                            monthString = "Jul";
                                            break;
                                        case 7:
                                            monthString = "Aug";
                                            break;
                                        case 8:
                                            monthString = "Sep";
                                            break;
                                        case 9:
                                            monthString = "Oct";
                                            break;
                                        case 10:
                                            monthString = "Nov";
                                            break;
                                        case 11:
                                            monthString = "Dec";
                                            break;
                                        default:
                                            monthString = "Invalid";
                                            break;
                                    }


                                    long currentTime = Calendar.getInstance().getTimeInMillis();
                                    String s2 = Long.toString(currentTime);

                                    //Toast.makeText(this, monthString, Toast.LENGTH_SHORT).show();
                                    // String current_user_Id = mAuth.getCurrentUser().getUid();
                                    storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("paid")
                                            .child(year + "_" + monthString).child(year + "_" + monthString + "_" + id);
                                    secondDataStore = FirebaseDatabase.getInstance().getReference().child("area_member").child(Area).child(id);
                                    paid_history = FirebaseDatabase.getInstance().getReference().child("paid_history").child(Area).child(id).child(s2+monthString+year);
                                    totalpaidinmonth = FirebaseDatabase.getInstance().getReference().child("total_paid_month").child(Area).child(year+monthString);
                                    subadminhistory = FirebaseDatabase.getInstance().getReference().child("sub_paid_month").child(Sub_admin_id).child(year+monthString);


                                    storeUserDefaultDataReference.child("id").setValue(id);
                                    storeUserDefaultDataReference.child("year").setValue(year);
                                    storeUserDefaultDataReference.child("month").setValue(monthString);
                                    storeUserDefaultDataReference.child("taka").setValue("paid");
                                    secondDataStore.child("receivedby").setValue(Sub_admin_name);
                                    secondDataStore.child("paid").setValue("Paid_" + year + "_" + monthString);

                                    paid_history.child("id").setValue(id);
                                    paid_history.child("receivedby").setValue(Sub_admin_name);
                                    paid_history.child("taka").setValue(finalAmountofMoney);
                                    paid_history.child("paid").setValue(year + "_" + monthString);
                                    // storeUserDefaultDataReference.child(""balance").setValue(taka);
                                    Toast.makeText(HomeActivity.this, "save", Toast.LENGTH_SHORT).show();

//                                    totalpaidinmonth.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//                                        {
//                                            if (dataSnapshot.hasChild("total")) {
//                                                String total = dataSnapshot.child("total").getValue().toString();
//                                                String month_year = dataSnapshot.child("month_year").getValue().toString();
//
////
//                                                int i = Integer.parseInt(total);
//                                                int j = Integer.parseInt(finalAmountofMoney);
//
//                                                int k =i+j;
//                                                String s=String.valueOf(k);
//
//
//
//                                                totalpaidinmonth.child("total").setValue(s);
//                                                totalpaidinmonth.child("month_year").setValue(year+monthString);
//                                                Toast.makeText(HomeActivity.this, "if", Toast.LENGTH_LONG).show();
//
//                                            }
//                                            else {
//                                                Toast.makeText(HomeActivity.this, "else", Toast.LENGTH_SHORT).show();
//                                                totalpaidinmonth.child("total").setValue(finalAmountofMoney);
//                                                totalpaidinmonth.child("month_year").setValue(year+monthString);
//
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                        }
//                                    });

                                    totalpaidinmonth.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                        {
                                            if (dataSnapshot.hasChild("monthly_total")) {
                                                String total = dataSnapshot.child("monthly_total").getValue().toString();
                                                String month_year = dataSnapshot.child("month_year").getValue().toString();

//
                                                int i = Integer.parseInt(total);
                                                int j = Integer.parseInt(finalAmountofMoney);

                                                int k =i+j;
                                                String s=String.valueOf(k);



                                                totalpaidinmonth.child("monthly_total").setValue(s);
                                                totalpaidinmonth.child("month_year").setValue(year+monthString);
                                                Toast.makeText(HomeActivity.this, "if", Toast.LENGTH_LONG).show();

                                            }
                                            else {
                                                Toast.makeText(HomeActivity.this, "else", Toast.LENGTH_SHORT).show();
                                                totalpaidinmonth.child("monthly_total").setValue(finalAmountofMoney);
                                                totalpaidinmonth.child("month_year").setValue(year+monthString);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                    subadminhistory.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                        {
                                            if (dataSnapshot.hasChild("monthly_total")) {
                                                String total = dataSnapshot.child("monthly_total").getValue().toString();
                                                String month_year = dataSnapshot.child("month_year").getValue().toString();

//
                                                int i = Integer.parseInt(total);
                                                int j = Integer.parseInt(finalAmountofMoney);

                                                int k =i+j;
                                                String s=String.valueOf(k);



                                                subadminhistory.child("monthly_total").setValue(s);
                                                subadminhistory.child("month_year").setValue(year+monthString);
                                                Toast.makeText(HomeActivity.this, "if", Toast.LENGTH_LONG).show();

                                            }
                                            else {
                                                Toast.makeText(HomeActivity.this, "else", Toast.LENGTH_SHORT).show();
                                                subadminhistory.child("monthly_total").setValue(finalAmountofMoney);
                                                subadminhistory.child("month_year").setValue(year+monthString);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });






                                }
                            });

                        }
                        else
                        {
                            dialog = new Dialog(HomeActivity.this);
                            dialog.setContentView(R.layout.dialog_admin_member);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView dialog_name =      dialog.findViewById(R.id.dialog_names);
                            Button PAID_MONEY =     dialog.findViewById(R.id.money_paids);
                            TextView  dialog_des =      dialog.findViewById(R.id.dialog_dess);
                            TextView  dialog_mobile =   dialog.findViewById(R.id.dialog_mobiles);
                            ImageView   dialog_pic =    dialog.findViewById(R.id.dialog_pics);
                            final EditText addextramonet =         dialog.findViewById(R.id.add_extra_monets);

                            Button Delete = dialog.findViewById(R.id.delete_member);


                            dialog_name.setText(Name);
                            // dialog_email.setText(ClearPaid);
                            dialog_des.setText(mobile);
                            // dialog_mobile.setText(mobile);
                            Picasso.get().load(image).into(dialog_pic);
                            dialog.show();

                            dialog_mobile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(HomeActivity.this,PaidHistoryActivity.class);
                                    intent.putExtra("area", Area);
                                    intent.putExtra("id", Id);
                                    startActivity(intent);
                                }
                            });

                            dialog_des.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ContextCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED)
                                    {
                                        ActivityCompat.requestPermissions((Activity) HomeActivity.this,new String[]{Manifest.permission.CALL_PHONE},Request_Call);
                                    }
                                    else {
                                        Intent intent1 =new Intent(Intent.ACTION_CALL);
                                        intent1.setData(Uri.parse("tel:"+mobile));
                                        startActivity(intent1);
                                    }
                                }
                            });

                            Delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog = new Dialog(HomeActivity.this);
                                    dialog.setContentView(R.layout.delete);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    Button  deleteYes =      dialog.findViewById(R.id.delete_yes);
                                    Button  deleteNo =     dialog.findViewById(R.id.delete_no);
                                    dialog.show();

                                    deleteYes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("area_member").child(Area).child(Id).removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(HomeActivity.this, "delete", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(HomeActivity.this, "error", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                            dialog.dismiss();
                                        }
                                    });

                                    deleteNo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });

                                    //dialog.dismiss();
                                }
                            });



                            PAID_MONEY.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    dialog = new Dialog(HomeActivity.this);
                                    dialog.setContentView(R.layout.yes_no);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    Button  newYes =      dialog.findViewById(R.id.new_yes);
                                    Button newNo =     dialog.findViewById(R.id.new_no);


                                    dialog.show();
                                    final String ExtraMOney = addextramonet.getText().toString();

                                    if (TextUtils.isEmpty(ExtraMOney))
                                    {
                                        Toast.makeText(HomeActivity.this, " No Extra Money", Toast.LENGTH_SHORT).show();
                                        finalAmountofMoney1 = AmountofMoney;
                                    }
                                    else {
                                        int i = Integer.parseInt(ExtraMOney);
                                        int j = Integer.parseInt(AmountofMoney);

                                        int k =i+j;
                                        String s=String.valueOf(k);
                                        //AmountofMoney=s;
                                         finalAmountofMoney1 = s;

                                    }




                                    newNo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();

                                        }
                                    });
                                    newYes.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v) {



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
                                                // Toast.makeText(MemberProfileActivity.this, id, Toast.LENGTH_SHORT).show();
                                                sendValue(Id);
                                                dialog.dismiss();


                                            }
                                            else
                                            {
                                                Toast.makeText(HomeActivity.this, "error", Toast.LENGTH_SHORT).show();
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
                                    final String year = String.valueOf(cal.get(Calendar.YEAR));

                                    final String monthString;
                                    switch (month) {
                                        case 0:
                                            monthString = "Jan";
                                            break;
                                        case 1:
                                            monthString = "Feb";
                                            break;
                                        case 2:
                                            monthString = "Mar";
                                            break;
                                        case 3:
                                            monthString = "Apr";
                                            break;
                                        case 4:
                                            monthString = "May";
                                            break;
                                        case 5:
                                            monthString = "Jun";
                                            break;
                                        case 6:
                                            monthString = "Jul";
                                            break;
                                        case 7:
                                            monthString = "Aug";
                                            break;
                                        case 8:
                                            monthString = "Sep";
                                            break;
                                        case 9:
                                            monthString = "Oct";
                                            break;
                                        case 10:
                                            monthString = "Nov";
                                            break;
                                        case 11:
                                            monthString = "Dec";
                                            break;
                                        default:
                                            monthString = "Invalid";
                                            break;
                                    }


                                    long currentTime = Calendar.getInstance().getTimeInMillis();
                                    String s2 = Long.toString(currentTime);

                                    //Toast.makeText(this, monthString, Toast.LENGTH_SHORT).show();
                                    // String current_user_Id = mAuth.getCurrentUser().getUid();
                                    storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("paid")
                                            .child(year + "_" + monthString).child(year + "_" + monthString + "_" + id);

                                    secondDataStore = FirebaseDatabase.getInstance().getReference().child("area_member").child(Area).child(id);
                                    paid_history = FirebaseDatabase.getInstance().getReference().child("paid_history").child(Area).child(id).child(s2+monthString+year);
                                    totalpaidinmonth = FirebaseDatabase.getInstance().getReference().child("total_paid_month").child(Area).child(year+monthString);
                                    subadminhistory = FirebaseDatabase.getInstance().getReference().child("sub_paid_month").child(Sub_admin_id).child(year+monthString);

                                    storeUserDefaultDataReference.child("id").setValue(id);
                                    storeUserDefaultDataReference.child("year").setValue(year);
                                    storeUserDefaultDataReference.child("month").setValue(monthString);
                                    storeUserDefaultDataReference.child("taka").setValue("paid");
                                    secondDataStore.child("receivedby").setValue(Sub_admin_name);
                                    secondDataStore.child("paid").setValue("Paid_" + year + "_" + monthString);

                                    paid_history.child("id").setValue(id);
                                    paid_history.child("receivedby").setValue(Sub_admin_name);
                                    paid_history.child("taka").setValue(finalAmountofMoney1);

                                    // storeUserDefaultDataReference.child(""balance").setValue(taka);
                                    Toast.makeText(HomeActivity.this, "save", Toast.LENGTH_SHORT).show();

                                    totalpaidinmonth.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                        {
                                            if (dataSnapshot.hasChild("monthly_total")) {
                                                String total = dataSnapshot.child("monthly_total").getValue().toString();
                                                //String month_year = dataSnapshot.child("month_year").getValue().toString();

//
                                                int i = Integer.parseInt(total);
                                                int j = Integer.parseInt(finalAmountofMoney1);

                                                int k =i+j;
                                                String s=String.valueOf(k);



                                                totalpaidinmonth.child("monthly_total").setValue(s);
                                                totalpaidinmonth.child("month_year").setValue(year+monthString);
                                                Toast.makeText(HomeActivity.this, "if", Toast.LENGTH_LONG).show();

                                            }
                                            else {
                                                Toast.makeText(HomeActivity.this, "else", Toast.LENGTH_SHORT).show();
                                                totalpaidinmonth.child("monthly_total").setValue(finalAmountofMoney);
                                                totalpaidinmonth.child("month_year").setValue(year+monthString);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });

                                    subadminhistory.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                        {
                                            if (dataSnapshot.hasChild("monthly_total")) {
                                                String total = dataSnapshot.child("monthly_total").getValue().toString();
                                                //String month_year = dataSnapshot.child("month_year").getValue().toString();

//
                                                int i = Integer.parseInt(total);
                                                int j = Integer.parseInt(finalAmountofMoney1);

                                                int k =i+j;
                                                String s=String.valueOf(k);



                                                subadminhistory.child("monthly_total").setValue(s);
                                                subadminhistory.child("month_year").setValue(year+monthString);
                                                Toast.makeText(HomeActivity.this, "if", Toast.LENGTH_LONG).show();

                                            }
                                            else {
                                                Toast.makeText(HomeActivity.this, "else", Toast.LENGTH_SHORT).show();
                                                subadminhistory.child("monthly_total").setValue(finalAmountofMoney);
                                                subadminhistory.child("month_year").setValue(year+monthString);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });



                                }
                            });

                        }









                    }
                });
             //   holder.setCode(model.getCode());
                //  holder.setDesignation(model.getCode());
//                holder.setEmail(model.getEmail());
//                holder.setMobile(model.getMobile());
//                holder.setPhone(model.getPhone());
//                holder.setImage(getBaseContext(), model.getImage());
            }


            @Override
            public HomeActivity.SubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.profile_rows, parent, false);

                return new HomeActivity.SubViewHolder(view);
            }
        };

        mPeopleRV.setAdapter(mPeopleRVAdapter);



    }

    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();


    }


    public static class SubViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public SubViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name)
        {
            TextView sub_admin = mView.findViewById(R.id.members_name);
            sub_admin.setText(name);
        }
        public void setAddress(String address)
        {
            TextView Address = mView.findViewById(R.id.members_address);
            Address.setText(address);
        }
        public void setId(String id)
        {
            TextView Id = mView.findViewById(R.id.members_email);
            Id.setText(id);
        }

        public void setEx(String ex)
        {
            TextView Ex = mView.findViewById(R.id.ex);
            Ex.setText(ex);
        }
        public void setMobile(String mobile)
        {
            TextView Mobile = mView.findViewById(R.id.members_mobile);
            Mobile.setText(mobile);
        }
        public void setReceived(String received)
        {
            TextView Received = mView.findViewById(R.id.received_by);
            Received.setText(received);
        }
        public void setImage(String image)
        {
            ImageView Image = mView.findViewById(R.id.members_profile_image);
            Picasso.get().load(image).into(Image);
        }
    }


}
