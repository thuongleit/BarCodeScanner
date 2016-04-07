package com.thuongleit.babr.view.history;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thuongleit.babr.R;
import com.thuongleit.babr.config.Constant;
import com.thuongleit.babr.vo.Product;
import com.thuongleit.babr.vo.ProductHistory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ThongLe on 3/31/2016.
 */
public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.HistoryHolder> {


    private Context context;
    private List<ProductHistory> list;

    public AdapterHistory(Context context, List<ProductHistory> list){
        this.context=context;
        this.list=list;
    }

    public void addItems(List<ProductHistory> arrayList){
        this.list=arrayList;
        notifyDataSetChanged();
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_recycler_history,parent,false));
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        ProductHistory history=list.get(position);
        holder.tvName.setText(history.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DetailActivity.class);
                intent.putExtra(Constant.KEY_LIST_ID,history.getListId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }


    public class HistoryHolder extends RecyclerView.ViewHolder{


        @Bind(R.id.tvName)
        TextView tvName;
        public HistoryHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
