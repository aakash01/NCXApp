package com.anutanetworks.ncxapp.activity.approval;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.adapter.ApprovalGridAdapter;
import com.anutanetworks.ncxapp.model.Approval;
import com.anutanetworks.ncxapp.services.AnutaRestClient;
import com.anutanetworks.ncxapp.services.EndlessScrollListener;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ApprovalFragment extends Fragment implements AbsListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AbsListView mListView;
    private ApprovalGridAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ApprovalFragment() {
    }

    // TODO: Rename and change types of parameters
    public static ApprovalFragment newInstance(String param1, String param2) {
        ApprovalFragment fragment = new ApprovalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAdapter = new ApprovalGridAdapter(getActivity(), new ArrayList<Approval>());
        getApprovalData(0, 10);

    }

    private void getApprovalData(int start, int limit) {
        RequestParams requestParams = new RequestParams();
        requestParams.add("start",String.valueOf(start));
        requestParams.add("limit",String.valueOf(limit));
        AnutaRestClient.get("/rest/workflowtasks", requestParams, new JsonHttpResponseHandler() {
           @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    ObjectMapper objectMapper1 = new ObjectMapper();
                    Object val = response.get("data");
                    final ArrayList<Approval> approvals = objectMapper1.readValue(val.toString(), new TypeReference<List<Approval>>() {
                    });
                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                mAdapter.updateApprovalEntries(approvals);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Unable to Load Approval Data", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approval, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        mListView.setOnScrollListener(new EndlessScrollListener(10) {

            @Override
            public void onLoadMore(int page, int limit) {
                int start = (page - 1) * limit;
                Log.d("aakash", "load more data with start " + start + " and limit " + limit);
                getApprovalData(start, limit);
            }
        });
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

        Approval approvalObj = (Approval) mAdapter.getItem(position);
        Intent i = new Intent(view.getContext(), ApprovalActivity.class);
        i.putExtra("approvalObject", (Serializable) approvalObj);
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

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        mAdapter.clear();
        getApprovalData(0,10);

    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    class ApprovalList {

        private List<Approval> approvals;

        public List<Approval> getApprovals() {
            return approvals;
        }

        public void setApprovals(List<Approval> approvals) {
            this.approvals = approvals;
        }
    }

}
