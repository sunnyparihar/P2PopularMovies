package udacity.moviestage2.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by sunnyparihar on 25/12/15.
 */

@Database( version = MovieFavouriteDatabase.VERSION )
public class MovieFavouriteDatabase {

    public static final int VERSION = 1;

    @Table(TableDataInterface.class)
    public static final String LISTS = "lists";
}
