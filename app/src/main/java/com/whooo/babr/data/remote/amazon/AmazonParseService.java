package com.whooo.babr.data.remote.amazon;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whooo.babr.config.Constant;
import com.whooo.babr.data.remote.ParseService;
import com.whooo.babr.data.remote.amazon.model.AmazonApisResponse;
import com.whooo.babr.vo.Product;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

@Singleton
public class AmazonParseService implements ParseService {

    private static final String IMAGE_URL_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
    private final RetrofitService mService;

    @Inject
    public AmazonParseService(AmazonParseService.RetrofitService service) {
        mService = service;
    }

    @Override
    public Observable<List<Product>> searchProductsByCode(@NonNull String code) {
        String signedUrl = buildSignedUrl(code);

        if (signedUrl != null) {
            return mService
                    .getProducts(signedUrl)
                    .flatMap(response -> Observable.just(response.rootItem))
                    .filter(rootItem -> rootItem != null && rootItem.products != null && !rootItem.products.isEmpty())
                    .flatMap(rootItem -> Observable.from(rootItem.products))
                    .filter(amazonProduct -> amazonProduct.attributes != null && amazonProduct.image != null)
                    .flatMap(amazonProduct -> {
                        Product product = new Product();

                        product.name = amazonProduct.attributes.title;
                        product.upcA = amazonProduct.attributes.upc;
                        product.imageUrl = amazonProduct.image.url;
                        product.manufacture = amazonProduct.attributes.manufacturer;

                        return Observable.just(product);
                    })
                    .toSortedList();
        }
        return Observable.empty();
    }

    @Nullable
    private String buildSignedUrl(@NonNull String code) {
        String signedUrl = null;
        try {
            AmazonSignedRequestsHelper signer = AmazonSignedRequestsHelper.
                    getInstance(Constant.AWS_ACCESS_KEY_ID, Constant.AWS_SECRET_KEY);
            signedUrl = signer.sign(code);
        } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            // if the key is invalid due to a programming error
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return signedUrl;
    }

//    private Observable<Product> parseProductFromSite(AmazonProduct amazonProduct) {
//        Product product = new Product();
//        try {
//            Document document = Jsoup.connect(amazonProduct.detailPageURL).get();
//
//            //check if item is valid or not
//            Elements spanElements = document.select("span");
//            for (Element element : spanElements) {
//                String idElement = element.attr("id");
//                if ("productTitle".equals(idElement)) {
//                    String productTitle = element.text();
//                    product.name = productTitle;
//                    product.source = "amazon.com";
//                    product.listId = "a";
//                }
//            }
//
//            //parsing product image
//            Elements divElements = document.select("div");
//            for (Element element : divElements) {
//                String id = element.attr("id");
//                if ("leftCol".equals(id)) {
//                    element.getAllElements();
//                    Elements imgElements = element.getElementsByTag("img");
//                    if (imgElements != null && !imgElements.isEmpty()) {
//                        for (Element imgElement : imgElements) {
//                            String img = imgElement.attr("data-a-dynamic-image");
//                            if (!"".equals(img)) {
//                                String url = img.substring(img.indexOf("http://"), img.indexOf(".jpg")) + ".jpg";
//                                if (url.matches(IMAGE_URL_PATTERN)) {
//                                    product.imageUrl = url;
//                                } else {
//                                    url = img.substring(img.indexOf("http://"), img.indexOf(".jpeg")) + ".jpeg";
//                                    if (url.matches(IMAGE_URL_PATTERN)) {
//                                        product.imageUrl = url;
//                                    } else {
//                                        url = img.substring(img.indexOf("http://"), img.indexOf(".png")) + ".png";
//                                        if (url.matches(IMAGE_URL_PATTERN)) {
//                                            product.imageUrl = url;
//                                        }
//
//                                    }
//                                }
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            return Observable.empty();
//        }
//
//        return Observable.just(product);
//    }

    public interface RetrofitService {
        @GET
        Observable<AmazonApisResponse> getProducts(@Url String signedUrl);
    }
}
