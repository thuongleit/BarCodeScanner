package com.example.barscanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.barscanner.model.BarCode;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BarViewActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.image_bar_view)
    ImageView imageBarView;
    @Bind(R.id.text_barcode_title)
    TextView textBarcodeTitle;
    @Bind(R.id.text_barcode_manufacture)
    TextView textBarcodeManufacture;
    @Bind(R.id.text_barcode_country)
    TextView textBarcodeCountry;
    private BarCode barCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_view);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Parcelable dataParcelable = getIntent().getParcelableExtra("data");
        barCode = Parcels.unwrap(dataParcelable);
        bindView(barCode);
    }

    private void bindView(BarCode barCode) {
        textBarcodeTitle.setText(barCode.model);
        textBarcodeManufacture.setText(barCode.manufacture);
        textBarcodeCountry.setText(barCode.country);

        if (!TextUtils.isEmpty(barCode.image)) {
            Picasso.with(this).load(barCode.image).into(imageBarView);
        }
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
                intent.putExtra("data", Parcels.wrap(barCode));
                setResult(RESULT_OK, intent);
                this.finish();
                return true;
            }
        }
        return false;
    }
}
