package com.example.nikhi.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import api.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import db.Movie;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.details_image_poster)
    ImageView imagePoster;

    @BindView(R.id.details_title)
    TextView DetailsTitle;

    @BindView(R.id.details_release_date)
    TextView DetailsReleaseDate;

    @BindView(R.id.rating_average)
    RatingBar DetailsVoteAverage;

    @BindView(R.id.details_summary)
    TextView DetailsSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        Movie movie = bundle.getParcelable("movieDetails");
        setMovieDetails(movie);
    }

    private void setMovieDetails(Movie movie) {
        Picasso.with(imagePoster.getContext())
                .load(NetworkUtils.buildPosterUrl(movie.getPosterPath()))
                .into(imagePoster);
        DetailsTitle.setText(movie.getTitle());
        DetailsReleaseDate.setText(String.format(getResources().getString(R.string.release_date), movie.getReleaseDate()));
        DetailsVoteAverage.setRating(movie.getVoteAverage());
        DetailsSummary.setText(movie.getOverview());

    }
}
