package com.whooo.babr.view.history;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whooo.babr.R;
import com.whooo.babr.config.Constant;
import com.whooo.babr.view.qrgenerate.QRCode;
import com.whooo.babr.vo.CheckoutHistory;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ThongLe on 3/31/2016.
 */
public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.HistoryHolder> {


    private Context context;
    private List<CheckoutHistory> list;

    public AdapterHistory(Context context, List<CheckoutHistory> list) {
        this.context = context;
        this.list = list;
    }

    public void addItems(List<CheckoutHistory> arrayList) {
        this.list = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        CheckoutHistory history = list.get(position);
        Bitmap myBitmap = QRCode.from(history.listId).bitmap();
        holder.ivQrCode.setImageBitmap(myBitmap);
        holder.textGroupTitle.setText(history.name);
        holder.textBarcodeCountry.setVisibility(View.GONE);
        holder.textBarcodeManufacture.setVisibility(View.GONE);
        holder.textSize.setText(String.valueOf(history.size));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(Constant.KEY_LIST_ID, history.listId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }


    public class HistoryHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.image_bar_view)
        ImageView ivQrCode;
        @Bind(R.id.text_barcode_title)
        TextView textGroupTitle;
        @Bind(R.id.text_source)
        TextView textSize;
        @Bind(R.id.text_barcode_manufacture)
        TextView textBarcodeManufacture;
        @Bind(R.id.text_barcode_country)
        TextView textBarcodeCountry;

        public HistoryHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
