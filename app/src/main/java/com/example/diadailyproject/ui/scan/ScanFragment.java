package com.example.diadailyproject.ui.scan;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.diadailyproject.R;
import com.example.diadailyproject.databinding.FragmentFoodBinding;
import com.example.diadailyproject.databinding.FragmentScanBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import androidx.fragment.app.Fragment;



public class ScanFragment extends Fragment {
    // class implementation

    private MaterialButton cameraBtn;
    private MaterialButton photosBtn;
    private MaterialButton scanBtn;
    private ImageView imageV;
    private TextView resultV;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri imageUri = null;

    private BarcodeScannerOptions barcodeScannerOptions;
    private BarcodeScanner barcodeScanner;

    private static final String TAG = "MAIN_TAG";

    private FragmentScanBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cameraBtn = root.findViewById(R.id.cameraBtn);
        photosBtn = root.findViewById(R.id.photosBtn);
        scanBtn = root.findViewById(R.id.scanBtn);
        imageV = root.findViewById(R.id.imageV);
        resultV = root.findViewById(R.id.resultV);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        barcodeScannerOptions = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build();

        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    pickImageCamera();
                } else {
                    requestCameraPermission();
                }
            }
        });

        photosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermission()) {
                    pickImageGallery();
                } else {
                    requestStoragePermission();
                }
            }
        });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null) {
                    Toast.makeText(getContext(), "Pick Image First", Toast.LENGTH_SHORT).show();
                } else {
                    detectResultFromImage();
                }
            }
        });

        return root;
    }




    private void detectResultFromImage(){
        try {
            InputImage inputImage = InputImage.fromFilePath(getContext(), imageUri);
            Task<List<Barcode>> barcodeResult = barcodeScanner.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            extractBarCodeQRCodeInfo(barcodes);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed scan due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e){
            Toast.makeText(getContext(), "Failed due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void extractBarCodeQRCodeInfo(List<Barcode> barcodes) {
        for (Barcode barcode : barcodes){
            Rect bounds = barcode.getBoundingBox();
            Point[] corners = barcode.getCornerPoints();

            String rawValue = barcode.getRawValue();
            Log.d(TAG, "extractBarCodeQRCodeInfo: rawValue: "+ rawValue);

            int valueType = barcode.getValueType();

            switch (valueType){
                case Barcode.TYPE_WIFI:{
                    Barcode.WiFi typeWifi = barcode.getWifi();

                    String ssid = ""+ typeWifi.getSsid();
                    String password = ""+ typeWifi.getPassword();
                    String encryptionType = ""+ typeWifi.getEncryptionType();

                    Log.d(TAG, "extractBarCodeQRCodeInfo: TYPE_WIFI: ");
                    Log.d(TAG, "extractBarCodeQRCodeInfo: ssid: "+ ssid);
                    Log.d(TAG, "extractBarCodeQRCodeInfo: password: "+ password);
                    Log.d(TAG, "extractBarCodeQRCodeInfo: encryptionType: "+ encryptionType);

                    resultV.setText("TYPE: TYPE_WIFI \nssid: " + ssid + "\npassword: " + password + "\nencryptionType"+encryptionType + "\nraw value: " + rawValue);
                }
                break;
                case Barcode.TYPE_URL:{

                    Barcode.UrlBookmark typeUrl = barcode.getUrl();

                    String title = ""+ typeUrl.getTitle();
                    String url = ""+ typeUrl.getUrl();

                    Log.d(TAG, "extractBarCodeQRCodeInfo: TYPE_WIFI: ");
                    Log.d(TAG, "extractBarCodeQRCodeInfo: title: "+title);
                    Log.d(TAG, "extractBarCodeQRCodeInfo: url: "+url);

                    resultV.setText("TYPE: TYPE_WIFI \ntitle: " + title + " \nurl: " + url + "\nraw value: " + rawValue);
                }
                break;
                case Barcode.TYPE_EMAIL:{

                    Barcode.Email typeEmail = barcode.getEmail();

                    String address = ""+ typeEmail.getAddress();
                    String body = ""+ typeEmail.getBody();
                    String subject = ""+ typeEmail.getSubject();

                    Log.d(TAG, "extractBarCodeQRCodeInfo: TYPE_EMAIL");
                    Log.d(TAG, "extractBarCodeQRCodeInfo: address: " + address);
                    Log.d(TAG, "extractBarCodeQRCodeInfo: body: " + body);
                    Log.d(TAG, "extractBarCodeQRCodeInfo: subject: " + subject);

                    resultV.setText("TYPE: TYPE_EMAIL \naddress: " + address + " \nbody: " + body +  " \nsubject: " + subject + " \nraw value: " + rawValue);
                }
                break;
                case Barcode.TYPE_CONTACT_INFO:{

                    Barcode.ContactInfo typeContact = barcode.getContactInfo();

                    String title = ""+ typeContact.getTitle();
                    String organizer = ""+ typeContact.getOrganization();
                    String name = ""+ typeContact.getName().getFirst() +" "+ typeContact.getName().getLast();
                    String phone = ""+ typeContact.getPhones().get(0).getNumber();

                    Log.d(TAG, "extractBarCodeQRCodeInfo: TYPE_EMAIL");
                    Log.d(TAG, "extractBarCodeQRCodeInfo: title: " + title);
                    Log.d(TAG, "extractBarCodeQRCodeInfo: organizer: " + organizer);
                    Log.d(TAG, "extractBarCodeQRCodeInfo: name: " + name);
                    Log.d(TAG, "extractBarCodeQRCodeInfo: phone: " + phone);

                    resultV.setText("TYPE: TYPE_CONTACT_INFO \ntitle: " + title + " \norganizer: " + organizer +  " \nname: " + name + " \nphone: " + phone + " \nraw value: " + rawValue);
                }
                break;
                default:{
                    resultV.setText("raw value: "+ rawValue);
                }
            }
        }
    }


    private void pickImageGallery(){

        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");
        photosActivityResultLauncher.launch(intent);

    }

    private final ActivityResultLauncher<Intent> photosActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Log.d(TAG, "onActivityResult: imageUri: "+imageUri);
                        imageV.setImageURI(imageUri);
                    }
                    else{
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );

    private void pickImageCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Description");

        imageUri = requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }


    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Log.d(TAG, "onActivityResult: imageUri "+ imageUri);
                        imageV.setImageURI(imageUri);
                    }
                    else{
                        Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private boolean checkStoragePermission(){

        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions((Activity) getContext(), storagePermissions, STORAGE_REQUEST_CODE);

    }

    private boolean checkCameraPermission(){
        boolean resultCamera = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        boolean resultStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        return resultCamera && resultStorage;

    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions((Activity) getContext(), cameraPermissions, CAMERA_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int [] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        pickImageCamera();
                    } else {
                        Toast.makeText(getContext(), "Camera + Storage Permissions Required", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickImageGallery();
                    } else {
                        Toast.makeText(getContext(), "Storage Permission is Required", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }
    }
}
