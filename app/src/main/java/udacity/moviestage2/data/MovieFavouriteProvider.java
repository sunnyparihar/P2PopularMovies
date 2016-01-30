package udacity.moviestage2.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by sunnyparihar on 25/12/15.
 */


@ContentProvider(authority = MovieFavouriteProvider.AUTHORITY,
        database = MovieFavouriteDatabase.class)
public class MovieFavouriteProvider {

    public static final String AUTHORITY = "udacity.moviestage2.data.MovieFavouriteProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    @TableEndpoint(table = MovieFavouriteDatabase.LISTS)
    public static class Lists {

        @ContentUri(
                path = MovieFavouriteDatabase.LISTS,
                type = "vnd.android.cursor.dir/list",
                defaultSort = TableDataInterface._ID + " ASC")
        public static final Uri LISTS = Uri.parse("content://" + AUTHORITY + "/lists");


        @InexactContentUri(
                path = MovieFavouriteDatabase.LISTS + "/#",
                name = "MOVIE_ID",
                type = "vnd.android.cursor.item/list",
                whereColumn = TableDataInterface.MOVIE_ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(MovieFavouriteDatabase.LISTS, String.valueOf(id));
        }

    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }
}
