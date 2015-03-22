package com.developers.obsidian.basicandroidnetworking.drawerfragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.developers.obsidian.basicandroidnetworking.R;
import com.developers.obsidian.basicandroidnetworking.utils.CheckNetwork;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class BasicImageFragment extends Fragment {

    ImageView imageView1, imageView2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.image_fragment, null);

        imageView1 = (ImageView) root.findViewById(R.id.image1);
        imageView2 = (ImageView) root.findViewById(R.id.image2);

        if (CheckNetwork.isInternetAvailable(getActivity())) {
            new Get().execute();
            Picasso.with(getActivity()).load("http://cdn.gottabemobile.com/wp-content/uploads/2014/06/Android-Evolution.jpg").into(imageView2);
        }
        return root;
    }

    private class Get extends AsyncTask<Void, Void, Void> {

        Bitmap bmp;
        URL url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Void doInBackground(Void... arg0) {

            try {
                url = new URL("http://cdn.gottabemobile.com/wp-content/uploads/2014/06/Android-Evolution.jpg");
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            } catch (MalformedURLException m) {

            } catch (IOException i) {

            }

            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            super.onPostExecute(result);
            imageView1.setImageBitmap(bmp);
        }
    }
}
