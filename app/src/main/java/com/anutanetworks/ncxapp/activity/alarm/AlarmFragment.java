package com.anutanetworks.ncxapp.activity.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.adapter.AlarmGridAdapter;
import com.anutanetworks.ncxapp.model.Alarm;
import com.anutanetworks.ncxapp.services.AnutaRestClient;
import com.anutanetworks.ncxapp.services.EndlessScrollListener;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AlarmFragment extends Fragment implements AbsListView.OnItemClickListener, AbsListView.OnItemLongClickListener, SwipeRefreshLayout.OnRefreshListener {

    private AbsListView mListView;

    private AlarmGridAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean onlyUnacknowledgedAlarm;
    private boolean onlyActiveAlarm;

    public AlarmFragment() {
    }

    // TODO: Rename and change types of parameters
    public static AlarmFragment newInstance(String param1, String param2) {
        AlarmFragment fragment = new AlarmFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new AlarmGridAdapter(getActivity(), new ArrayList<Alarm>());
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_alarm_filter, menu);
    }

    private synchronized void getAlarmData(int start, int limit) {
        RequestParams requestParams = new RequestParams();
        requestParams.add("start", String.valueOf(start));
        requestParams.add("limit",String.valueOf(limit));
        if(onlyUnacknowledgedAlarm) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("property", "acknowledged");
                jsonObject.put("value", false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            requestParams.add("filter", jsonArray.toString());
        }
        if(onlyActiveAlarm){
            requestParams.add("query", "ACTIVE");
        }
        AnutaRestClient.get("/rest/alarms", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Object val = response.get("data");
                    final ArrayList<Alarm> alarms = objectMapper.readValue(val.toString(), new TypeReference<List<Alarm>>() {
                    });
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            mAdapter.updateAlarmEntries(alarms);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Unable to Load Alarm Data", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = mListView.getCheckedItemCount();
                mAdapter.toggleSelection(position);


                mode.invalidate();
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, final MenuItem item) {
                boolean isAck = false;
                String posturl = null;
                switch (item.getItemId()) {
                    case R.id.Ack:
                        isAck = true;
                        posturl = "/rest/alarms/action/acknowledge";
                        break;
                    case R.id.unAck:
                        isAck = false;
                        posturl = "/rest/alarms/action/unacknowledge";
                        break;
                }
                if (null != posturl) {
                    SparseBooleanArray selected = mAdapter
                            .getSelectedIds();
                    JSONArray data = new JSONArray();
                    StringEntity entity = null;
                    for (int i = (selected.size() - 1); i >= 0; i--) {
                        try {
                            Alarm selecteditem = mAdapter.getItem(selected.keyAt(i));

                            data.put(selecteditem.getId());
                            entity = new StringEntity(data.toString());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    final boolean isAcknowledge = isAck;
                    AnutaRestClient.post(getActivity(), posturl, entity, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            mAdapter.updateItemsValue(isAcknowledge);
                            Toast.makeText(getActivity(), "Successfully " + (isAcknowledge ? "Acknowledged" : "Unacknowledged"), Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.d("internal-error", error.getLocalizedMessage());
                            Toast.makeText(getActivity(), " Error occcured!", Toast.LENGTH_LONG).show();

                        }
                    });
                }
                // Close CAB
                mode.finish();
                return true;

            }


            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_alarm_list, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                mAdapter.removeSelection();
                mListView.clearChoices();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                MenuItem ackMenu = menu.findItem(R.id.Ack);
                ackMenu.setVisible(true);
                MenuItem unAckMenu = menu.findItem(R.id.unAck);
                unAckMenu.setVisible(true);
                SparseBooleanArray selected = mAdapter
                        .getSelectedIds();
                boolean isAck = false;
                boolean isUnAck = false;
                for (int i = 0; i < selected.size(); i++) {
                    Alarm selecteditem = mAdapter.getItem(selected.keyAt(i));

                    if (selecteditem.isAcknowledged()) {
                        isAck = true;
                    } else {
                        isUnAck = true;
                    }
                }
                if (isAck) {
                    ackMenu.setVisible(false);
                }
                if (isUnAck) {
                    unAckMenu.setVisible(false);
                }

                return true;
            }
        });

        mListView.setOnScrollListener(new EndlessScrollListener(15) {

            @Override
            public void onLoadMore(int page, int limit) {
                swipeRefreshLayout.setRefreshing(true);
                int start = (page - 1) * limit;
                Log.d("aakash", "load more data with start " + start + " and limit " + limit);
                getAlarmData(start, limit);
            }
        });


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

        Alarm alarmObj = (Alarm) mAdapter.getItem(position);
        Intent i = new Intent(view.getContext(), AlarmActivity.class);
        i.putExtra("alarmObject", (Serializable) alarmObj);

        startActivity(i);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        return true;
    }


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
        getAlarmData(0,15);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.isChecked())
            item.setChecked(false);
        else
            item.setChecked(true);
        boolean reloadData = false;
        switch (item.getItemId()) {
            case R.id.onlyUnackFilter:
                onlyUnacknowledgedAlarm = item.isChecked();
                reloadData = true;
                break;
            case R.id.onlyActiveFilter:
                onlyActiveAlarm = item.isChecked();
                reloadData = true;
                break;
        }
        if(reloadData){
            onRefresh();
        }

        return true;
    }

    class AlarmList {
        private List<Alarm> alarms;

        public List<Alarm> getAlarms() {
            return alarms;
        }

        public void setAlarms(List<Alarm> alarms) {
            this.alarms = alarms;
        }
    }
}
