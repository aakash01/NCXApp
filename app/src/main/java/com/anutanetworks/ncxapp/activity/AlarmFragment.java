package com.anutanetworks.ncxapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

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

public class AlarmFragment extends Fragment implements AbsListView.OnItemClickListener, AbsListView.OnItemLongClickListener {

    private AbsListView mListView;

    private AlarmGridAdapter mAdapter;

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

        // TODO: Change Adapter to display your content

        mAdapter = new AlarmGridAdapter(getActivity(), new ArrayList<Alarm>());

        AnutaRestClient.get("/rest/alarms", null, new JsonHttpResponseHandler() {
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
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {


                Log.d("something", "someting");
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
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.list);
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
                ;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, final MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete:
                       SparseBooleanArray selected = mAdapter
                                .getSelectedIds();

                         JSONArray data = new JSONArray();
                        StringEntity entity = null;
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            try {
                                Alarm   selecteditem  = mAdapter.getItem(selected.keyAt(i));
                                data.put(selecteditem.getId());
                                entity = new StringEntity(data.toString());
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        AnutaRestClient.post(getActivity(), "/rest/alarms/action/acknowledge", entity, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                               mAdapter.updateItemsValue();
                                Toast.makeText(getActivity(), "successfully approved!", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                Toast.makeText(getActivity(), " Error occcured!", Toast.LENGTH_LONG).show();

                            }
                        });
                     /*  for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                WorldPopulation selecteditem = listviewadapter
                                        .getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                listviewadapter.remove(selecteditem);
                            }
                        }*/
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_alarm_list, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
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

            Alarm alarmObj = (Alarm)mAdapter.getItem(position);
            Intent i = new Intent(view.getContext(),AlarmActivity.class);
          i.putExtra("alarmObject",(Serializable) alarmObj);

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
