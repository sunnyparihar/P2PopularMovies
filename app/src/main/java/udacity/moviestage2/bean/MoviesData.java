package udacity.moviestage2.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sunnyparihar on 21/08/15.
 */
public class MoviesData implements Parcelable {

    private String title;
    private String desc;
    private String releaseDate;
    private String imgPath;
    private double popularity, rating;
    private int voteCount, movie_id;


    public MoviesData() {

    }

    private MoviesData(Parcel in) {
        readFromParcel(in);
    }

    private int mData;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(desc);
        parcel.writeString(releaseDate);
        parcel.writeString(imgPath);
        parcel.writeDouble(popularity);
        parcel.writeDouble(rating);
        parcel.writeInt(voteCount);
        parcel.writeInt(movie_id);
    }

    private void readFromParcel(Parcel in) {

        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        title = in.readString();
        desc = in.readString();
        releaseDate = in.readString();
        imgPath = in.readString();
        popularity = in.readDouble();
        rating = in.readDouble();
        voteCount = in.readInt();
        movie_id = in.readInt();
    }


    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public MoviesData createFromParcel(Parcel in) {
                    return new MoviesData(in);
                }

                public MoviesData[] newArray(int size) {
                    return new MoviesData[size];
                }
            };
}
