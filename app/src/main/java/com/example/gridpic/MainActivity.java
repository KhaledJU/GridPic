package com.example.gridpic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gridpic.catAPI.CatResponce;
import com.example.gridpic.catAPI.CatService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    int position=0;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==101 && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            imageView.setImageBitmap(image);
        }
        else if(requestCode==100 && resultCode==RESULT_OK){
            Uri uri=data.getData();
            imageView.setImageURI(uri);
        }

    }

    public void fetchCat(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                CatService service  = retrofit.create(CatService.class);
                service.get("json").enqueue(new Callback<List<CatResponce>>() {
                    @Override
                    public void onResponse(Call<List<CatResponce>> call, Response<List<CatResponce>> response) {
                        if(!response.isSuccessful()){
                            ResponseBody errorBody = response.errorBody();
                            try{
                                Log.d("RESPONCE ERROR",errorBody.string());
                            }catch (Exception error){
                                Log.d("RESPONCEERR",error.getLocalizedMessage());
                            }
                            return;
                        }
                        Picasso.with(getApplicationContext()).load(response.body().get(0).getUrl()).into(imageView);
            }

            @Override
            public void onFailure(Call<List<CatResponce>> call, Throwable t) {
                Log.d("RESPONCEFAIL",t.getMessage());
            }
        });
    }

    public void onClick(View view){
        imageView = (ImageView) view;
        position=0;
        String[] list = getResources().getStringArray(R.array.Choices);
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Select your choice");
        mBuilder.setCancelable(false);

        mBuilder.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                position=i;
            }
        });
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (position){
                    case 0:
                        Intent imageTaken= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(imageTaken.resolveActivity(getPackageManager())!=null)
                            startActivityForResult(imageTaken,101);

                        break;
                    case 1:
                        Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(gallery,100);
                        break;

                    case 2:
                        fetchCat();
                        break;
                    case 3:
                        imageView.setImageResource(R.drawable.ic_launcher_foreground);
                        break;
                    default: Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        mBuilder.show();
    }

}
