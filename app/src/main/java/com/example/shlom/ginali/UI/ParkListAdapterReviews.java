    package com.example.shlom.ginali.UI;

    import android.app.Activity;
    import android.content.Context;
    import android.support.annotation.NonNull;
    import android.support.v7.widget.RecyclerView;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.RatingBar;
    import android.widget.TextView;

    import com.example.shlom.ginali.Database.Review;
    import com.example.shlom.ginali.R;

    import java.util.List;

    public class ParkListAdapterReviews extends RecyclerView.Adapter<ParkListAdapterReviews.ReviewViewHolder> {

        private Activity mActivity;
        private Context mContext;
        private List<Review> reviewList;


        public ParkListAdapterReviews(Context mContext, Activity activity, List<Review> reviewList) {
            this.mActivity = activity;
            this.mContext = mContext;
            this.reviewList = reviewList;

        }

        @NonNull
        @Override
        public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ///need to make
            final View view = inflater.inflate(R.layout.review_bars,null);
            return new ReviewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
            final Review review = reviewList.get(position);
                holder.txtReview.setText(review.getReview());
                holder.txtADate.setText(review.getDate());
                holder.ratingBarReview.setRating(review.getRating());
        }

        @Override
        public int getItemCount() {
            return reviewList.size();
        }

        class ReviewViewHolder extends RecyclerView.ViewHolder {

            public TextView txtReview,txtADate;
            public RatingBar ratingBarReview;


            public ReviewViewHolder(final View itemView) {
                super(itemView);
                txtReview = itemView.findViewById(R.id.textviewGetReview);
                txtADate = itemView.findViewById(R.id.textviewGetReviewDate);
                ratingBarReview = itemView.findViewById(R.id.ratingbarGetReview);



            }
        }



    }
