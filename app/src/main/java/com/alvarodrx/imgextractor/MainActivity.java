package com.alvarodrx.imgextractor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Elements images, characters;
    public static ProgressDialog progressDialog;
    ArrayList<Integer> valoresJuego;
    Button btn1, btn2, btn3, btn4;
    TextView tv1, tv2;
    int respCorrecta = 0;
    int puntaje = 0;
    int incorrectas = 0;
    Button[] btns;


    public void respuesta1(View v) throws InterruptedException { respuesta(0); }
    public void respuesta2(View v) throws InterruptedException { respuesta(1); }
    public void respuesta3(View v) throws InterruptedException { respuesta(2); }
    public void respuesta4(View v) throws InterruptedException { respuesta(3); }


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadDoc downloadTask = new DownloadDoc();
        Document doc = null;
        try {
            doc = downloadTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        characters = doc.select("div.character");
        images = doc.select(".line.reparto .portrait img");

        Log.i("Result", ""+characters.get(0).text());
        Log.d("URL", ""+images.get(0).absUrl("src"));


        imageView = findViewById(R.id.imageView);
        btn1 = findViewById(R.id.btn1);
        btn1.setBackgroundColor(R.color.color1);
        btn2 = findViewById(R.id.btn2);
        btn2.setBackgroundColor(R.color.color1);
        btn3 = findViewById(R.id.btn3);
        btn3.setBackgroundColor(R.color.color1);
        btn4 = findViewById(R.id.btn4);
        btn4.setBackgroundColor(R.color.color1);
        btns = new Button[]{btn1, btn2, btn3, btn4};
        tv1 = findViewById(R.id.textView);
        tv2 = findViewById(R.id.textView2);

        tv1.setText("Correctas = "+ puntaje);
        tv2.setText("Incorrectas = "+ incorrectas);
        nuevoQuiz();
    }


    @SuppressLint("ResourceAsColor")
    public void respuesta(int resp) throws InterruptedException {
        if(respCorrecta == resp){
            puntaje++;
            Context context = getApplicationContext();
            CharSequence text = "Respuesta correcta!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.TOP, 0, 20);
            toast.show();
            btns[resp].setBackgroundColor(Color.GREEN);
            Thread.sleep(1000);
            btns[resp].setBackgroundColor(R.color.color1);
            tv1.setText("Correctas = "+ puntaje);

        }
        else{
            incorrectas++;
            Context context = getApplicationContext();
            CharSequence text = "Respuesta incorrecta!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.TOP, 0, 20);
            toast.show();
            btns[resp].setBackgroundColor(Color.RED);
            Thread.sleep(1000);
            btns[resp].setBackgroundColor(R.color.color1);
            tv2.setText("Incorrectas = "+ incorrectas);
        }
        nuevoQuiz();
    }

    public void nuevoQuiz(){
        int rndMax = characters.size()-50;
        Random rand = new Random();
        valoresJuego = new ArrayList<>();
        int n = rand.nextInt(rndMax-1);
        respCorrecta = n;

        valoresJuego.add(n);
        for(int i = 0; i < 3; i++){
            n = rand.nextInt(rndMax-1);
            while(valoresJuego.contains(n)){
                n = rand.nextInt(rndMax-1);
            }
            valoresJuego.add(n);
        }
        //Collections.shuffle(valoresJuego);
        new ImageDownloader(imageView).execute(images.get(valoresJuego.get(0)).absUrl("src"));
        Collections.shuffle(valoresJuego);
        btn1.setText(characters.get(valoresJuego.get(0)).text());
        btn2.setText(characters.get(valoresJuego.get(1)).text());
        btn3.setText(characters.get(valoresJuego.get(2)).text());
        btn4.setText(characters.get(valoresJuego.get(3)).text());
        respCorrecta = valoresJuego.indexOf(respCorrecta);
        //Log.i('resp:',""+respCorrecta);

    }

}
