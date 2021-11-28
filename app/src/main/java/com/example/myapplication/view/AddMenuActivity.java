package com.example.myapplication.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.SendDataMenu;
import com.example.myapplication.adapter.AdapterMenu;
import com.example.myapplication.api.ApiInsertMenu;
import com.example.myapplication.api.ApiMenu;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.databinding.ActivityAddMenuBinding;
import com.example.myapplication.model.MenuItem;
import com.example.myapplication.model.ResponseDTO;
import com.example.myapplication.utils.Contants;
import com.example.myapplication.utils.Extensions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddMenuActivity extends AppCompatActivity {
    ActivityAddMenuBinding binding;
    private static final int REQUEST_PHOTO_GALLERY = 100;
    private static final int REQUEST_CAPTURE_IMAGE = 110;
    private Uri mPhotoUri;
    Uri uriImage;
    private KProgressHUD hud;
    protected boolean suppressPasscodeLock = false;
    public static final int REQUEST_CODE_CAMERA = 456;
    public static final int SELECT_PICTURE = 123;
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    String type = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_status));


        String[] types = {Contants.rice, Contants.Noodle_dishes, Contants.salad, Contants.grilled, Contants.beef,
                Contants.pig, Contants.chicken, Contants.seafood, Contants.dessert, Contants.snacks, Contants.drink};


        binding = ActivityAddMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.imgBackAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                types);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinner.setAdapter(adapter);


        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = (String) parent.getAdapter().getItem(position);
                if (type.equals(Contants.rice)) {
                    type = "1";
                } else if (type.equals(Contants.Noodle_dishes)) {
                    type = "2";
                } else if (type.equals(Contants.salad)) {
                    type = "3";
                } else if (type.equals(Contants.grilled)) {
                    type = "4";
                } else if (type.equals(Contants.beef)) {
                    type = "5";
                } else if (type.equals(Contants.pig)) {
                    type = "6";
                } else if (type.equals(Contants.chicken)) {
                    type = "7";
                } else if (type.equals(Contants.seafood)) {
                    type = "8";
                } else if (type.equals(Contants.dessert)) {
                    type = "9";
                } else if (type.equals(Contants.snacks)) {
                    type = "10";
                } else if (type.equals(Contants.drink)) {
                    type = "11";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.imgHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChooseImage();
            }
        });

        binding.btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToFirebase(uriImage);
            }
        });

    }

    private void uploadToFirebase(Uri uri) {
        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        hud = KProgressHUD.create(AddMenuActivity.this)
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setLabel("Please wait")
                                .setCancellable(true)
                                .setAnimationSpeed(2)
                                .setDimAmount(0.5f)
                                .show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CompositeDisposable compositeDisposable = new CompositeDisposable();
                                Disposable disposable = RetrofitClient.getClient()
                                        .create(ApiInsertMenu.class)
                                        .insertMenu(binding.edtTenMon.getText().toString(), uri.toString(), binding.edtMoTa.getText().toString(),
                                                binding.edtNguyenLieu.getText().toString(), binding.edtSoChe.getText().toString(),
                                                binding.edtCachNau.getText().toString(), 0, false, type)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.newThread())
                                        .subscribe(new Consumer<ResponseDTO>() {
                                            @Override
                                            public void accept(ResponseDTO responseDTO) throws Exception {

                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                                throwable.printStackTrace();
                                            }
                                        });
                                compositeDisposable.add(disposable);
                                hud.dismiss();

                                Toast.makeText(AddMenuActivity.this, "Thành công !!!", Toast.LENGTH_SHORT).show();
                            }
                        }, 2000);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    public void dialogChooseImage() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        ArrayList<String> list = new ArrayList<String>();

        list.add("Máy ảnh ");
        list.add("Album");

        CharSequence[] items = list.toArray(new CharSequence[list.size()]);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(AddMenuActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_CAMERA);
                        break;
                    case 1:
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(AddMenuActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_PICTURE);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void callCamera() {
        String filename = System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, filename);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        mPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        suppressPasscodeLock = true;
        Bundle bundleOptions = new Bundle();
        startActivityForResult(intent, REQUEST_CAPTURE_IMAGE, bundleOptions);

    }

    private void callGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        suppressPasscodeLock = true;
        startActivityForResult(intent, REQUEST_PHOTO_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                callCamera();
            } else if (requestCode == SELECT_PICTURE) {
                callGallery();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_CANCELED) {
            getContentResolver().delete(mPhotoUri, null, null);
            return;
        }

        String path = null;
        Bitmap tmpImg = null;
        File cache = null;
        if (requestCode == REQUEST_PHOTO_GALLERY && resultCode == RESULT_OK) {
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                cache = Extensions.cache(this, in);
                path = cache.getAbsolutePath();

            } catch (FileNotFoundException e) {
                throw new AssertionError();
            }

            uriImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                tmpImg = this.decodeSampledBitmapFromUri(data.getData(), bitmap.getWidth(), bitmap.getHeight(), getContentResolver());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            final boolean existsData = data != null && data.getData() != null;
            Uri uri = existsData ? data.getData() : mPhotoUri;
            uriImage = uri;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                tmpImg = this.decodeSampledBitmapFromUri(uri, bitmap.getWidth(), bitmap.getHeight(), getContentResolver());
            } catch (IOException e) {
                e.printStackTrace();
            }
            path = this.getPath(getContentResolver(), uri);
        }
        if (path == null) {
            return;
        }

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (exif == null && tmpImg == null) {
            return;
        }

        Matrix mat = new Matrix();
        String exifDate = null;
        if (exif != null) {
            exifDate = exif.getAttribute(ExifInterface.TAG_DATETIME);
            mat = Extensions.asMatrixByOrientation(exif);
        }

        Bitmap img = null;
        try {
            img = Bitmap.createBitmap(tmpImg, 0, 0, tmpImg.getWidth(), tmpImg.getHeight(), mat, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (img.getConfig() != null) {
            switch (img.getConfig()) {
                case RGBA_F16:
                    img = img.copy(Bitmap.Config.ARGB_8888, true);
                    break;
            }
        }

        binding.imgHinhAnh.setImageBitmap(img);

        if (cache != null) cache.delete();
    }

    protected Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth,
                                                int reqHeight, ContentResolver contentResolver) throws IOException {
        InputStream in = contentResolver.openInputStream(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int width = options.outWidth / reqWidth + 1;
        int height = options.outHeight / reqHeight + 1;
        options.inSampleSize = Math.max(width, height);
        options.inJustDecodeBounds = false;
        in = contentResolver.openInputStream(uri);
        Bitmap tmpImg = BitmapFactory.decodeStream(in, null, options);
        in.close();
        return tmpImg;
    }

    protected String getPath(ContentResolver contentResolver, Uri uri) {
        String[] columns = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(uri, columns, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(0);
        cursor.close();
        return path;
    }
}