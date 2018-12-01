package ua.od.radio.pozitivefm.data.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

public class URLImageParser implements Html.ImageGetter {
    private Context c;
    private View container;

    public URLImageParser(View t, Context c) {
        this.c = c;
        this.container = t;
    }

    public Drawable getDrawable(String source) {
        Log.i("URLImageParser", "getDrawable, source " + source);
        URLDrawable urlDrawable = new URLDrawable();

        // get the actual source
        ImageGetterAsyncTask asyncTask =
                new ImageGetterAsyncTask(urlDrawable);

        asyncTask.execute(source);

        // return reference to URLDrawable where I will change with actual image from
        // the src tag
        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLDrawable urlDrawable;
        int sizeUp = 5;
        int margin = 12;

        ImageGetterAsyncTask(URLDrawable d) {
            this.urlDrawable = d;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result == null)
                return;
            // set the correct bound according to the result from HTTP call
            urlDrawable.setBounds(margin, margin, result.getIntrinsicWidth() * sizeUp,
                    result.getIntrinsicHeight() * sizeUp);
            Log.i("URLImageParser", "getDrawable, getIntrinsicWidth " + result.getIntrinsicWidth());
            Log.i("URLImageParser", "getDrawable, getIntrinsicHeight " + result.getIntrinsicHeight());

            // change the reference of the current drawable to the result
            // from the HTTP call
            urlDrawable.drawable = result;
            if (result.getIntrinsicWidth() < 50) {
                // redraw the image by invalidating the container
                URLImageParser.this.container.invalidate();
            }
        }

        Drawable fetchDrawable(String urlString) {
            Log.i("URLImageParser", "fetchDrawable, urlString " + urlString);
            try {
                InputStream is = fetch(urlString);
                Drawable drawable = Drawable.createFromStream(is, "src");
                if (drawable.getIntrinsicWidth() > 50)
                    return null;
                drawable.setBounds(margin, margin, drawable.getIntrinsicWidth() * sizeUp,
                        drawable.getIntrinsicHeight() * sizeUp);
                return drawable;
            } catch (Exception e) {
                return null;
            }
        }

        private InputStream fetch(String urlString) throws IOException {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlString);
            HttpResponse response = httpClient.execute(request);
            return response.getEntity().getContent();
        }
    }
}