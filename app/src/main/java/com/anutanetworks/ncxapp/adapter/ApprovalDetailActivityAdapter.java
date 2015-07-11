package com.anutanetworks.ncxapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.model.ApprovalDetailItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aakash on 7/9/2015.
 */
public class ApprovalDetailActivityAdapter extends ArrayAdapter {
    private Context context;
    private List<ApprovalDetailItem> items = new ArrayList<>();

    private LayoutInflater inflater;

    private int HEADER_ITEM = 0;
    private int DETAIL_DATA_ITEM = 1;
    private int DEVICE_ITEM=2;
    private int COMMAND_DATA_ITEM =3;


    public ApprovalDetailActivityAdapter(Context context, ArrayList<ApprovalDetailItem> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        ApprovalDetailItem approvalDetailItem = items.get(position);
        if(approvalDetailItem.isHeader()){
            return HEADER_ITEM;
        }else if(approvalDetailItem.isDetailData()){
            return DETAIL_DATA_ITEM;
        } else if(approvalDetailItem.isDevice()){
            return DEVICE_ITEM;
        } else {
            return COMMAND_DATA_ITEM;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridItemView;
        if(convertView == null) {
            gridItemView = new View(context);
            gridItemView = inflater.inflate(R.layout.activity_approval_detail_item, null);
        }else{
            gridItemView = (View)convertView;
        }

        ApprovalDetailItem approvalDetailItem = items.get(position);
        int itemViewType = getItemViewType(position);
        TextView rowTextView = (TextView) gridItemView
                .findViewById(R.id.value);
        rowTextView.setText(approvalDetailItem.getValue());
        if(itemViewType == HEADER_ITEM) {
            rowTextView.setTextColor(Color.parseColor("#BEFF2917"));
            rowTextView.setTextSize(20);
        }else if(itemViewType == DETAIL_DATA_ITEM){
            rowTextView.setPadding(20, 0, 0, 0);
            rowTextView.setTextSize(13);
        } else if(itemViewType == COMMAND_DATA_ITEM){
            rowTextView.setPadding(20, 0, 0, 0);
            rowTextView.setTextSize(13);
        } else if(itemViewType == DEVICE_ITEM) {
            rowTextView.setPadding(12, 0, 0, 0);
            rowTextView.setTextColor(Color.parseColor("#FF182EF4"));
            rowTextView.setTextSize(15);
        }
        return gridItemView;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void updateItems(List<ApprovalDetailItem> items) {
        this.addAll(items);
        notifyDataSetChanged();
    }
}
