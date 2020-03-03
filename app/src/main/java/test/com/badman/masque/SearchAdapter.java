package test.com.badman.masque;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>
{
    Context context;
    ArrayList<String> name;
    ArrayList<String> address;
    ArrayList<String> mobile;
    ArrayList<String> id;
    ArrayList<String> paid;
  //  ArrayList<String> amount;
   ArrayList<String> image;
    Dialog dialog;
    final int Request_Call = 1;

    class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView nimage;
        TextView nname, naddress,nmobile,npaid,nid,namount;
        TextView dialog_name,dialog_des,dialog_mobile,dialog_email;
        ImageView dialog_pic;

        public SearchViewHolder(View itemView)
        {
            super(itemView);


            nname =          itemView.findViewById(R.id.members_name);
            naddress =   itemView.findViewById(R.id.members_address);
            nid =         itemView.findViewById(R.id.members_email);
            nmobile =        itemView.findViewById(R.id.members_mobile);
            nimage =         itemView.findViewById(R.id.members_profile_image);
            npaid =         itemView.findViewById(R.id.ex);



        }
    }

    public SearchAdapter(Context context, ArrayList<String> name, ArrayList<String> address ,ArrayList<String> mobile, ArrayList<String> id ,ArrayList<String> paid, ArrayList<String> image)
    //, ArrayList<String> amount, ArrayList<String> image)
    {
        this.context = context;
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.id = id;
        this.paid = paid;
       // this.amount = amount;
        this.image = image;
    }



    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_rows, parent, false);
        return new SearchAdapter.SearchViewHolder(view);

    }

    @Override
    public void onBindViewHolder( SearchViewHolder holder, final int position) {
        holder.nname.setText(name.get(position));
        holder.naddress.setText(address.get(position));
        holder.nmobile.setText(mobile.get(position));

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
        if (PAid.equals(paid.get(position)))
        {
           // holder.setEx(model.getPaid());
            holder.npaid.setText(paid.get(position));
        }
        else
            holder.npaid.setText("Unpaid");
        //holder.setEx("Unpaid");

      //  holder.npaid.setText(paid.get(position));
       holder.nid.setText(id.get(position));
       Picasso.get().load(image.get(position)).into(holder.nimage);



        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

    }




    @Override
    public int getItemCount() {
        return name.size();
    }
}

