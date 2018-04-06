package AsyncTask;

import android.os.AsyncTask;

import com.example.nikhi.popularmovies.BuildConfig;

import java.net.URL;
import java.util.List;

import api.NetworkUtils;
import api.ReviewsJsonUtils;
import db.Review;
import db.ReviewResponse;


public class FetchReviewsTask extends AsyncTask<String, Void, List<Review>> {

    private String id;


    public FetchReviewsTask(String id) {
        this.id = id;

    }

    @Override
    protected List<Review> doInBackground(String... params) {
        URL reviewsRequestUrl = NetworkUtils.buildTrailersOrReviewsUrl(BuildConfig.API_KEY, id, "reviews");

        try {
            String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewsRequestUrl);
            ReviewResponse reviewResponse = ReviewsJsonUtils.parseJson(jsonReviewResponse);
            return reviewResponse.getReviews();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}
