package test.com.badman.masque;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;
import java.util.Date;

import okhttp3.Route;

public class PaidFragment extends Fragment
{

    private View v;
    private RecyclerView deptRecycleView;
    private DatabaseReference deptDatabase;
    private FirebaseRecyclerAdapter<Paid, PaidViewHolder> mPeopleRVAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        Date today = new Date(); // Fri Jun 17 14:54:28 PDT 2016
        Calendar cal = Calendar.getInstance();
        cal.setTime(today); // don't forget this if date is arbitrary e.g. 01-01-2014

        // int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 6
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH); // 17
        // int dayOfYear = cal.get(Calendar.DAY_OF_YEAR); //169

        int month = cal.get(Calendar.MONTH); // 5
        int year = cal.get(Calendar.YEAR);

        v= inflater.inflate(R.layout.fragment_paid, container, false);
        deptDatabase = FirebaseDatabase.getInstance().getReference().child("paid")
                .child(year+"_"+month);
        deptDatabase.keepSynced(true);

        deptRecycleView =  v.findViewById(R.id.paid_recycle);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("paid")
                .child(year+"_"+month);
        Query personsQuery = personsRef.orderByKey();

        deptRecycleView.hasFixedSize();
        deptRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Paid>().setQuery(personsQuery, Paid.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Paid, PaidFragment.PaidViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(PaidFragment.PaidViewHolder holder, final int position, final Paid model) {
                String id = "MEM0007";
                String ids=model.getId();
               if (id.equals(ids))
               {
                   holder.setTime(ids);
               }
               else
                   holder.setTime("not");


               // holder.setBus(model.getMonth());
               // holder.setPlace(model.getYear());


                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        final String d_id = model.getD_id();
//                        final String name = model.getName();
//                        Intent intent = new Intent(getContext(), ScienceActivity.class);
//                        intent.putExtra("id", d_id);
//                        intent.putExtra("name", name);
//                        intent.putExtra("key",key);
//                        startActivity(intent);
                    }
                });
            }

            @Override
            public PaidFragment.PaidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.sub_admin_rows, parent, false);
                PaidFragment.PaidViewHolder viewHolder =new PaidFragment.PaidViewHolder(view);
                return viewHolder;


            }
        };

        deptRecycleView.setAdapter(mPeopleRVAdapter);
        return v;
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

    public static class PaidViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public PaidViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setTime(String time){
            TextView bus_time = (TextView)mView.findViewById(R.id.sub_admin_name);
            bus_time.setText(time);
        }


    }

}
