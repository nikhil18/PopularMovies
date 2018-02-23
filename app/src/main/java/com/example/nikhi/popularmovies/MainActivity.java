package com.example.nikhi.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import api.NetworkUtils;
import api.PopularMovieJsonUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import db.Movie;
import db.MoviesResponse;
import adapter.MoviesAdapter;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycle)
    RecyclerView recyclerView;

    @BindView(R.id.no_internet_connection)
    TextView noInternet;

    private static final String FILTER_1 = "popular";

    private static final String FILTER_2 = "top_rated";

    private static MoviesAdapter adapter;

    private static final String LOG_TAG = MainActivity.class.getCanonicalName();

    //enter your api key that you got from themoviedb.org here//
    private static final String apiKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        if (isNetworkConnected()) {
            moviesTask.execute(FILTER_1);
            noInternet.setVisibility(TextView.INVISIBLE);
        } else {
            noInternet.setVisibility(TextView.VISIBLE);
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MoviesAdapter();
        recyclerView.setAdapter(adapter);
    }

    public static class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {
            URL moviesRequestUrl = NetworkUtils.buildUrl(apiKey, params[0]);

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
                Log.v(LOG_TAG, "moviesData: " + movies);
                adapter.setMoviesData(movies);

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_popular) {
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            if (isNetworkConnected()) {
                moviesTask.execute(FILTER_1);
                noInternet.setVisibility(TextView.INVISIBLE);
            } else {
                noInternet.setVisibility(TextView.VISIBLE);
            }

        }

        if (item.getItemId() == R.id.action_best) {
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            if (isNetworkConnected()) {
                moviesTask.execute(FILTER_2);
                noInternet.setVisibility(TextView.INVISIBLE);
            } else {
                noInternet.setVisibility(TextView.VISIBLE);
            }
        }

        return super.onOptionsItemSelected(item);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
