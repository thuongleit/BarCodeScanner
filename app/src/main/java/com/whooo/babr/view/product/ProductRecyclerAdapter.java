package com.whooo.babr.view.product;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import com.whooo.babr.util.TypefacesUtils;
import com.whooo.babr.util.swipe.ItemTouchHelperAdapter;
import com.whooo.babr.util.swipe.ItemTouchHelperViewHolder;
import com.whooo.babr.vo.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private final Context context;
    private final List<Product> mProducts;
    private List<Product> listSearch = new ArrayList<>();
    private SparseBooleanArray selectedItems;
    private View mRootView;



    public ProductRecyclerAdapter(Context context, List<Product> values) {
        this.context = context;
        this.mProducts = values;
        this.listSearch.addAll(values);
        selectedItems = new SparseBooleanArray();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_recycler_bar_view, parent, false);
        mRootView=view;
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
        if (mProducts.contains(product)) {
            this.mProducts.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void deleteAll() {
        this.mProducts.clear();
        notifyDataSetChanged();
    }



    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(final int position) {
        final Product product = new Product();
            product.name = mProducts.get(position).name;
        product.manufacture = mProducts.get(position).manufacture;
        product.country = mProducts.get(position).country;
        product.source = mProducts.get(position).source;
        product.imageUrl = mProducts.get(position).imageUrl;

        deleteItem(position);

        final Snackbar snackbar = Snackbar.make(mRootView, "Item deteted", Snackbar.LENGTH_LONG)
                .setActionTextColor(ContextCompat.getColor(context, R.color.white))
                .setAction("Undo", view -> {
                    mProducts.add(position, product);
                    notifyItemInserted(position);
                });

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        TextView tvSnack = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        TextView tvSnackAction = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_action);
        tvSnack.setTextColor(Color.WHITE);
        tvSnack.setTypeface(TypefacesUtils.getRobotoMedium(context));
        tvSnackAction.setTypeface(TypefacesUtils.getRobotoMedium(context));
        snackbar.show();
        Handler handler=new Handler();
        handler.postDelayed(()->{
            snackbar.dismiss();
        },2500);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{

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


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindView(Product product, int position) {

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


        @Override
        public void onItemSelected(Context context) {
            container.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        @Override
        public void onItemClear(Context context) {
            container.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
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
