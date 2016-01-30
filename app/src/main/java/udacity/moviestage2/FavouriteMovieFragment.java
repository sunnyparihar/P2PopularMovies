package udacity.moviestage2;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import udacity.moviestage2.bean.MoviesData;
import udacity.moviestage2.data.MovieFavouriteProvider;
import udacity.moviestage2.data.TableDataInterface;

/**
 * Created by sunnyparihar on 26/12/15.
 */
public class FavouriteMovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private GridView moviesGrid;
    private ImageAdapter imageAdapter;
    private Cursor saveData;

    private static final int LOADER_NOTES = 20;

    public interface GridItemClick {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onGridItemSelected(MoviesData moviesData);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),
                MovieFavouriteProvider.Lists.LISTS,
                null,
                null,
                null,
                null
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        this.saveData = data;
        if (imageAdapter == null) {
            imageAdapter = new ImageAdapter(getActivity(), data);
            moviesGrid.setAdapter(imageAdapter);

        } else {
            imageAdapter.changeCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (imageAdapter != null) {
            imageAdapter.changeCursor(null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.movies_fragment, container, false);
        moviesGrid = (GridView) rootView.findViewById(R.id.movies_grid);
        moviesGrid.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        moviesGrid.setDrawSelectorOnTop(true);
        getLoaderManager().initLoader(LOADER_NOTES, null, this);


        moviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int movie_id = saveData.getInt(saveData.getColumnIndex(TableDataInterface.MOVIE_ID));

                MoviesData moviesData = new MoviesData();
                Cursor c = rootView.getContext().getContentResolver().query(MovieFavouriteProvider.Lists.withId(movie_id), null, null, null, null);

                if (c != null && c.moveToFirst()) {
                    moviesData.setMovie_id(c.getInt(c.getColumnIndex(TableDataInterface.MOVIE_ID)));
                    moviesData.setDesc(c.getString(c.getColumnIndex(TableDataInterface.DESC)));
                    moviesData.setImgPath(c.getString(c.getColumnIndex(TableDataInterface.IMG_PATH)));
                    moviesData.setPopularity(c.getDouble(c.getColumnIndex(TableDataInterface.POPULARITY)));
                    moviesData.setRating(c.getDouble(c.getColumnIndex(TableDataInterface.RATING)));
                    moviesData.setVoteCount(c.getInt(c.getColumnIndex(TableDataInterface.VOTE_COUNT)));
                    moviesData.setReleaseDate(c.getString(c.getColumnIndex(TableDataInterface.RELEASE_DATE)));

                    ((GridItemClick) getActivity())
                            .onGridItemSelected(moviesData);
                }
            }
        });
        return rootView;
    }


    public class ImageAdapter extends CursorAdapter {
        private Context mContext;
        private String IMG_BASE_URL = "http://image.tmdb.org/t/p/w185/";
        private ImageView imageView;

        public ImageAdapter(Context context, Cursor c) {
            super(context, c, 0);
            this.mContext = context;
        }


        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setPadding(1, 1, 1, 1);
            imageView.setAdjustViewBounds(true);

            return imageView;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String img_path = cursor.getString(cursor.getColumnIndex(TableDataInterface.IMG_PATH));

            if (img_path != null)
                Picasso.with(mContext).load(IMG_BASE_URL + img_path).noFade().into(imageView);

        }

    }
}


