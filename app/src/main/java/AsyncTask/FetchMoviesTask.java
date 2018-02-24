package AsyncTask;

import android.os.AsyncTask;

import com.example.nikhi.popularmovies.BuildConfig;

import java.net.URL;
import java.util.List;

import adapter.MoviesAdapter;
import api.NetworkUtils;
import api.PopularMovieJsonUtils;
import db.Movie;
import db.MoviesResponse;

public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

    private MoviesAdapter adapter;

    public FetchMoviesTask(MoviesAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        URL moviesRequestUrl = NetworkUtils.buildUrl(BuildConfig.API_KEY, params[0]);

        try {
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
            MoviesResponse moviesResponse = PopularMovieJsonUtils.parseJson(jsonMovieResponse);
            return moviesResponse.getMovies();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(List<Movie> movies) {

        if (movies != null) {
            adapter.setMoviesData(movies);

        }

    }
}