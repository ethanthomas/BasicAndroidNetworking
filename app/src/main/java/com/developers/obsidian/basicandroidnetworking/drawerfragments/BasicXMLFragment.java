package com.developers.obsidian.basicandroidnetworking.drawerfragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.developers.obsidian.basicandroidnetworking.R;
import com.developers.obsidian.basicandroidnetworking.utils.CheckNetwork;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class BasicXMLFragment extends Fragment {

    String URL = "http://ethanthomas.me/xml.xml";
    TextView title, subtext;
    NodeList nodelist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {

        View v = inflater.inflate(R.layout.xml, container, false);

        if (CheckNetwork.isInternetAvailable(getActivity())) {

            new DownloadXML().execute(URL);

        }

        return v;
    }


    private class DownloadXML extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... Url) {

            try {

                java.net.URL url = new URL(Url[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName("header");


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args) {

            if (nodelist != null && nodelist.getLength() != 0)
            for (int temp = 0; temp < nodelist.getLength(); temp++) {
                Node nNode = nodelist.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;

                    title = (TextView) getView().findViewById(R.id.xmlTitle);
                    title.setText(getNode("title", eElement).toString());

                    subtext = (TextView) getView().findViewById(R.id.xmlSubText);
                    subtext.setText(getNode("subtext", eElement).toString());


                }
            }
        }
    }

    private static String getNode(String sTag, org.w3c.dom.Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                .getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
    }

}
