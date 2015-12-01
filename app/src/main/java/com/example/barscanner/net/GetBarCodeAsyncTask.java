package com.example.barscanner.net;

import android.os.AsyncTask;

import com.example.barscanner.model.BarCode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GetBarCodeAsyncTask extends AsyncTask<String, Void, BarCode> {

    private OnUpdateUICallback callback;

    public GetBarCodeAsyncTask(OnUpdateUICallback callback) {
        this.callback = callback;
    }

    @Override
    protected BarCode doInBackground(String... params) {
        try {
            BarCode barCode = new BarCode();
            Document document = Jsoup.connect(params[0]).get();

            //check if item is valid or not
            Elements pElements = document.select("p");
            for (Element element : pElements) {
                String aClass = element.attr("class");
                if ("detailtitle".equals(aClass)) {
                    String detail = element.text();
                    if (detail != null && (detail.contains("incorrect") || detail.contains("invalid"))) {
                        //not found item
                        return null;
                    }
                }
            }

            Elements info = document.select("tr");
            for (Element element : info) {
                String textInElement = element.text();

                if (textInElement.startsWith("UPC-A")) {
                    barCode.upcA = textInElement;
                } else if (textInElement.startsWith("EAN-13")) {
                    barCode.ean = textInElement;
                } else if (textInElement.startsWith("Country of Registration")) {
                    barCode.country = textInElement;
                } else if (textInElement.startsWith("Manufacture")) {
                    barCode.manufacture = textInElement;
                } else if (textInElement.startsWith("Model")) {
                    barCode.model = textInElement;
                }
            }

            Elements image = document.select("img");
            for (Element element : image) {
                String aClass = element.attr("class");
                if ("amzn".equals(aClass)) {
                    String src = element.attr("src");
                    barCode.image = src;
                }
            }
            return barCode;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(BarCode barCode) {
        if (callback != null) {
            callback.onUpdateUI(barCode);
        }
    }

    public interface OnUpdateUICallback {
        void onUpdateUI(BarCode barCode);
    }
}