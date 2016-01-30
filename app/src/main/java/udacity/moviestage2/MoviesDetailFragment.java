package udacity.moviestage2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import udacity.moviestage2.bean.MoviesData;
import udacity.moviestage2.bean.ReviewData;
import udacity.moviestage2.bean.ReviewsList;
import udacity.moviestage2.bean.TrailersData;
import udacity.moviestage2.bean.TrailersList;
import udacity.moviestage2.interfaces.ReviewAPI;
import udacity.moviestage2.interfaces.TrailersAPI;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesDetailFragment extends Fragment {

    View rootView;

    @Bind(R.id.total_user)
    TextView total_user;
    @Bind(R.id.trailers)
    TextView trailers_text;
    @Bind(R.id.rating)
    TextView rating;
    @Bind(R.id.description)
    TextView description;
    @Bind(R.id.release_date)
    TextView release_date;
    @Bind(R.id.thumb)
    ImageView imgThumbnail;
    @Bind(R.id.trailer_grid)
    GridView trailersGrid;
    @Bind(R.id.review_list)
    RecyclerView reviewList;

    private String IMG_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private String YOU_TUBE_IMG_BASE_URL = "http://img.youtube.com/vi/";
    public static final String MOVIES_DATA = "MOVIES_DATA";
    private MoviesData moviesData;
    private String BASE_URL = "http://api.themoviedb.org/3/";
    private TrailersAdapter trailersAdapter;
    private ReviewAdapter reviewAdapter;
    private Context mContext;
    ArrayList<TrailersData> trailersDatas;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ReviewData> reviewDatas;
    private Retrofit retrofit;

    public MoviesDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_movies_detail, container, false);

        mContext = rootView.getContext();

        ButterKnife.bind(this, rootView);

        Bundle arguments = getArguments();

        if (arguments != null) {
            moviesData = arguments.getParcelable(MoviesDetailFragment.MOVIES_DATA);
        }

        if (moviesData.getReleaseDate() != null)
            release_date.setText(getString(R.string.release_date_string) + moviesData.getReleaseDate());

        rating.setText(getString(R.string.user_rating_string) + String.valueOf(moviesData.getRating()));
        if (moviesData.getDesc() != null)
            description.setText(String.valueOf(moviesData.getDesc()));

        total_user.setText(getString(R.string.total_user_string) + String.valueOf(moviesData.getVoteCount()));

        Picasso.with(getActivity()).load(IMG_BASE_URL + moviesData.getImgPath()).noFade().into(imgThumbnail);

        mLayoutManager = new LinearLayoutManager(mContext);
        reviewList.setLayoutManager(mLayoutManager);
        trailers_text.setVisibility(View.GONE);

        trailersGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (trailersDatas != null) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailersDatas.get(i).getKey()));
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + trailersDatas.get(i).getKey()));
                        startActivity(intent);
                    }
                }
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        if (savedInstanceState == null) {
            callTrailerData();
            callReviewData();
        } else {
            trailersDatas = savedInstanceState.getParcelableArrayList("trailersData");
            reviewDatas = savedInstanceState.getParcelableArrayList("reviewData");
            if (trailersDatas != null) {
                trailers_text.setVisibility(View.VISIBLE);
                trailersAdapter = new TrailersAdapter(trailersDatas, mContext);
                trailersGrid.setAdapter(trailersAdapter);
            }
            if (reviewDatas != null) {
                reviewAdapter = new ReviewAdapter(reviewDatas, mContext);
                LinearLayout.LayoutParams params = new
                        LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                params.height = 270 * reviewDatas.size();
                if (reviewDatas.size() > 1)
                    reviewList.setLayoutParams(params);
                reviewList.setAdapter(reviewAdapter);
            }
        }

        return rootView;
    }


    private void callReviewData() {
        ReviewAPI reviewAPI = retrofit.create(ReviewAPI.class);
        Call<ReviewsList> reviewCall = reviewAPI.listReview(String.valueOf(moviesData.getMovie_id()), MoviesFragment.API_KEY);

        try {
            reviewCall.enqueue(new Callback<ReviewsList>() {

                @Override
                public void onResponse(Response<ReviewsList> response, Retrofit retrofit) {
                    ReviewsList reviewsList = response.body();

                    reviewDatas = (ArrayList<ReviewData>) reviewsList.getReviewData();
                    if (reviewDatas != null) {
                        reviewAdapter = new ReviewAdapter(reviewDatas, mContext);
                        LinearLayout.LayoutParams params = new
                                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        params.height = 270 * reviewDatas.size();

                        if (reviewDatas.size() > 1)
                            reviewList.setLayoutParams(params);
                        reviewList.setAdapter(reviewAdapter);
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callTrailerData() {
        TrailersAPI trailersAPI = retrofit.create(TrailersAPI.class);
        Call<TrailersList> call = trailersAPI.listTrailersLink(String.valueOf(moviesData.getMovie_id()), MoviesFragment.API_KEY);
        try {
            call.enqueue(new Callback<TrailersList>() {
                @Override
                public void onResponse(Response<TrailersList> response, Retrofit retrofit) {
                    TrailersList trailersList = response.body();
                    trailers_text.setVisibility(View.VISIBLE);
                    trailersDatas = (ArrayList<TrailersData>) trailersList.getTrailersData();
                    trailersAdapter = new TrailersAdapter(trailersDatas, mContext);
                    trailersGrid.setAdapter(trailersAdapter);
                }

                @Override
                public void onFailure(Throwable t) {
                    trailers_text.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("moviesData", moviesData);
        outState.putParcelableArrayList("trailersData", trailersDatas);
        outState.putParcelableArrayList("reviewData", reviewDatas);
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public class TrailersAdapter extends BaseAdapter {
        List<TrailersData> trailersDatas;
        private Context context;

        TrailersAdapter(List<TrailersData> trailersData, Context mContext) {
            this.trailersDatas = trailersData;
            this.context = mContext;

            if (trailersDatas.size() > 1) {

                ViewGroup.LayoutParams layoutParams = trailersGrid.getLayoutParams();
                layoutParams.height = trailersData.size() * 200;
                trailersGrid.setLayoutParams(layoutParams);
            }
        }

        @Override
        public int getCount() {
            return trailersDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView, imagePlay;

            if (trailersDatas.get(position).getKey() != null) {
                if (convertView == null) {
                    // if it's not recycled, initialize some attributes
                    imageView = new ImageView(context);

                    imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    imageView.setPadding(1, 1, 1, 1);
                    imageView.setAdjustViewBounds(true);
                } else {
                    imageView = (ImageView) convertView;
                }

                if (trailersDatas.get(position).getKey() != null)
                    Picasso.with(context).load(YOU_TUBE_IMG_BASE_URL + trailersDatas.get(position).getKey() + "/default.jpg").noFade().into(imageView);

                return imageView;
            } else
                return null;
        }
    }


    public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

        List<ReviewData> reviewData;
        private Context context;
        private LayoutInflater inflater = null;

        ReviewAdapter(List<ReviewData> reviewDatas, Context mContext) {
            this.reviewData = reviewDatas;
            this.context = mContext;

            inflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_row, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String author = reviewData.get(position).getAuthor();
            String content = reviewData.get(position).getContent();
            holder.author.setText(author);
            holder.content.setText(content);
        }


        @Override
        public int getItemCount() {
            return reviewData.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.review_author_name)
            TextView author;
            @Bind(R.id.review_comment)
            TextView content;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

    }

}

