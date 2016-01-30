package udacity.moviestage2.interfaces;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import udacity.moviestage2.bean.ReviewsList;

/**
 * Created by sunnyparihar on 12/12/15.
 */
public interface ReviewAPI {

    @GET("movie/{id}/reviews")
    Call<ReviewsList> listReview(@Path("id") String id, @Query("api_key") String key);
}
