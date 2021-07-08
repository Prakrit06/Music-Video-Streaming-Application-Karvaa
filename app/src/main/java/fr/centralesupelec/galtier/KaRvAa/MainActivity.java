package fr.centralesupelec.galtier.KaRvAa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1;
    ArrayList<MusicFiles> musicFiles;
    MediaPlayer music;
    WebView webView;
    private ImageButton imageButton;
    private ImageButton imageButtonYoutube;
    private ImageButton imageButtonNetflix;
    private ImageButton imageButtonRadio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        music = MediaPlayer.create(this, R.raw.padmavati);
        //webView = (WebView) findViewById(R.id.webView);
        //WebSettings webSettings = webView.getSettings();
        //webSettings.setJavaScriptEnabled(true);

        imageButton = (ImageButton) findViewById(R.id.imageButtonPlaylist);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlaylist();
            }
        });

        imageButtonYoutube = (ImageButton) findViewById(R.id.imageButtonVideo);
        imageButtonYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYoutube();
            }
        });

        imageButtonNetflix = (ImageButton) findViewById(R.id.imageButtonTv);
        imageButtonNetflix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNetflix();
            }
        });

        imageButtonRadio = (ImageButton) findViewById(R.id.imageButtonRadio);
        imageButtonRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRadio();
            }
        });



        permission();

           }
    public void openPlaylist(){
        Intent intent = new Intent(this, playlist.class);
        startActivity(intent);
    }

    public void openYoutube(){
                Intent intent = new Intent(this, youtube.class);
                startActivity(intent);
           }

    public void openNetflix(){
        Intent intent = new Intent(this, netflix.class);
        startActivity(intent);
    }
    public void openRadio(){
        Intent intent = new Intent(this, radio.class);
        startActivity(intent);
    }

    private void permission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
            , REQUEST_CODE);
        }
        else{
            Toast.makeText(this, "Permission Granted !", Toast.LENGTH_SHORT).show();
            musicFiles = getAllAudio(this);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted !", Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , REQUEST_CODE);
            }
        }
    }

    public void playMusic (View v) {
    music.start();
    }
    public void pauseMusic (View v) {
        if(music.isPlaying())
        music.pause();
    }
    public void navigate (View v){
        webView.loadUrl("https://gaana.com/");
    }
    public void navigate2(View v){
        webView.loadUrl("https://www.youtube.com/");
    }
    public void navigate3(View v){
        webView.loadUrl("http://www.radioindia.in/");
    }
    public void navigate4(View v){
        webView.loadUrl("https://www.netflix.com/in/");
    }

    public static ArrayList<MusicFiles> getAllAudio(Context context){
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            while (cursor.moveToNext())
            {
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);

                MusicFiles musicfiles = new MusicFiles(path, title, artist, album, duration);
                Log.e("Path : "+path, "Album : "+album);
                tempAudioList.add(musicfiles);
             }
            cursor.close();
        }
        return tempAudioList;
    }
}