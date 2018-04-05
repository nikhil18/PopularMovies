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

import adapter.FavoritesAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import adapter.MoviesAdapter;
import AsyncTask.FetchMoviesTask;
import db.FavoritesCursorLoader;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycle)
    RecyclerView recyclerView;

    @BindView(R.id.no_internet_connection)
    TextView noInternet;


    private MoviesAdapter moviesAdapter;

    private int selectedOption = R.id.action_popular;

    private static final String FILTER_1 = "popular";

    private static final String FILTER_2 = "top_rated";

    public static final int ID_FAVORITES_LOADER = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        moviesAdapter = new MoviesAdapter();
        if (savedInstanceState == null) {
            movieTaskPopular();
        } else {
            loadAdapterPerOptionSelected(
                    savedInstanceState.getInt("optionSelected", R.id.action_popular));
        }
        recyclerView.setAdapter(moviesAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("optionSelected", selectedOption);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        loadAdapterPerOptionSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void loadAdapterPerOptionSelected(int selectedOption) {
        this.selectedOption = selectedOption;

        if (selectedOption == R.id.action_popular) {
            movieTaskPopular();
        }

        if (selectedOption == R.id.action_best) {
            movieTaskBest();
        }
        if (selectedOption == R.id.action_favorites) {
            setMovieAdapterFavorites();
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setMovieAdapterFavorites() {
        FavoritesAdapter favoritesAdapter = new FavoritesAdapter();
        recyclerView.setAdapter(favoritesAdapter);
        getSupportLoaderManager().initLoader(
                ID_FAVORITES_LOADER, null, new FavoritesCursorLoader(this, favoritesAdapter));
    }

    private void movieTaskPopular() {
        moviesAdapter = new MoviesAdapter();
        FetchMoviesTask moviesTask = new FetchMoviesTask(moviesAdapter);
        recyclerView.setAdapter(moviesAdapter);
        if (isNetworkConnected()) {
            moviesTask.execute(FILTER_1);
            noInternet.setVisibility(TextView.INVISIBLE);
        } else {
            noInternet.setVisibility(TextView.VISIBLE);
        }
    }

    private void movieTaskBest() {
        moviesAdapter = new MoviesAdapter();
        FetchMoviesTask moviesTask = new FetchMoviesTask(moviesAdapter);
        recyclerView.setAdapter(moviesAdapter);
        if (isNetworkConnected()) {
            moviesTask.execute(FILTER_2);
            noInternet.setVisibility(TextView.INVISIBLE);
        } else {
            noInternet.setVisibility(TextView.VISIBLE);
        }
    }

}
