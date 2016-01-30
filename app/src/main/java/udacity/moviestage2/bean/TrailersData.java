package udacity.moviestage2.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunnyparihar on 13/12/15.
 */
public class TrailersData implements Parcelable {

    @SerializedName("key")
    public String key;

    @SerializedName("id")
    public String id;

    public TrailersData() {

    }

    private TrailersData(Parcel in) {

        readFromParcel(in);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(id);
    }

    private void readFromParcel(Parcel in) {
        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        key = in.readString();
        id = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public TrailersData createFromParcel(Parcel in) {
                    return new TrailersData(in);
                }

                public TrailersData[] newArray(int size) {
                    return new TrailersData[size];
                }
            };
}