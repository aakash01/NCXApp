package com.anutanetworks.ncxapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.adapter.ApprovalGridAdapter;
import com.anutanetworks.ncxapp.model.Approval;
import com.anutanetworks.ncxapp.services.AnutaRestClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApprovalFragment extends Fragment implements AbsListView.OnItemClickListener {


    ProgressDialog progressDialog;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ApprovalGridAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static ApprovalFragment newInstance(String param1, String param2) {
        ApprovalFragment fragment = new ApprovalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ApprovalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       mAdapter=new ApprovalGridAdapter(getActivity(),new ArrayList<Approval>());
        AnutaRestClient.get("/rest/workflowtasks", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {

                    ObjectMapper objectMapper1 = new ObjectMapper();
                    Object val1 = response.toString();
                    final ArrayList<Approval> approvals = objectMapper1.readValue(val1.toString(), new TypeReference<List<Approval>>() {
                    });
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            mAdapter.updateApprovalEntries(approvals);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approval, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }




    @Override

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Approval item = (Approval)mAdapter.getItem(position);
        String aid=item.getId();
        Intent i = new Intent(view.getContext(),ApprovalActivity.class);
        i.putExtra("id",aid);

        startActivity(i);


    }



    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }
class ApprovalList{

    private List<Approval> approvals;
    public List<Approval> getApprovals(){
        return approvals;
    }
    public void setApprovals(List<Approval> approvals){
        this.approvals=approvals;
    }
}

}
