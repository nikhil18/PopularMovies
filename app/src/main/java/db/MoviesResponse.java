package db;

import java.util.List;


public class MoviesResponse {

        List<Movie> movies;

        public List<Movie> getMovies() {
            return movies;
        }

        public void setMovies(List<Movie> movies) {
            this.movies = movies;
        }
}
