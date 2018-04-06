package AsyncTask;

import android.os.AsyncTask;

import com.example.nikhi.popularmovies.BuildConfig;

import java.net.URL;
import java.util.List;

import api.NetworkUtils;
import api.TrailersJsonUtils;
import db.Trailer;
import db.TrailerResponse;

public class FetchTrailersTask extends AsyncTask<String,Void, List<Trailer>> {

    private String id;


    public FetchTrailersTask(String id) {
        this.id = id;

    }

    @Override
    protected List<Trailer> doInBackground(String... params) {
        URL trailersRequestUrl = NetworkUtils.buildTrailersOrReviewsUrl(BuildConfig.API_KEY, id, "videos");

        try {
            String jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(trailersRequestUrl);
            TrailerResponse trailerResponse = TrailersJsonUtils.parseJson(jsonTrailerResponse);
            return trailerResponse.getTrailers();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
