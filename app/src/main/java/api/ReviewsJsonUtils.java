package api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import db.Review;
import db.ReviewResponse;

public class ReviewsJsonUtils {

    private static final String LOG_TAG = ReviewsJsonUtils.class.getCanonicalName();

    private static final String statusError = "status_code";
    private static final String reviews = "results";
    public static final String author = "author";
    public static final String content = "content";
    public static final String url = "url";

    public static ReviewResponse parseJson(String json)
            throws JSONException {
        JSONObject responseJson = new JSONObject(json);
        if (responseJson.has(statusError)) {
            int errorCode = responseJson.getInt(statusError);
            Log.e(LOG_TAG, "parse trailers json error code: " + String.valueOf(errorCode));
        }
        JSONArray reviewArray = responseJson.getJSONArray(reviews);
        List<Review> reviewList = parseReviewsList(reviewArray);
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setReviews(reviewList);
        return reviewResponse;
    }


    private static List<Review> parseReviewsList(JSONArray reviewArray)
            throws JSONException {
        List<Review> reviewList = new ArrayList<>();
        for (int i = 0; i < reviewArray.length(); i++) {
            JSONObject review = reviewArray.getJSONObject(i);
            Review currentMovie = parseReview(review);
            reviewList.add(currentMovie);
        }
        return reviewList;

    }

    private static Review parseReview(JSONObject review)
            throws JSONException{
        Review currentReview = new Review();
        currentReview.setAuthor(review.getString(author));
        currentReview.setContent(review.getString(content));
        currentReview.setUrl(review.getString(url));
        return currentReview;

    }
}
