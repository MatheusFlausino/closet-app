package com.matheusflausino.closetapp.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.matheusflausino.closetapp.R;

public class UtilFoto {

    public static String createImageFile(Context context, Intent data) throws IOException {

        String imageFileName = "IMG_" + (new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(new Date()));
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);

        Bundle bundle = data.getExtras();
        Bitmap bitmap;
        if(bundle != null) {
            bitmap = (Bitmap) bundle.getParcelable("data");
        }else{
            final Uri imageUri = data.getData();
            final InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
            bitmap = BitmapFactory.decodeStream(imageStream);
            //image_view.setImageBitmap(selectedImage);
        }


        FileOutputStream fos = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        fos.flush();
        fos.close();

        return imageFile.getAbsolutePath();
    }

    public static void setImage(String caminho, ImageView imageView, int minWidt, int minHeight) {
    //public static void setImage(Intent data, ImageView imageView){
        // Get the dimensions of the View

        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        if (targetH == 0)
            targetH = minHeight;

        if (targetW == 0){
            targetW = minWidt;
        }

        // Get the dimensions of the bitmap

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(caminho, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(caminho, bmOptions);
//        Bundle bundle = data.getExtras();
//        Bitmap bitmap = bundle.getParcelable("data");
        imageView.setImageBitmap(bitmap);
    }

    public static void galleryOpen(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra("return-data",true);

        activity.startActivityForResult(Intent.createChooser(intent,"Select Image from Gallery"),requestCode);
    }

    public static void cameraOpen(Activity activity, int requestCode) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(activity.getPackageManager()) != null) {

            intent.putExtra("return-data",true);
            activity.startActivityForResult(intent, requestCode);
        }else {
            Log.d("DEBUG", "Erro ao pegar nome do arquivo");
        }
    }

    public static boolean deleteImage(Context context, String caminho){

        File file = new File(caminho);
        Uri  uri  = FileProvider.getUriForFile(context,
                context.getPackageName() + ".fileprovider",
                                               file);

        return file.delete();
    }

    public static void cropImage(Activity activity, int requestCode, Uri uri) {

        try{
            Intent CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setClassName("com.android.camera", "com.android.camera.CropImage");

            CropIntent.setDataAndType(uri, "image/*");;

            CropIntent.putExtra("crop","true");
            CropIntent.putExtra("outputX",300);
            CropIntent.putExtra("outputY",400);
            CropIntent.putExtra("aspectX",3);
            CropIntent.putExtra("aspectY",4);
            CropIntent.putExtra("noFaceDetection", true);
            CropIntent.putExtra("scaleUpIfNeeded",true);
            CropIntent.putExtra("return-data",true);

            CropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            activity.startActivityForResult(CropIntent,requestCode);
        } catch (ActivityNotFoundException ex){
            Toast toast = Toast.makeText(activity, R.string.error_crop, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}