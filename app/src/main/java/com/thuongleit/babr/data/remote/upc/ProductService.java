package com.thuongleit.babr.data.remote.upc;

import com.thuongleit.babr.data.exception.ItemNotFoundException;
import com.thuongleit.babr.data.exception.ItemParsingErrorException;
import com.thuongleit.babr.vo.UpcProduct;

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
public class ProductService {

    @Inject
    public ProductService() {
    }

    public Observable<UpcProduct> getProduct(final String url) {
        return Observable.create(new Observable.OnSubscribe<UpcProduct>() {
            @Override
            public void call(Subscriber<? super UpcProduct> subscriber) {
                try {
                    UpcProduct upcProduct = new UpcProduct();
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
                            upcProduct.setUpcA(textInElement);
                        } else if (textInElement.startsWith("EAN-13")) {
                            upcProduct.setEan(textInElement);
                        } else if (textInElement.startsWith("Country of Registration")) {
                            upcProduct.setCountry(textInElement);
                        } else if (textInElement.startsWith("Manufacture")) {
                            upcProduct.setManufacture(textInElement);
                        } else if (textInElement.startsWith("Model")) {
                            upcProduct.setModel(textInElement);
                        }
                    }

                    Elements image = document.select("img");
                    for (Element element : image) {
                        String aClass = element.attr("class");
                        if ("amzn".equals(aClass)) {
                            String src = element.attr("src");
                            upcProduct.setImage(src);
                        }
                    }

                    subscriber.onNext(upcProduct);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    Timber.e(e, "Cannot get product from url %s", url);
                    subscriber.onError(new ItemParsingErrorException());
                }

            }
        });
    }
}
