package udacity.moviestage2.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by sunnyparihar on 25/12/15.
 */
public interface TableDataInterface {

    @DataType(INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(TEXT)
    @NotNull
    String TITLE = "title";

    @DataType(TEXT)
    @NotNull
    String IMG_PATH = "img_path";

    @DataType(INTEGER)
    String MOVIE_ID = "movie_id";

    @DataType(TEXT)
    String DESC = "desc";

    @DataType(TEXT)
    String RELEASE_DATE = "release_date";

    @DataType(REAL)
    String POPULARITY = "popularity";

    @DataType(REAL)
    String RATING = "rating";

    @DataType(INTEGER)
    String VOTE_COUNT = "vote_count";
}
