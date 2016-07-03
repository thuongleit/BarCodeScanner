package com.whooo.babr.data.remote.upc;

import com.whooo.babr.data.exception.ItemNotFoundException;
import com.whooo.babr.data.exception.ItemParsingErrorException;
import com.whooo.babr.vo.Product;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by thuongle on 1/31/16.
 */
@Singleton
public class UpcParseService {

    @Inject
    public UpcParseService() {
    }

    public Observable<Product> getProduct(final String url) {
        return Observable.create(new Observable.OnSubscribe<Product>() {
            @Override
            public void call(Subscriber<? super Product> subscriber) {
                try {
                    Product product = new Product();
                    Document document = Jsoup.connect(url).get();

                    //check if item is valid or not
                    Elements pElements = document.select("p");
                    for (Element element : pElements) {
                        String aClass = element.attr("class");
                        if ("detailtitle".equals(aClass)) {
                            String detail = element.text();
                            if (detail != null && (detail.contains("incorrect") || detail.contains("invalid"))) {
                                //not found item
                                subscriber.onError(new ItemNotFoundException());
                            }
                        }
                    }

                    Elements info = document.select("tr");
                    for (Element element : info) {
                        String textInElement = element.text();

                        if (textInElement.startsWith("UPC-A")) {
                            product.upcA = textInElement;
                        } else if (textInElement.startsWith("EAN-13")) {
                            product.ean = textInElement;
                        } else if (textInElement.startsWith("Country of Registration")) {
                            product.country = textInElement;
                        } else if (textInElement.startsWith("Manufacture")) {
                            product.manufacture = textInElement;
                        } else if (textInElement.startsWith("Model")) {
                            product.model = textInElement;
                        }
                    }

                    Elements image = document.select("img");
                    for (Element element : image) {
                        String aClass = element.attr("class");
                        if ("amzn".equals(aClass)) {
                            String src = element.attr("src");
                            product.imageUrl = src;
                        }
                    }

                    subscriber.onNext(product);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    Timber.e(e, "Cannot get product from url %s", url);
                    subscriber.onError(new ItemParsingErrorException());
                }
            }
        });
    }
}
