package com.whooo.babr.view.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;
import com.squareup.picasso.Picasso;
import com.whooo.babr.R;
import com.whooo.babr.data.remote.ParseServiceOK;
import com.whooo.babr.vo.Product;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BarViewActivity extends BaseActivity {

    private static final String EXTRA_PRODUCT =
            "com.whooo.barscanner.view.product.BarViewActivity.EXTRA_PRODUCT";
    @Bind(R.id.image_bar_view)
    ImageView mImageBarView;
    @Bind(R.id.text_barcode_title)
    TextView mTextBarcodeTitle;
    @Bind(R.id.text_barcode_manufacture)
    TextView mTextBarcodeManufacture;
    @Bind(R.id.text_barcode_country)
    TextView mTextBarcodeCountry;

    @Inject
    ParseServiceOK mParseServiceOK;

    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_view);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProduct = getIntent().getParcelableExtra(EXTRA_PRODUCT);
        bindView(mProduct);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add: {
                Intent intent = getIntent();

//                if (ParseUser.getCurrentUser() != null) {
//                    //put data to server
//                    mParseServiceOK.saveProduct(mProduct);
//                }

                intent.putExtra(EXTRA_PRODUCT, mProduct);
                setResult(RESULT_OK, intent);
                this.finish();
                return true;
            }
        }
        return false;
    }

    private void bindView(Product product) {
        mTextBarcodeTitle.setText(product.model);
        mTextBarcodeManufacture.setText(product.manufacture);
        mTextBarcodeCountry.setText(product.country);

        if (!TextUtils.isEmpty(product.imageUrl)) {
            Picasso.with(this).load(product.imageUrl).into(mImageBarView);
        }
    }
}
