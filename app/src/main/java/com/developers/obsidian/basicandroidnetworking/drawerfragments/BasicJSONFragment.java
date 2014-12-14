package com.developers.obsidian.basicandroidnetworking.drawerfragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.developers.obsidian.basicandroidnetworking.R;
import com.developers.obsidian.basicandroidnetworking.utils.CheckNetwork;
import com.developers.obsidian.basicandroidnetworking.utils.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class BasicJSONFragment extends Fragment {

    String URL = "http://ethanthomas.me/json.json";

    String TAG_TITLE = "title";
    String TAG_DESCRIPTION = "description";
    String TAG_AUTHOR = "author";

    String headerTitleText, headerTypeText;

    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<HashMap<String, String>> List;

    TextView headerTitle, headerType;
    ListAdapter adapter;

    View header;
    ListView lv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle b) {

        View v = inflater.inflate(R.layout.listview_main, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe);

        header = getActivity().getLayoutInflater().inflate(R.layout.header, null);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (CheckNetwork.isInternetAvailable(getActivity())) {

                    new Get().execute();
                    lv.removeHeaderView(header);
                    lv.addHeaderView(header);

                } else {

                    lv.setAdapter(null);
                    lv.removeHeaderView(header);
                    swipeRefreshLayout.setRefreshing(false);

                }
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        lv = (ListView) v.findViewById(R.id.listview);

        if (CheckNetwork.isInternetAvailable(getActivity())) {

            lv.removeHeaderView(header);
            lv.addHeaderView(header);
            headerTitle = (TextView) v.findViewById(R.id.headerTitle);
            headerType = (TextView) v.findViewById(R.id.headerType);

            new Get().execute();

        } else {

            lv.setAdapter(null);
            lv.removeHeaderView(header);
            swipeRefreshLayout.setRefreshing(false);

        }
        return v;
    }

    private class Get extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
            List = new ArrayList<HashMap<String, String>>();

        }

        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(URL, ServiceHandler.GET);

            if (jsonStr != null) {
                Log.d("String = ", jsonStr);

                try {
                    JSONObject json = new JSONObject(jsonStr);
                    headerTitleText = json.getString("header");
                    headerTypeText = json.getString("type");

                    JSONArray array = new JSONArray(json.getJSONArray("articles").toString());

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        HashMap<String, String> articleMap = new HashMap<String, String>();

                        articleMap.put(TAG_TITLE, object.getString(TAG_TITLE));
                        articleMap.put(TAG_DESCRIPTION, object.getString(TAG_DESCRIPTION));
                        articleMap.put(TAG_AUTHOR, object.getString(TAG_AUTHOR));

                        List.add(articleMap);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            super.onPostExecute(result);

            lv = (ListView) getView().findViewById(R.id.listview);
            headerTitle = (TextView) getView().findViewById(R.id.headerTitle);
            headerType = (TextView) getView().findViewById(R.id.headerType);

            adapter = new SimpleAdapter(getActivity(), List,
                    R.layout.listview_item, new String[]{TAG_TITLE,
                    TAG_DESCRIPTION, TAG_AUTHOR}, new int[]{R.id.title, R.id.description, R.id.author}
            );

            headerTitle.setText(headerTitleText);
            headerType.setText(headerTypeText);

            lv.setAdapter(adapter);

            swipeRefreshLayout.setRefreshing(false);

        }

    }

}
