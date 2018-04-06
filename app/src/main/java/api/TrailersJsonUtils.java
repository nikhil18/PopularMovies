package api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import db.Trailer;
import db.TrailerResponse;

public class TrailersJsonUtils {

    private static final String LOG_TAG = TrailersJsonUtils.class.getCanonicalName();

    private static final String statusError = "status_code";
    private static final String trailers = "results";
    public static final String key = "key";
    public static final String name = "name";
    public static final String site = "site";
    public static final String type = "type";

    public static TrailerResponse parseJson(String json)
            throws JSONException {
        JSONObject responseJson = new JSONObject(json);
        if (responseJson.has(statusError)) {
            int errorCode = responseJson.getInt(statusError);
            Log.e(LOG_TAG, "parse reviews json error code: " + String.valueOf(errorCode));
        }
        JSONArray trailerArray = responseJson.getJSONArray(trailers);
        List<Trailer> trailerList = parseTrailersList(trailerArray);
        TrailerResponse trailerResponse = new TrailerResponse();
        trailerResponse.setTrailers(trailerList);
        return trailerResponse;
    }


    private static List<Trailer> parseTrailersList(JSONArray trailerArray)
            throws JSONException {
        List<Trailer> trailerList = new ArrayList<>();
        for (int i = 0; i < trailerArray.length(); i++) {
            JSONObject trailer = trailerArray.getJSONObject(i);
            Trailer currentMovie = parseTrailer(trailer);
            trailerList.add(currentMovie);
        }
        return trailerList;

    }

    private static Trailer parseTrailer(JSONObject trailer)
    throws JSONException{
        Trailer currentTrailer = new Trailer();
        currentTrailer.setKey(trailer.getString(key));
        currentTrailer.setName(trailer.getString(name));
        currentTrailer.setSite(trailer.getString(site));
        return currentTrailer;

    }


}
