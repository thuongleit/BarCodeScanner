package com.jokotech.babr.data.remote.amazon;

import com.jokotech.babr.vo.Product;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import timber.log.Timber;

/**
 * Created by thuongle on 2/25/16.
 */
@Singleton
public class AmazonParseService {

    private static final String IMAGE_URL_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    @Inject
    public AmazonParseService() {
    }

    public Observable<Product> parse(String amazonUrl) {
        return Observable.create((Observable.OnSubscribe<Product>) subscriber -> {
            try {
                Product product = new Product();
                Document document = Jsoup.connect(amazonUrl).get();

                //check if item is valid or not
                Elements spanElements = document.select("span");
                for (Element element : spanElements) {
                    String idElement = element.attr("id");
                    if ("productTitle".equals(idElement)) {
                        String productTitle = element.text();
                        product.name = productTitle;
                        product.source = "amazon.com";
                        product.listId = "a";

                    }
                }

                //parsing product image
                Elements divElements = document.select("div");
                for (Element element : divElements) {
                    String id = element.attr("id");
                    if ("leftCol".equals(id)) {
                        element.getAllElements();
                        Elements imgElements = element.getElementsByTag("img");
                        if (imgElements != null && !imgElements.isEmpty()) {
                            for (Element imgElement : imgElements) {
                                String img = imgElement.attr("data-a-dynamic-image");
                                if (!"".equals(img)) {
                                    String url = img.substring(img.indexOf("http://"), img.indexOf(".jpg")) + ".jpg";
                                    if (url.matches(IMAGE_URL_PATTERN)) {
                                        product.imageUrl = url;
                                    } else {
                                        url = img.substring(img.indexOf("http://"), img.indexOf(".jpeg")) + ".jpeg";
                                        if (url.matches(IMAGE_URL_PATTERN)) {
                                            product.imageUrl = url;
                                        } else {
                                            url = img.substring(img.indexOf("http://"), img.indexOf(".png")) + ".png";
                                            if (url.matches(IMAGE_URL_PATTERN)) {
                                                product.imageUrl = url;
                                            }

                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                subscriber.onNext(product);
                subscriber.onCompleted();
            } catch (IOException e) {
                Timber.e(e, "Cannot get product from url %s", amazonUrl);
                subscriber.onError(e);
            }
        });
    }
}
