package api;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import db.Movie;
import db.MoviesResponse;

public class PopularMovieJsonUtils {

    private static final String LOG_TAG = PopularMovieJsonUtils.class.getCanonicalName();

    private static final String statusError = "status_code";
    private static final String movies = "results";
    private static final String posterKey = "poster_path";
    private static final String overviewKey = "overview";
    private static final String releaseDateKey = "release_date";
    private static final String titleKey = "title";
    private static final String idKey = "id";
    private static final String voteAverageKey = "vote_average";

    public static MoviesResponse parseJson(String json)
            throws JSONException {
        JSONObject responseJson = new JSONObject(json);
        if (responseJson.has(statusError)) {
            int errorCode = responseJson.getInt(statusError);
            Log.e(LOG_TAG, "parse movies json error code: " + String.valueOf(errorCode));
        }
        JSONArray moviesArray = responseJson.getJSONArray(movies);
        List<Movie> movieList = parseMovieList(moviesArray);
        MoviesResponse moviesResponse = new MoviesResponse();
        moviesResponse.setMovies(movieList);
        return moviesResponse;
    }

    @NonNull
    private static List<Movie> parseMovieList(JSONArray moviesArray) throws JSONException {
        List<Movie> movieList = new ArrayList<>();
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);
            Movie currentMovie = parseMovie(movie);
            movieList.add(currentMovie);
        }
        return movieList;
    }

    @NonNull
    private static Movie parseMovie(JSONObject movie) throws JSONException {
        Movie currentMovie = new Movie();
        currentMovie.setPosterPath(movie.getString(posterKey));
        currentMovie.setOverview(movie.getString(overviewKey));
        currentMovie.setReleaseDate(movie.getString(releaseDateKey));
        currentMovie.setTitle(movie.getString(titleKey));
        currentMovie.setId(movie.getInt(idKey));
        currentMovie.setVoteAverage(movie.getLong(voteAverageKey));
        return currentMovie;
    }
}
