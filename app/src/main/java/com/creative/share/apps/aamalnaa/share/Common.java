package com.creative.share.apps.aamalnaa.share;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;


import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.aamalnaa.databinding.DialogCustom2Binding;
import com.creative.share.apps.aamalnaa.databinding.DialogCustomBinding;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Common {


    public static void CloseKeyBoard(Context context, View view) {
        if (context != null && view != null) {
            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (manager != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }

        }


    }


    public static void CreateNoSignAlertDialog(Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        AppCompatActivity activity = (AppCompatActivity) context;
        DialogCustomBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_custom, null, false);

        binding.btnSignUp.setOnClickListener((v) -> {
            dialog.dismiss();

            if (activity instanceof HomeActivity) {
                HomeActivity homeActivity = (HomeActivity) activity;
                homeActivity.NavigateToSignInActivity(false);
            } else {
                Intent intent = new Intent(context, SignInActivity.class);
                intent.putExtra("sign_up", false);
                context.startActivity(intent);
                ((AppCompatActivity) context).finish();
            }
        });
        binding.btnSignIn.setOnClickListener((v) -> {
            dialog.dismiss();

            if (activity instanceof HomeActivity) {
                HomeActivity homeActivity = (HomeActivity) activity;
                homeActivity.NavigateToSignInActivity(true);
            } else {
                Intent intent = new Intent(context, SignInActivity.class);
                intent.putExtra("sign_up", true);
                context.startActivity(intent);
                ((AppCompatActivity) context).finish();


            }

        });

        binding.btnCancel.setOnClickListener((v) ->
                dialog.dismiss()

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }


    public static void CreateNoSignAlertDialogs(Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogCustom2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_custom2, null, false);

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     dialog.dismiss();
                                                 }
                                             }

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    public static void CreateAlertDialog(Context context, String msg) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogCustom2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_custom2, null, false);
        binding.tvMsg.setText(msg);
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     dialog.dismiss();
                                                 }
                                             }

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getImagePath(Context context, Uri uri) {
        int currentApiVersion;
        try {
            currentApiVersion = Build.VERSION.SDK_INT;
        } catch (NumberFormatException e) {
            //API 3 will crash if SDK_INT is called
            currentApiVersion = 3;
        }
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {


            if (DocumentsContract.isDocumentUri(context, uri)) {

                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/"
                                + split[1];
                    }
                } else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));

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
                    final String[] selectionArgs = new String[]{split[1]};

                    return getDataColumn(context, contentUri, selection,
                            selectionArgs);
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
        } else if (currentApiVersion <= Build.VERSION_CODES.HONEYCOMB_MR2 && currentApiVersion >= Build.VERSION_CODES.HONEYCOMB) {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null) {
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        } else {

            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
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
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    private static File getFileFromImagePath(String path) {
        File file = new File(path);
        return file;
    }

    public static MultipartBody.Part getMultiPart(Context context, Uri uri, String partName) {
        File file = getFileFromImagePath(getImagePath(context, uri));
        RequestBody requestBody = getRequestBodyImage(file);
        MultipartBody.Part part = MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
        return part;

    }

    private static RequestBody getRequestBodyImage(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        return requestBody;
    }

    public static RequestBody getRequestBodyText(String data) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), data);
        return requestBody;
    }

    public static ProgressDialog createProgressDialog(Context context, String msg) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        ProgressBar bar = new ProgressBar(context);
        Drawable drawable = bar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        dialog.setIndeterminateDrawable(drawable);
        return dialog;

    }

}
