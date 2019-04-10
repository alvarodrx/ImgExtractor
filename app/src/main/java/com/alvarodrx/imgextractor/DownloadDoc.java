package com.alvarodrx.imgextractor;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.IOException;
public class DownloadDoc extends AsyncTask<Void, Void, Document> {

    @Override
    protected Document doInBackground(Void... params) {
        String title ="";
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.formulatv.com/series/game-of-thrones/reparto/").timeout(30000).get();
            title = doc.title();
            Log.i("Result", title);
            Log.i("Result", doc.html());
            //System.out.print(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }



}