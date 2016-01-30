package udacity.moviestage2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import udacity.moviestage2.bean.MoviesData;
import udacity.moviestage2.data.MovieFavouriteProvider;
import udacity.moviestage2.data.TableDataInterface;


public class MoviesDetail extends AppCompatActivity {

    private MoviesData moviesData;
    private String IMG_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private ImageView imgLargeView;
    private FloatingActionButton favourite, share;
    private boolean isFavourite;
    private int sel_movie_id;
    private MoviesDetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);

        moviesData = getIntent().getParcelableExtra(MoviesDetailFragment.MOVIES_DATA);
        favourite = (FloatingActionButton) findViewById(R.id.favourite_movie_fab);
        share = (FloatingActionButton) findViewById(R.id.share_movie_fab);

        Bundle arguments = new Bundle();
        arguments.putParcelable(MoviesDetailFragment.MOVIES_DATA, moviesData);

        imgLargeView = (ImageView) findViewById(R.id.movies_img);

        fragment = new MoviesDetailFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.movie_detail_container, fragment)
                .commit();

        sel_movie_id = moviesData.getMovie_id();

        Picasso.with(this).load(IMG_BASE_URL + moviesData.getImgPath()).noFade().into(imgLargeView);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.details_collasping_toopbar);
        collapsingToolbar.setTitle(moviesData.getTitle());
        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toopbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isFavourite) {
                    isFavourite = true;
                    favourite.setImageResource(R.drawable.ic_favorite_white_48dp);
                    ContentValues cv = new ContentValues();
                    cv.put("title", moviesData.getTitle());
                    cv.put("movie_id", moviesData.getMovie_id());
                    cv.put("img_path", moviesData.getImgPath());
                    cv.put("desc", moviesData.getDesc());
                    cv.put("rating", moviesData.getRating());
                    cv.put("release_date", moviesData.getReleaseDate());
                    cv.put("vote_count", moviesData.getVoteCount());
                    cv.put("popularity", moviesData.getPopularity());

                    getApplicationContext().getContentResolver().insert(MovieFavouriteProvider.Lists.LISTS, cv);

                    Snackbar.make(view, R.string.add_favourite_list, Snackbar.LENGTH_LONG).show();
                } else {
                    isFavourite = false;
                    favourite.setImageResource(R.drawable.ic_favorite_border_white_48dp);
                    getApplicationContext().getContentResolver().delete(MovieFavouriteProvider.Lists.withId(sel_movie_id), null, null);
                    Snackbar.make(view, R.string.remove_favourite_list, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fragment.trailersDatas != null && fragment.trailersDatas.size() > 0) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "Hey check out this cool movie- " + moviesData.getTitle() + ".  Watch the trailer at you tube- http://www.youtube.com/watch?v=" + fragment.trailersDatas.get(0).getKey());
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                } else {
                    Snackbar.make(view, R.string.no_trailer, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        Cursor cursor = getContentResolver().query(MovieFavouriteProvider.Lists.LISTS, null, null, null, null);

        if (cursor == null) {
            // Some providers return null if an error occurs whereas thers throw an exception
        } else if (cursor.getCount() < 1) {
            // No matches found
        } else {

            while (cursor.moveToNext()) {
                // dint id = cursor.getInt(idIndex);
                int movie_id = cursor.getInt(cursor.getColumnIndex(TableDataInterface.MOVIE_ID));

                if (movie_id == sel_movie_id) {
                    isFavourite = true;
                    favourite.setImageResource(R.drawable.ic_favorite_white_48dp);
                }
            }

        }

    }

}
