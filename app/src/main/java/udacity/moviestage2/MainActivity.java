package udacity.moviestage2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import udacity.moviestage2.bean.MoviesData;

public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {

    private boolean mDualPane;
    private static final String TAG = "DetailsTag";
    static final String FILTER_POPULAR = "popularity.desc";
    static final String MOVIES_DETAIL_FRAGMENT_TAG = "TAG";
    static final String FILTER_HIGEST_RATED = "vote_average.desc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movies_detail_container) != null) {
            mDualPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.movies_detail_container, new MoviesDetailFragment(), TAG);
            }
        } else {
            mDualPane = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_pouplar) {
            getSupportActionBar().setTitle(getString(R.string.most_popular_string));
            MoviesFragment moviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movies);
            if (null != moviesFragment) {
                moviesFragment.setFilter(FILTER_POPULAR);
            }

            return true;
        }
        if (id == R.id.action_highest_rated) {

            getSupportActionBar().setTitle(getString(R.string.highest_rating_string));
            MoviesFragment moviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movies);
            if (null != moviesFragment) {
                moviesFragment.setFilter(FILTER_HIGEST_RATED);
            }

            return true;
        }

        if (id == R.id.action_favourite) {

            getSupportActionBar().setTitle(getString(R.string.favourite_list));
            Intent intent = new Intent(this, FavouriteMovieList.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onGridItemSelected(ArrayList<MoviesData> moviesData, int pos) {

        MoviesData moviesDataBean = new MoviesData();
        moviesDataBean.setTitle(moviesData.get(pos).getTitle());
        moviesDataBean.setRating(moviesData.get(pos).getRating());
        moviesDataBean.setImgPath(moviesData.get(pos).getImgPath());
        moviesDataBean.setReleaseDate(moviesData.get(pos).getReleaseDate());
        moviesDataBean.setVoteCount(moviesData.get(pos).getVoteCount());
        moviesDataBean.setDesc(moviesData.get(pos).getDesc());
        moviesDataBean.setMovie_id(moviesData.get(pos).getMovie_id());

        if (mDualPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();

            args.putParcelable(MoviesDetailFragment.MOVIES_DATA, moviesDataBean);

            MoviesDetailFragment fragment = new MoviesDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movies_detail_container, fragment, MOVIES_DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MoviesDetail.class);
            intent.putExtra(MoviesDetailFragment.MOVIES_DATA, moviesDataBean);
            startActivity(intent);
        }
    }
}
