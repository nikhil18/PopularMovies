package com.example.nikhi.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import AsyncTask.FetchReviewsTask;
import AsyncTask.FetchTrailersTask;
import api.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import db.Movie;
import db.MoviesContract;
import db.Review;
import db.Trailer;

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

    @BindView(R.id.layout_reviews_list)
    LinearLayout reviewsLinearLayout;

    @BindView(R.id.text_review_title)
    TextView textReviewsTitle;

    @BindView(R.id.text_trailer_title)
    TextView textTrailerTitle;

    @BindView(R.id.layout_trailers_list)
    LinearLayout trailerLinearLayout;

    private Menu mOptionsMenu;

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
        new FetchTrailersTask(String.valueOf(movie.getId())) {
            @Override
            protected void onPostExecute(List<Trailer> trailers) {
                addTrailersToLayout(trailers);
            }
        }.execute();

        new FetchReviewsTask(String.valueOf(movie.getId())) {

            @Override
            protected void onPostExecute(List<Review> reviews) {
                addReviewsToLayout(reviews);
            }
        }.execute();
    }

    private void addReviewsToLayout(List<Review> reviews) {
        if (reviews != null && !reviews.isEmpty()) {
            for (Review review : reviews) {
                View view = getReviewView(review);
                reviewsLinearLayout.addView(view);
            }
        } else {
            hideReviewsSection();
        }
    }

    private void addTrailersToLayout(List<Trailer> trailers) {
        if (trailers != null && !trailers.isEmpty()) {
            for (Trailer trailer : trailers) {
                if (trailer.getSite().equals("YouTube")) {
                    View view = getTrailerView(trailer);
                    trailerLinearLayout.addView(view);
                }
            }
        } else {
            hideTrailersSection();
        }

    }

    private View getTrailerView(final Trailer trailer) {
        LayoutInflater inflater = LayoutInflater.from(DetailsActivity.this);
        View view = inflater.inflate(R.layout.trailer_list_item, trailerLinearLayout, false);
        TextView trailerNameTextView = ButterKnife.findById(view, R.id.text_trailer_item_name);
        trailerNameTextView.setText(trailer.getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(NetworkUtils.buildYouTubeUrl(trailer.getKey())));
                startActivity(intent);
            }
        });
        return view;
    }

    private View getReviewView(final Review review) {
        LayoutInflater inflater = LayoutInflater.from(DetailsActivity.this);
        View view = inflater.inflate(R.layout.review_list_item, reviewsLinearLayout, false);
        TextView contentTextView = ButterKnife.findById(view, R.id.text_content_review);
        TextView authorTextView = ButterKnife.findById(view, R.id.text_author_review);
        authorTextView.setText(getString(R.string.author_review, review.getAuthor()));
        contentTextView.setText(review.getContent());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = review.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        return view;
    }

    private void hideReviewsSection() {
        textReviewsTitle.setVisibility(View.GONE);
        reviewsLinearLayout.setVisibility(View.GONE);
    }

    private void hideTrailersSection() {
        textTrailerTitle.setVisibility(View.GONE);
        trailerLinearLayout.setVisibility(View.GONE);
    }

    private void deleteMovieFromDb(Movie movie) {
        String selection = MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(movie.getId())};
        getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI, selection, selectionArgs);
    }

    private void saveMovieInDb(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_DESCRIPTION, movie.getOverview());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, contentValues);
    }


    private boolean checkIfMovieIsInDb(Movie movie) {
        Cursor cursor = getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int movieId = cursor.getInt(
                        cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID));
                if (movieId == movie.getId()) {
                    return true;
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);

        Bundle bundle = getIntent().getExtras();
        Movie movie = bundle.getParcelable("movieDetails");
        mOptionsMenu = menu;
        if (checkIfMovieIsInDb(movie)) {
            mOptionsMenu.findItem(R.id.fav_movie).setIcon(R.drawable.ic_favorite_black_24dp);
        } else {
            mOptionsMenu.findItem(R.id.fav_movie).setIcon(R.drawable.ic_favorite_border_black_24dp);
        }
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.fav_movie) {

            Bundle bundle = getIntent().getExtras();
            Movie movie = bundle.getParcelable("movieDetails");
            if (!item.isChecked() && !checkIfMovieIsInDb(movie)) {
                item.setIcon(R.drawable.ic_favorite_black_24dp);
                saveMovieInDb(movie);
                return true;
            } else {
                item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                deleteMovieFromDb(movie);
                return false;
            }
        }

        return super.onOptionsItemSelected(item);

    }

}


