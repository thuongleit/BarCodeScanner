package com.thuongleit.babr.view.product;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thuongleit.babr.R;
import com.thuongleit.babr.vo.UpcProduct;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by thuongle on 11/24/15.
 */
public class BarViewRecyclerAdapter extends RecyclerView.Adapter<BarViewRecyclerAdapter.ViewHolder> {

    private final Context context;
    private final List<UpcProduct> values;

    public BarViewRecyclerAdapter(Context context, List<UpcProduct> values) {
        this.context = context;
        this.values = values;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_recycler_bar_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UpcProduct upcProduct = values.get(position);
        holder.bindView(upcProduct);
    }

    @Override
    public int getItemCount() {
        return (values == null) ? 0 : values.size();
    }

    public void addItem(UpcProduct upcProduct) {
        this.values.add(upcProduct);
        notifyDataSetChanged();
    }

    public void addItems(List<UpcProduct> upcProducts) {
        this.values.addAll(upcProducts);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image_bar_view)
        ImageView imageBarView;
        @Bind(R.id.text_barcode_title)
        TextView textBarcodeTitle;
        @Bind(R.id.text_barcode_manufacture)
        TextView textBarcodeManufacture;
        @Bind(R.id.text_barcode_country)
        TextView textBarcodeCountry;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindView(UpcProduct upcProduct) {
            textBarcodeTitle.setText(upcProduct.getModel());
            textBarcodeManufacture.setText(upcProduct.getManufacture());
            textBarcodeCountry.setText(upcProduct.getCountry());

            if (!TextUtils.isEmpty(upcProduct.getImage())) {
                Picasso.with(context).load(upcProduct.getImage()).into(imageBarView);
            }
        }
    }
}
