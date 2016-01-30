package udacity.moviestage2.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunnyparihar on 12/12/15.
 */
public class TrailersList {

    @SerializedName("results")
    List<TrailersData> trailersData;

    public List<TrailersData> getTrailersData() {
        return trailersData;
    }

    public void setTrailersData(List<TrailersData> trailersData) {
        this.trailersData = trailersData;
    }
}

