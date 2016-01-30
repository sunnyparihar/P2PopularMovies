package udacity.moviestage2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import udacity.moviestage2.bean.MoviesData;

/**
 * Created by sunnyparihar on 26/12/15.
 */
public class FavouriteMovieList extends AppCompatActivity implements FavouriteMovieFragment.GridItemClick {

    private boolean mDualPane;
    private static final String TAG = "FAVOURITE_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_list);

        if (findViewById(R.id.favourite_detail_container) != null) {
            mDualPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.favourite_detail_container, new FavouriteMovieFragment(), TAG);
            }
        } else {
            mDualPane = false;

        }

        getSupportActionBar().setTitle(R.string.favourite_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onGridItemSelected(MoviesData moviesData) {
        if (mDualPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();

            args.putParcelable(MoviesDetailFragment.MOVIES_DATA, moviesData);

            MoviesDetailFragment fragment = new MoviesDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.favourite_detail_container, fragment, TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MoviesDetail.class);
            intent.putExtra(MoviesDetailFragment.MOVIES_DATA, moviesData);
            startActivity(intent);
        }
    }
}