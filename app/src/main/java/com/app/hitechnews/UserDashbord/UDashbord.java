package com.app.hitechnews.UserDashbord;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.hitechnews.Activity.Notification.NotificationFragment;
import com.app.hitechnews.Activity.Post.PrepairVideoForUploadActivity;
import com.app.hitechnews.Activity.Profile.ProfileFragment;
import com.app.hitechnews.Activity.Search.SearchFragment;
import com.app.hitechnews.Fragment.HomeFragment;
import com.app.hitechnews.MainActivity;
import com.app.hitechnews.R;
import com.app.hitechnews.UserDashbord.DynamicTab.DynamicActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UDashbord extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    private static final String TAG= MainActivity.class.getSimpleName();
    public static Uri videoUri;
    public static String videoBase64String;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    public static final int RequestPermissionCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udashbord);
        requestPermission();
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if(savedInstanceState==null)
        {
            fragmentManager=getSupportFragmentManager();
            UserHome homeFragment=new UserHome();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,homeFragment).commit();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
        Fragment fragment=null;
        switch (item.getItemId())
        {
            case R.id.tab_home:
                fragment=new UserHome();

                break;
            case R.id.tab_search:
                fragment=new SearchFragment();
                break;
//            case R.id.tab_post:
//                showchooser();
//                break;
            case R.id.tab_noti:
                fragment=new NotificationFragment();
                break;
            case R.id.tab_profile:
                fragment=new UserProfile();
                break;
        }
        if(fragment!=null)
        {
            fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).commit();
        }else{
            Log.e(TAG,"Error in Creating Fragment");
        }
        return false;
    }

    public void showchooser() {
        final BottomSheetDialog bottomSheet = new BottomSheetDialog(UDashbord.this);
        View mView = LayoutInflater.from(UDashbord.this).inflate(R.layout.layout_selection, null);
        bottomSheet.setContentView(mView);
        ImageView rv_cart_list = mView.findViewById(R.id.ivCamera);
        ImageView tv_next = mView.findViewById(R.id.ivGallery);
        ImageView tvCancel = mView.findViewById(R.id.tvCancel);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                } else {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                }
                startActivityForResult(Intent.createChooser(intent, "Select a video "), 100);

                bottomSheet.cancel();
            }
        });
        rv_cart_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakeVideoIntent();
                bottomSheet.cancel();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.cancel();
            }
        });
        bottomSheet.show();
        bottomSheet.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog dialogc = (BottomSheetDialog) dialog;
                // When using AndroidX the resource can be found at com.google.android.material.R.id.design_bottom_sheet
//                LinearLayout bottomSheet1 = dialogc.findViewById(R.id.v_bt);
//                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet1);
//                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    public void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = intent.getData();
            String imagePath=getPathFromUri(UDashbord.this,videoUri);
            Log.v("dsjsdh",""+imagePath);
//            File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
//            try {
////                videoBase64String  = encodeFileToBase64Binary(imagePath);//
//                Log.v("videoBase64",videoBase64String);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            Log.v("dsjsdh",""+imagePath);
            startActivity(new Intent(getApplicationContext(), PrepairVideoForUploadActivity.class));
        }
        if (requestCode == 100 && resultCode == RESULT_OK) {
            videoUri = intent.getData();
            startActivity(new Intent(UDashbord.this, PrepairVideoForUploadActivity.class));
        }
    }


    public static String encodeFileToBase64Binary(String fileName) throws IOException {

        Log.d(TAG, "encodeFileToBase64Binary: " + fileName);

        File file = new File(fileName);
        Log.v("hjghjjhj",""+file);
        Uri ImageUri=Uri.fromFile(file);
        byte[] bytes = loadFile(file);
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        Log.v(TAG, "gsadgjhdsg: " + encodedString);
        return encodedString;
    }
    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        Log.v("hjkdh",""+length);

        Log.v("lengthhhytftf",""+length);
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(UDashbord.this, new
                String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(UDashbord.this, "Welcome",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(UDashbord.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }



}