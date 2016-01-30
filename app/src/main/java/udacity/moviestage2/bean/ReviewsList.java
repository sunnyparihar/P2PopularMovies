package udacity.moviestage2.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunnyparihar on 12/12/15.
 */
public class ReviewsList {

    @SerializedName("results")
    List<ReviewData> reviewData;

    public List<ReviewData> getReviewData() {
        return reviewData;
    }

    public void setReviewData(List<ReviewData> reviewData) {
        this.reviewData = reviewData;
    }
}

