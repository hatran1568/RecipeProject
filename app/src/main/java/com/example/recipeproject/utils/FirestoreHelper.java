package com.example.recipeproject.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.recipeproject.InterfaceGetData.FirebaseStorageCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirestoreHelper {
    private static FirebaseStorage storageRef = FirebaseStorage.getInstance();

    // Upload uri to storage and return the download url
    public static void uploadToStorage(FirebaseStorageCallback callback, Context context, Uri fileUri){
        StorageReference storageReference = storageRef.getReference()
                .child(System.currentTimeMillis() + "." + getFileExstension(context, fileUri));

        final String[] downloadUri = {null};
        storageReference.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("Firebase storage", "Uploaded " + fileUri + " to " + uri.toString());
                        downloadUri[0] = uri.toString();
                        callback.onResponse(downloadUri[0]);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Upload failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void deleteFile(String url){
        StorageReference fileRef = storageRef.getReferenceFromUrl(url);
        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("File", "onSuccess: deleted file " + url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("File", "onFailure: did not delete file " + url);
            }
        });
    }

    private static String getFileExstension(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
