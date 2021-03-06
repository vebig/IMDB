package com.vaibhav.imdb.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.vaibhav.imdb.model.Cast;
import com.vaibhav.imdb.model.Constants;
import com.vaibhav.imdb.net.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetCastCrew extends AsyncTask<String, Void, List<Cast>> {

    private Context context;
    private String tag;

    public GetCastCrew(Context context, String tag) {

        this.context = context;
        this.tag = tag;
    }

    @Override
    protected List<Cast> doInBackground(String... urls) {
        ServiceHandler sh = new ServiceHandler();
        String jsonStr = sh.makeServiceCall(urls[0], ServiceHandler.GET);
        List<Cast> castList = new ArrayList<>();

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray castArray = jsonObj.getJSONArray(tag);

                if(tag.equals(Constants.TAG_CAST)) {

                    for (int i = 0; i < castArray.length(); i++) {
                        JSONObject o = castArray.getJSONObject(i);
                        Cast cast = new Cast();
                        cast.setCharacter(o.getString(Constants.CHARACTER));
                        cast.setName(o.getString(Constants.NAME));
                        cast.setProfilePath(o.getString(Constants.PROFILE_PATH));
                        castList.add(cast);
                    }
                } else if (tag.equals(Constants.TAG_CREW)) {
                    for (int i = 0; i < castArray.length(); i++) {
                        JSONObject o = castArray.getJSONObject(i);
                        Cast cast = new Cast();
                        cast.setJob(o.getString(Constants.JOB));
                        cast.setName(o.getString(Constants.NAME));
                        cast.setProfilePath(o.getString(Constants.PROFILE_PATH));
                        castList.add(cast);
                    }
                } else if (tag.equals(Constants.TAG_RESULTS)) {
                    for (int i = 0; i < castArray.length(); i++) {
                        JSONObject o = castArray.getJSONObject(i);
                        Cast cast = new Cast();
                        cast.setName(o.getString(Constants.NAME));
                        cast.setKey(o.getString(Constants.KEY));
                        castList.add(cast);
                    }
                } else if (tag.equals(Constants.TAG_POSTERS)) {
                    for (int i = 0; i < castArray.length(); i++) {
                        JSONObject o = castArray.getJSONObject(i);
                        Cast cast = new Cast();
                        cast.setProfilePath(o.getString(Constants.TAG_FILE_PATH));
                        castList.add(cast);
                    }
                }
                return castList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Cast> result) {
        super.onPostExecute(result);

        if (result == null) {

            Toast.makeText(context, "Unable to fetch data from server", Toast.LENGTH_LONG).show();
        }
    }
}
