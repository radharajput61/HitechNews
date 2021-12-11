package com.app.hitechnews.Util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploader {

    private FileUploaderCallback mFileUploaderCallback;
    long filesize = 0l;

    public FileUploader(File file, Context context) {
        filesize = file.length();
        Log.e("--URL--", "filesize: " + filesize);

        InterfaceFileUpload interfaceFileUpload = ApiClient.getRetrofitInstance()
                .create(InterfaceFileUpload.class);

        Log.e("--URL--", "interfaceDesignation: " + interfaceFileUpload.toString());

//        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        PRRequestBody mFile = new PRRequestBody(file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file",
                file.getName(), mFile);


        RequestBody CustomerID = RequestBody.create(
                MultipartBody.FORM, "CIK002");

        RequestBody BackupType = RequestBody.create(
                MultipartBody.FORM, "Document");

        RequestBody ProductName = RequestBody.create(
                MultipartBody.FORM, "Retail");

        RequestBody AppVersionAppVersion = RequestBody.create(
                MultipartBody.FORM, "1.22.3");

        RequestBody AppType = RequestBody.create(
                MultipartBody.FORM, "Mobile");

        RequestBody IMEINumber = RequestBody.create(
                MultipartBody.FORM, "23234234");

        RequestBody DeviceInfo = RequestBody.create(
                MultipartBody.FORM, "Motorola");

        RequestBody Remark = RequestBody.create(
                MultipartBody.FORM, "@Backup");

        RequestBody FileName = RequestBody.create(
                MultipartBody.FORM, "@SHAP_Backup");

        RequestBody Extentsion = RequestBody.create(
                MultipartBody.FORM, ".zip");

        Call<String> fileUpload = interfaceFileUpload.UploadFile(fileToUpload,filename );


        Log.e("--URL--", "************************  before call : " +
                fileUpload.request().url());

        fileUpload.enqueue(new Callback<String>() {

            @Override
            public void onResponse(@NonNull Call<String> call,
                                   @NonNull Response<String> response) {

                if (response != null && response.code() == 200) {
                    Log.e("getResponse", "--Response:-" + response.message());
                    Log.e("getResponse", "--Response:-" + response.body().toString());

//                    if (response.body().getSuccess().equalsIgnoreCase("1")) {
                       mFileUploaderCallback.onFinish(response.body() );
                        Toast.makeText(context," Success "+ response.body() , Toast.LENGTH_LONG).show();
                   // }

                }

else {
                    mFileUploaderCallback.onError(response+"");
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mFileUploaderCallback.onError(t.getMessage());
            }
        });


    }

    public void SetCallBack(FileUploaderCallback fileUploaderCallback) {
        this.mFileUploaderCallback = fileUploaderCallback;
    }


    public class PRRequestBody extends RequestBody {
        private File mFile;

        private static final int DEFAULT_BUFFER_SIZE = 20000048;

        public PRRequestBody(final File file) {
            mFile = file;
        }

        @Override
        public MediaType contentType() {
            // i want to upload only images
            return MediaType.parse("multipart/form-data");
        }

        @Override
        public long contentLength() throws IOException {
            return mFile.length();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            long fileLength = mFile.length();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            FileInputStream in = new FileInputStream(mFile);
            long uploaded = 0;
//            Source source = null;

            try {
                int read;
//                source = Okio.source(mFile);
                Handler handler = new Handler(Looper.getMainLooper());
                while ((read = in.read(buffer)) != -1) {

                    // update progress on UI thread
                    handler.post(new ProgressUpdater(uploaded, fileLength));
                    uploaded += read;
                    sink.write(buffer, 0, read);
                    Log.e("FileUploader", String.valueOf(uploaded));

                }



//                sink.writeAll(source);
            } catch (Exception e){
                Log.e("Exception",e.getMessage());
            } finally {
                in.close();
            }
        }
    }


    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;

        ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            int current_percent = (int) (100 * mUploaded / mTotal);
            int total_percent = (int) (100 * (mUploaded) / mTotal);
            mFileUploaderCallback.onProgressUpdate(current_percent, total_percent,
                    "File Size: " + ClsGlobal.readableFileSize(filesize));
        }
    }

    public interface FileUploaderCallback {

        void onError(String msg);

        void onFinish(String responses);

        void onProgressUpdate(int currentpercent, int totalpercent, String msg);
    }


}
