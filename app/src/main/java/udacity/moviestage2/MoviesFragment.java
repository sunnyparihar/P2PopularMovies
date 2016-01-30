package udacity.moviestage2;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import udacity.moviestage2.background.MoviesInterface;
import udacity.moviestage2.background.MoviesPosterAsyncTask;
import udacity.moviestage2.bean.MoviesData;

/**
 * Created by sunnyparihar on 19/08/15.
 */
public class MoviesFragment extends Fragment {

    static final String API_KEY = "XXXXXXXXXX";

    private ArrayList<MoviesData> getMoviesData;
    MoviesInterface moviesInterface;

    private int mPosition = GridView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onGridItemSelected(ArrayList<MoviesData> moviesData, int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movies_fragment, container, false);
        final GridView moviesGrid = (GridView) rootView.findViewById(R.id.movies_grid);
        moviesGrid.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        moviesGrid.setDrawSelectorOnTop(true);

        moviesInterface = new MoviesInterface() {
            @Override
            public void passMoviesData(ArrayList<MoviesData> moviesData) {
                getMoviesData = moviesData;
                for (int i = 0; i < moviesData.size(); i++) {
                    moviesGrid.setAdapter(new ImageAdapter(getActivity(), moviesData));
                }
            }
        };

        moviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((Callback) getActivity()).onGridItemSelected(getMoviesData, i);
                mPosition = i;

            }
        });

        if (savedInstanceState == null)
            setFilter("");
        else {

            getMoviesData = savedInstanceState.getParcelableArrayList("moviesData");
            moviesGrid.setAdapter(new ImageAdapter(getActivity(), getMoviesData));
            if (savedInstanceState.containsKey(SELECTED_KEY)) {
                mPosition = savedInstanceState.getInt(SELECTED_KEY);
                moviesGrid.smoothScrollToPosition(mPosition);
            }
        }

        return rootView;
    }


    public boolean isNetworkAvailable(final Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList("moviesData", getMoviesData);

        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);

    }


    public void setFilter(String filter) {

        if (isNetworkAvailable(getActivity()))
            new MoviesPosterAsyncTask(moviesInterface, getActivity()).execute(filter, API_KEY);
        else {
            Toast.makeText(getActivity(), getString(R.string.no_network_connection), Toast.LENGTH_LONG).show();
        }
    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<MoviesData> moviesData;
        private String IMG_BASE_URL = "http://image.tmdb.org/t/p/w185/";

        public ImageAdapter(Context c, ArrayList<MoviesData> moviesDatas) {
            mContext = c;
            this.moviesData = moviesDatas;
        }

        public int getCount() {
            return moviesData.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (moviesData.get(position).getImgPath() != null) {
                if (convertView == null) {
                    // if it's not recycled, initialize some attributes
                    imageView = new ImageView(mContext);
                    imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


                    imageView.setAdjustViewBounds(true);
                } else {
                    imageView = (ImageView) convertView;
                }

                if (moviesData.get(position).getImgPath() != null)
                    Picasso.with(mContext).load(IMG_BASE_URL + moviesData.get(position).getImgPath()).noFade().into(imageView);

                return imageView;
            } else
                return null;
        }

    }
}
