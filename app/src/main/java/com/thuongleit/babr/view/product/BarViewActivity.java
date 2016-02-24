package com.thuongleit.babr.view.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.thuongleit.babr.R;
import com.thuongleit.babr.data.remote.ParseService;
import com.thuongleit.babr.view.base.ToolbarActivity;
import com.thuongleit.babr.vo.Product;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BarViewActivity extends ToolbarActivity {

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
    ParseService mParseService;

    private Product mProduct;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bar_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        getComponent().inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProduct = getIntent().getParcelableExtra(EXTRA_PRODUCT);
        bindView(mProduct);
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

                if (ParseUser.getCurrentUser() != null) {
                    //put data to server
                    mParseService.saveProduct(mProduct);
                }

                intent.putExtra(EXTRA_PRODUCT, mProduct);
                setResult(RESULT_OK, intent);
                this.finish();
                return true;
            }
        }
        return false;
    }

    private void bindView(Product product) {
        mTextBarcodeTitle.setText(product.getModel());
        mTextBarcodeManufacture.setText(product.getManufacture());
        mTextBarcodeCountry.setText(product.getCountry());

        if (!TextUtils.isEmpty(product.getImage())) {
            Picasso.with(this).load(product.getImage()).into(mImageBarView);
        }
    }
}
