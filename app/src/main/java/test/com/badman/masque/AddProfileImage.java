package test.com.badman.masque;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddProfileImage extends AppCompatActivity
{
    private String full_name;
    private String full_mobile;
    private String Area;


    private String categoryName, Description, Price, ProductName, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButtion,Skip;
    private Button InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductRef;
    private ProgressDialog loadingBar;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_image);


        Intent intent = getIntent();
        final String f_name = intent.getExtras().getString("name");
        final String f_mobile = intent.getExtras().getString("mobile");
        final String f_area = intent.getExtras().getString("area");
        full_name=f_name;
        full_mobile=f_mobile;
        Area=f_area;



        storage = FirebaseStorage.getInstance();
        ProductImagesRef = storage.getReference().child("profile_image");


        ProductRef = FirebaseDatabase.getInstance().getReference().child("area_member").child(Area);

        AddNewProductButtion = findViewById(R.id.sen_that);
        InputProductImage = findViewById(R.id.setting_profile_image_button);
         Skip = findViewById(R.id.skip);
//        InputProductName = findViewById(R.id.product_Name);
//        InputProductDescription = findViewById(R.id.product_description);
//        InputProductPrice = findViewById(R.id.prduct_price);

      //  loadingBar = new ProgressDialog(this);

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        AddNewProductButtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });
        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddProfileImage.this,HomeActivity.class));
            }
        });

    }

    private void OpenGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
//            InputProductImage.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData() {

//        Description = InputProductDescription.getText().toString();
//        Price = InputProductPrice.getText().toString();
        ProductName = full_name;
                //InputProductName.getText().toString();


        if(ImageUri == null)
        {
            Toast.makeText(this,"Profile Image is mandatory",Toast.LENGTH_SHORT).show();
        }
//        else if (TextUtils.isEmpty(Description))
//        {
//            Toast.makeText(this,"Please Write Product Description",Toast.LENGTH_SHORT).show();
//        }
//        else if(TextUtils.isEmpty(Price))
//        {
//            Toast.makeText(this,"Please Write Product Price",Toast.LENGTH_SHORT).show();
//        }
//        else if(TextUtils.isEmpty(ProductName))
//        {
//            Toast.makeText(this,"Please Write Product Name",Toast.LENGTH_SHORT).show();
//        }
        else
        {
            StoreProductInformation();

        }
    }

    private void StoreProductInformation() {

//        loadingBar.setTitle("Add New Product");
//        loadingBar.setMessage("Please Wait, While we are adding the new Product");
//        loadingBar.setCanceledOnTouchOutside(false);
//        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM yyy ||");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS am");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = full_name+"_"+full_mobile;
                //saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(AddProfileImage.this,"Error "+message,Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddProfileImage.this,"Profile Image Uploaded Successfullly ",Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AddProfileImage.this,"Get the Profile Image Url Successfully",Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });



    }

    private void SaveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();

       // productMap.put("pid",productRandomKey);
      //  productMap.put("date",saveCurrentDate);
        productMap.put("time",ProductName);
      //  productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
      //  productMap.put("category",categoryName);
      //  productMap.put("price",Price);
      //  productMap.put("name",ProductName);

        ProductRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
//                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
//
//                            loadingBar.dismiss();
                            Toast.makeText(AddProfileImage.this,"Pic Added",Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
//                            loadingBar.dismiss();
//                            String message = task.getException().toString();
                            Toast.makeText(AddProfileImage.this,"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}


//    }
//
//}
