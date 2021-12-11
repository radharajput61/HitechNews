package com.app.hitechnews.UserDashbord;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.hitechnews.Activity.Auth.LoginActivity;
import com.app.hitechnews.Activity.Follow.FollowerListActivity;
import com.app.hitechnews.Activity.Follow.FollowingListActivity;
import com.app.hitechnews.Activity.Profile.UpdateProfileActivity;
import com.app.hitechnews.Activity.UserTypeSelect;
import com.app.hitechnews.Adapter.PagerAdapter2;
import com.app.hitechnews.R;
import com.app.hitechnews.Util.API;
import com.app.hitechnews.Util.MyPreferences;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends Fragment {
    MyPreferences myPreferences;
    String UserId;
    TextView logout,tvName,tvEmail,tvAbout,tvFollowrs,tvFollowing,tvGroup,tvMobileAndGender,tvEdit,tvChangeProfilePic,tvGender;
    CircleImageView ivProfilePic;
    int type = 0;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    int PERMISSION_ALL = 4;
    private static final int SELECT_PICTURE = 100;
    public Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "Real Estate";
    public static final int MEDIA_TYPE_VIDEO = 2;
    String picturePath = "",filename = "",selectedImagePath,ext = "";
    public Uri picUri;
    public static String photo_url = "";
    public static Bitmap bitmap;
    LinearLayout llFollower,llFollowing;
    String mName,mEmail,mAbout,mGender,mMobile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_user_profile, container, false);
        myPreferences=new MyPreferences(getActivity());
        UserId=myPreferences.getUserId();
        tvName=root.findViewById(R.id.tvName);
        tvGender=root.findViewById(R.id.tvGender);
        tvEmail=root.findViewById(R.id.tvEmail);
        tvAbout=root.findViewById(R.id.tvAbout);
        ivProfilePic=root.findViewById(R.id.ivProfilePic);
        tvMobileAndGender=root.findViewById(R.id.tvMobileAndGender);
        tvEdit=root.findViewById(R.id.tvEdit);
        logout=root.findViewById(R.id.logout);
        tvChangeProfilePic=root.findViewById(R.id.tvChangeProfilePic);
        getUserProfileData(UserId);

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateProfileActivity.class));
            }
        });

        tvChangeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetImage();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });

        return root;
    }


    public void logoutDialog() {

        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialog.setContentView(R.layout.logout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        Button btnOkay,btnCancel;
        btnOkay = dialog.findViewById(R.id.btnOkay);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                myPreferences.setUserId(null);
                myPreferences.setMobile(null);
                myPreferences.setUserId(null);
                myPreferences.setUsertype(null);

                Intent i= new Intent(getActivity(), UserTypeSelect.class);
                startActivity(i);
                getActivity().finish();

            }

        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void getUserProfileData(final String UserId)
    {
        final JSONObject obj = new JSONObject();
        try
        {
            obj.put("UserId", UserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("ksjdf",obj.toString());
        final RequestQueue requestQueue= Volley.newRequestQueue(getActivity());

        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.UserProfile, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    boolean status=response.getBoolean("Status");

                    String Msg=response.getString("Message");
                    if(status)
                    {

                        String AboutUs=response.getString("AboutUs");
                        String Email=response.getString("Email");//
                        String Gender=response.getString("Gender");//
                        String MobileNo=response.getString("MobileNo");//

                        String Name=response.getString("Name");//
                        String ProfilePic=response.getString("ProfilePic");
                        String TotalFollower=response.getString("TotalFollower");
                        String TotalFollowing=response.getString("TotalFollowing");
                        String TotalGroup=response.getString("TotalGroup");
                        mName=Name;
                        mAbout=AboutUs;
                        mEmail=Email;
                        mGender=Gender;
                        mMobile=MobileNo;

                        if(!Name.equalsIgnoreCase(""))
                            tvName.setText("Name - "+Name);
                        else
                            tvName.setText("Name -");

                        if(!Email.equalsIgnoreCase(""))
                            tvEmail.setText("Email - "+Email);
                        else
                            tvEmail.setText("Email -");

                        if(!MobileNo.equalsIgnoreCase(""))
                            tvMobileAndGender.setText("Mobile - "+MobileNo);
                        else
                            tvMobileAndGender.setText("Mobile -");

                        if(!mGender.equalsIgnoreCase(""))
                            tvGender.setText("Gender - "+Gender);
                        else
                            tvGender.setText("Gender -");

                        if(!AboutUs.equalsIgnoreCase(""))
                            tvAbout.setText(AboutUs);
                        else
                            tvAbout.setText("Write Your Self");

                        try {
                            Picasso.with(getActivity()).load(ProfilePic).placeholder(R.drawable.placeholder).into(ivProfilePic);

                        } catch (Exception e) {
                        }

                    }else{

                        Toast.makeText(getActivity(), ""+Msg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("jgdkjhkj",error.toString());

            }
        });
        requestQueue.add(request);
    }

    private void getUpdateProfileData(final String AboutUs, final String Email,final String Gender,final String Name,String ProfilePic,String UserId)
    {
        final JSONObject obj = new JSONObject();
        try
        {
            obj.put("AboutUs", AboutUs);
            obj.put("Email", Email);
            obj.put("Gender", Gender);
            obj.put("Name", Name);
            obj.put("ProfilePic", ProfilePic);
            obj.put("UserId", UserId);
            obj.put("TotalFollower", "");
            obj.put("TotalFollowing", "");
            obj.put("TotalGroup", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("ksjdf",obj.toString());
        final RequestQueue requestQueue= Volley.newRequestQueue(getActivity());

        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.UpdateUserProfile, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    boolean status=response.getBoolean("Status");

                    String Msg=response.getString("Message");
                    if(status)
                    {


                        Toast.makeText(getActivity(), ""+Msg, Toast.LENGTH_LONG).show();

                    }else{

                        Toast.makeText(getActivity(), ""+Msg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("jgdkjhkj",error.toString());

            }
        });
        requestQueue.add(request);
    }

    private void GetImage() {
        type = 1;
        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        } else {
            imageDialog();
        }
    }

    public void imageDialog() {
        Log.v("agdsh", "1");
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialog.setContentView(R.layout.image_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        ImageView ivCamera, ivGallery;
        TextView tvCancel;

        ivCamera = dialog.findViewById(R.id.ivCamera);
        ivGallery = dialog.findViewById(R.id.ivGallery);
        tvCancel = dialog.findViewById(R.id.tvCancel);

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                captureImage();
                dialog.dismiss();
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPickerIntent, SELECT_PICTURE);
                dialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void captureImage() {
        Log.v("agdsh", "2");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                "com.app.hitechnews.fileprovider", getOutputMediaFile(MEDIA_TYPE_IMAGE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        Log.v("agdsh", "5");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    private static File getOutputMediaFile(int type) {
        Log.v("agdsh", "3");
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static int getImageOrientation(String imagePath) {
        Log.v("agdsh", "4");
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static String getFileType(String path) {
        Log.v("agdsh", "6");
        String fileType = null;
        fileType = path.substring(path.indexOf('.', path.lastIndexOf('/')) + 1).toLowerCase();
        return fileType;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                picturePath = fileUri.getPath().toString();

                filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                Log.d("filename_camera", filename);

                String selectedImagePath = picturePath;

                //    selectedImagePath =SiliCompressor.with(this).compress(picturePath,  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));;

                Uri uri = Uri.parse(picturePath);
                ext = "jpg";
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 500;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
                //ivUserImage.setImageBitmap(bitmap);
                Matrix matrix = new Matrix();
                matrix.postRotate(getImageOrientation(picturePath));
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte[] ba = bao.toByteArray();

                if (type == 1) {
                    Log.v("agdsh", "8");

                    photo_url = getEncoded64ImageStringFromBitmap(rotatedBitmap);
                    ivProfilePic.setImageBitmap(rotatedBitmap);
                    getUpdateProfileData(mAbout,mEmail,mGender,mName,photo_url,UserId);

                }
            }
        } else if (requestCode == SELECT_PICTURE) {
            Log.v("agdsh", "9");
            Log.v("jdhakjh", "" + data);
            if (data != null) {
                Log.v("agdsh", "10");
                Uri contentURI = data.getData();
                //get the Uri for the captured image
                picUri = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(contentURI, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                selectedImagePath = picturePath;
                Log.d("path", picturePath);
                System.out.println("Image Path : " + picturePath);
                cursor.close();


                filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                ext = getFileType(picturePath);
                String selectedImagePath = picturePath;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 500;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
                //  ivUserImage.setImageBitmap(bitmap);

                Matrix matrix = new Matrix();
                matrix.postRotate(getImageOrientation(picturePath));
                try {
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                    if (type == 1) {
                        Log.v("agdsh", "11");
                        photo_url = getEncoded64ImageStringFromBitmap(rotatedBitmap);
                        ivProfilePic.setImageBitmap(rotatedBitmap);
                        getUpdateProfileData(mAbout,mEmail,mGender,mName,photo_url,UserId);
                    }


                    byte[] ba = bao.toByteArray();
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }


            } else {

                // Snackbar.make(this, "Please enter Username.", Snackbar.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Unable to Select the Image.", Toast.LENGTH_LONG).show();
            }

        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}