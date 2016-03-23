package com.thuongleit.babr.view.product;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.thuongleit.babr.R;
import com.thuongleit.babr.vo.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by thuongle on 11/24/15.
 */
public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder> {

    private final Context context;
    private final List<Product> values;
    private List<Product> listSearch = new ArrayList<>();
    private SelectMultiDeleteItemListener listener;
  //  private MultiSelector mMultiSelector;
    private SparseBooleanArray selectedItems;



    public ProductRecyclerAdapter(Context context, List<Product> values) {
        this.context = context;
        this.values = values;
        this.listSearch.addAll(values);
        selectedItems = new SparseBooleanArray();
    //    this.mMultiSelector=mMultiSelector;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_recycler_bar_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = values.get(position);
        holder.bindView(product);
    }

    @Override
    public int getItemCount() {
        return (values == null) ? 0 : values.size();
    }

    public void addItem(Product product) {
        this.values.add(product);
        notifyDataSetChanged();
    }

    public void addItems(List<Product> products) {
        this.values.addAll(products);
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        this.values.remove(position);
        notifyItemRemoved(position);
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
        }
        else {
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
    class ViewHolder extends RecyclerView.ViewHolder {

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
        @Bind(R.id.cb_itemProdut)
        CheckBox cbSelect;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
//            view.setOnClickListener(this);
//            view.setLongClickable(true);
//            view.setOnLongClickListener(this);
        }

        public void bindView(Product product) {
            if (!TextUtils.isEmpty(product.getName())) {
                textBarcodeTitle.setText(product.getName());
            }
            textBarcodeManufacture.setText(product.getManufacture());
            textBarcodeCountry.setText(product.getCountry());
            textSource.setText(product.getSource());

            if (!TextUtils.isEmpty(product.getImageUrl())) {
                Picasso.with(context).load(product.getImageUrl()).fit().into(imageBarView);
            }
            cbSelect.setChecked(product.isChecked());
        }

//        @Override
//        public void onClick(View v) {
//            if (mMultiSelector.tapSelection(this)){
//                toggleSelection(getAdapterPosition());
//                if (!values.get(getAdapterPosition()).isChecked()) {
//                    cbSelect.setChecked(true);
//                    values.get(getAdapterPosition()).setChecked(true);
//                }else {
//                    cbSelect.setChecked(false);
//                    values.get(getAdapterPosition()).setChecked(false);
//                }
//            }
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//            mMultiSelector.setSelected(this,true);
//            return true;
//        }
    }

    public void filter(String textSearch) {
        textSearch = textSearch.toLowerCase(Locale.getDefault());
        values.clear();
        if (textSearch.length() == 0) {
            values.addAll(listSearch);
        } else {
            for (Product product : listSearch) {
                if (product.getName()!=null&&product.getName().toLowerCase(Locale.getDefault()).contains(textSearch)) {
                    values.add(product);
                }
            }
        }
        notifyDataSetChanged();

    }

    public interface SelectMultiDeleteItemListener{

        void onToggle(int pos,boolean isChecked);

    }
}
