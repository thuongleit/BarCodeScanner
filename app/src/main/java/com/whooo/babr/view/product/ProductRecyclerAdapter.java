package com.whooo.babr.view.product;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whooo.babr.R;
import com.whooo.babr.util.RoundedTransformation;
import com.whooo.babr.vo.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder> {

    private final Context context;
    private final List<Product> mProducts;
    private final List<Product> valuesPendingRemoval = new ArrayList<>();
    private List<Product> listSearch = new ArrayList<>();
    private SparseBooleanArray selectedItems;
    private boolean isUndoOn = true;
    private Handler handler = new Handler();
    private HashMap<Product, Runnable> pendingRunnables = new HashMap<>();
    private static final int PENDING_REMOVAL_TIMEOUT = 2000;

    public boolean isUndoOn() {
        return true;
    }


    public ProductRecyclerAdapter(Context context, List<Product> values) {
        this.context = context;
        this.mProducts = values;
        this.listSearch.addAll(values);
        selectedItems = new SparseBooleanArray();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_recycler_bar_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = mProducts.get(position);

        holder.bindView(product, holder.getAdapterPosition());
        holder.itemView.setActivated(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return (mProducts == null) ? 0 : mProducts.size();
    }

    public void addItem(Product product) {
        this.mProducts.add(product);
        notifyItemInserted(mProducts.size());
    }

    public void addItems(List<Product> products) {
        int oldIndex = mProducts.size();
        this.mProducts.addAll(products);
        notifyItemRangeInserted(oldIndex, mProducts.size());
    }

    public void deleteItem(int position) {
        Product product = mProducts.get(position);
        if (valuesPendingRemoval.contains(product)) {
            valuesPendingRemoval.remove(product);
        }
        if (mProducts.contains(product)) {
            this.mProducts.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void deleteAll() {
        this.mProducts.clear();
        notifyDataSetChanged();
    }

    public boolean isPendingRemoval(int position) {
        Product product = mProducts.get(position);
        return valuesPendingRemoval.contains(product);
    }


    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image_bar_view)
        ImageView imageBarView;
        @Bind(R.id.text_barcode_title)
        TextView textBarcodeTitle;
        @Bind(R.id.text_barcode_manufacture)
        TextView textBarcodeManufacture;
        @Bind(R.id.text_barcode_country)
        TextView textBarcodeCountry;
        @Bind(R.id.text_source)
        TextView textSource;
        @Bind(R.id.item_recycler_container)
        RelativeLayout container;
        @Bind(R.id.undo_button)
        Button btnUndo;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindView(Product product, int position) {

            if (valuesPendingRemoval.contains(product)) {
                container.setVisibility(View.GONE);
                itemView.setBackgroundColor(Color.RED);
                btnUndo.setVisibility(View.VISIBLE);

                btnUndo.setOnClickListener(v -> {
                    Runnable pendingRemovalRunnable = pendingRunnables.get(product);
                    pendingRunnables.remove(product);
                    if (pendingRemovalRunnable != null)
                        handler.removeCallbacks(pendingRemovalRunnable);
                    valuesPendingRemoval.remove(product);
                    notifyItemChanged(mProducts.indexOf(product));
                });
            } else {

                itemView.setBackgroundColor(Color.WHITE);
                btnUndo.setVisibility(View.GONE);
                btnUndo.setOnClickListener(null);

                if (!TextUtils.isEmpty(product.name)) {
                    textBarcodeTitle.setText(product.name);
                }
                if (!TextUtils.isEmpty(product.manufacture)) {
                    textBarcodeManufacture.setText(product.manufacture);
                } else {
                    textBarcodeManufacture.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(product.country)) {
                    textBarcodeCountry.setText(product.country);
                } else {
                    textBarcodeCountry.setVisibility(View.GONE);
                }
                textSource.setText(product.source);

                if (!TextUtils.isEmpty(product.imageUrl)) {
                    Picasso.with(context).load(product.imageUrl).centerCrop().fit().transform(new RoundedTransformation()).into(imageBarView);
                }

            }
        }


    }

    public void pendingRemoval(int position) {
        final Product item = mProducts.get(position);
        if (!valuesPendingRemoval.contains(item)) {
            valuesPendingRemoval.add(item);
            notifyItemChanged(position);
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    deleteItem(mProducts.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }


    public void filter(String textSearch) {
        textSearch = textSearch.toLowerCase(Locale.getDefault());
        mProducts.clear();
        if (textSearch.length() == 0) {
            mProducts.addAll(listSearch);
        } else {
            for (Product product : listSearch) {
                if (product.name != null && product.name.toLowerCase(Locale.getDefault()).contains(textSearch)) {
                    mProducts.add(product);
                }
            }
        }
        notifyDataSetChanged();

    }

}
