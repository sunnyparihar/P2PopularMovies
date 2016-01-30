package udacity.moviestage2.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunnyparihar on 13/12/15.
 */
public class ReviewData implements Parcelable {

    @SerializedName("author")
    public String author;

    @SerializedName("content")
    public String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReviewData() {

    }

    private ReviewData(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
    }

    private void readFromParcel(Parcel in) {

        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        author = in.readString();
        content = in.readString();
    }


    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public ReviewData createFromParcel(Parcel in) {
                    return new ReviewData(in);
                }

                public ReviewData[] newArray(int size) {
                    return new ReviewData[size];
                }
            };
}