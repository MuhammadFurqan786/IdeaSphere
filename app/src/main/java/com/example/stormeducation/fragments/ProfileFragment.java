package com.example.stormeducation.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.stormeducation.R;
import com.example.stormeducation.Utils.Constants;
import com.example.stormeducation.Utils.Utilities;
import com.example.stormeducation.activities.AskquestionActivity;
import com.example.stormeducation.activities.LoginActivity;
import com.example.stormeducation.activities.MainActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    TextView title_text;
    Spinner changeLan;
    String[] country = {"Select Language", "English", "German"};
    TextView logout, name;
    String username,image,downloadImageURi;
    ImageView imageView;
    Uri imageUri;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 201;
    private static final int CAMERA_IMAGE_CODE = 400;
    private static final int GALLERY_IMAGE_CODE = 401;
    String cameraPermission[];
    String storagePermission[];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        getUserdetails();
        title_text = v.findViewById(R.id.title_text);
        name = v.findViewById(R.id.name);
        changeLan = v.findViewById(R.id.changelanguage);
        logout = v.findViewById(R.id.logout);
        imageView = v.findViewById(R.id.changeProfile);
        username = Utilities.getString(getContext(), "username");
        name.setText(username);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

//        if (Utilities.getString(getContext(),"userimage").isEmpty()){
////            Toast.makeText(getContext(), "No Image", Toast.LENGTH_SHORT).show();
//        }else
//        {
//            Glide.with(getContext()).load(Utilities.getString(getContext(),"userimage")).into(imageView);
//
//        }


        title_text.setText("Profile");
        changeLan.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        changeLan.setAdapter(aa);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUser();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public void onClick(View v) {
               showimageimportdialog();
            }
        });

        return v;
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private void logOutUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        Objects.requireNonNull(getActivity()).finish();
        startActivity(new Intent(getContext(), LoginActivity.class));
        Utilities.saveString(getContext(), "login", "no");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = country[position];
        Toast.makeText(getContext(), country[position], Toast.LENGTH_SHORT).show();
        if (selected.equals("English")) {
            setLocale(getActivity(), "en");
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();

        } else if (selected.equals("German")) {
            setLocale(getActivity(), "de");
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            setLocale(getActivity(), "en");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }


    private void showimageimportdialog() {
        //items to display in dialog
        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Select Image");

        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (position == 0) {
                    //Camera option Clicked
                    if (!checkCameraPermission()) {
                        //camera permission not allowed,request it
                        requestCameraPermission();
                    } else {
                        //permission allowed ,take picture

                        pickCamera();
                    }


                }
                if (position == 1) {
                    //Gallery option Clicked
                    if (!checkStoragePermission()) {
                        //Storage permission not allowed,request it
                        requestStoragePermission();
                    } else {
                        //permission allowed, take picture

                        pickGallery();
                    }

                }
            }
        });
        dialog.create().show();//show dialog
    }

    private void pickGallery() {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        //set intent type to image
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_IMAGE_CODE);

    }

    private void pickCamera() {
        //intent to take image from camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Photo");//title of the pic
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to Text");//desc of the pic
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_IMAGE_CODE);
    }

    private void requestStoragePermission() {

        ActivityCompat.requestPermissions(getActivity(), storagePermission, STORAGE_REQUEST_CODE);

    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestCameraPermission() {

        ActivityCompat.requestPermissions(getActivity(), cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {

        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);


        boolean result1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    //handle permission result

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        pickCamera();
                    } else {
                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted) {
                        pickGallery();
                    } else {
                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }

            /*case FILE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
            break;*/
        }
    }

    //handle image result

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //got image from camera
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_IMAGE_CODE) {
                //got image from gallery now crop it
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(getContext(),this);

            }
            if (requestCode == CAMERA_IMAGE_CODE) {


                CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).start(getContext(),this);


            }
        }
        //get cropped image

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                Uri uri = result.getUri(); //get image uri
                imageView.setImageURI(uri);

                storeImageToFirebase(uri);
                //get drawable bitmap for text recognition

            } else {
                //if there is any error
                assert result != null;
                Exception error = result.getError();
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void storeImageToFirebase(Uri bitmap) {


        StorageReference mStorageref = FirebaseStorage.getInstance().getReference().child(Constants.USER).child(FirebaseAuth.getInstance().getUid()).child("image");

        final StorageReference filePath = mStorageref.child(bitmap.getLastPathSegment());
        final UploadTask uploadTask = filePath.putFile(bitmap);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return mStorageref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                        } else {
//                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
            }


            private void getUserdetails() {
                FirebaseDatabase.getInstance().getReference().child(Constants.USER)
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    image = String.valueOf(snapshot.child("image").getValue());
//                                    Toast.makeText(getContext(), "" + username, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
            }

