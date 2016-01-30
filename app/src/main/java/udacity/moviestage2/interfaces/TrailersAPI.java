package udacity.moviestage2.interfaces;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import udacity.moviestage2.bean.TrailersList;

/**
 * Created by sunnyparihar on 12/12/15.
 */
public interface TrailersAPI {

    @GET("movie/{id}/videos")
    Call<TrailersList> listTrailersLink(@Path("id") String id, @Query("api_key") String key);
}
