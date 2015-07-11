package com.anutanetworks.ncxapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.model.Approval;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aakash on 7/5/2015.
 */
public class ApprovalGridAdapter extends ArrayAdapter {

    private Context context;
    private List<Approval> approvals = new ArrayList<>();
    private LayoutInflater inflater;


    public ApprovalGridAdapter(Context context, ArrayList<Approval> approvals) {
        super(context, android.R.layout.simple_list_item_1, approvals);
        this.context = context;
        this.approvals = approvals;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return approvals.size();
    }

    @Override
    public Object getItem(int position) {
        return approvals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridItemView;

        if (convertView == null) {
            gridItemView = new View(context);
            gridItemView = inflater.inflate(R.layout.fragment_approval_grid_item, null);
            Approval app = approvals.get(position);
            TextView textView = (TextView) gridItemView
                    .findViewById(R.id.actions);
            //textView.setText(app.getApproved());
            TextView textView2 = (TextView) gridItemView
                    .findViewById(R.id.description);
            textView2.setText(app.getDescription());
            TextView textView3 = (TextView) gridItemView
                    .findViewById(R.id.originator);
            textView3.setText(app.getOriginator());

            if ("Rejected".equals(app.getApproved())) {
                textView.setText(app.getApproved());
                textView.setTextColor(Color.parseColor("#B71C1C"));
            } else if ("Approved".equals(app.getApproved())) {
                textView.setText(app.getApproved());
                textView.setTextColor(Color.parseColor("#00C853"));
            }

            if ("Admin".equals(app.getOriginator())) {
                textView3.setTextColor(Color.parseColor("#FF9E80"));
            }

        } else {
            gridItemView = (View) convertView;
        }

        return gridItemView;
    }

    public void updateApprovalEntries(ArrayList<Approval> approvals) {
        this.addAll(approvals);
        notifyDataSetChanged();
    }
}

