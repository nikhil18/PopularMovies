package db;

import java.util.List;

/**
 * Created by nikhi on 05-04-2018.
 */

public class ReviewResponse {
    private List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
