package com.example.nikhi.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import adapter.MoviesAdapter;
import AsyncTask.FetchMoviesTask;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycle)
    RecyclerView recyclerView;

    @BindView(R.id.no_internet_connection)
    TextView noInternet;

    private static final String FILTER_1 = "popular";

    private static final String FILTER_2 = "top_rated";

    private static MoviesAdapter adapter;

    private static final String LOG_TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MoviesAdapter();
        movieTaskPopular();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_popular) {
            movieTaskPopular();
        }

        if (item.getItemId() == R.id.action_best) {
            movieTaskBest();
        }

        return super.onOptionsItemSelected(item);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void movieTaskPopular() {
        FetchMoviesTask moviesTask = new FetchMoviesTask(adapter);
        if (isNetworkConnected()) {
            moviesTask.execute(FILTER_1);
            noInternet.setVisibility(TextView.INVISIBLE);
        } else {
            noInternet.setVisibility(TextView.VISIBLE);
        }
    }

    private void movieTaskBest() {
        FetchMoviesTask moviesTask = new FetchMoviesTask(adapter);
        if (isNetworkConnected()) {
            moviesTask.execute(FILTER_2);
            noInternet.setVisibility(TextView.INVISIBLE);
        } else {
            noInternet.setVisibility(TextView.VISIBLE);
        }
    }

}
