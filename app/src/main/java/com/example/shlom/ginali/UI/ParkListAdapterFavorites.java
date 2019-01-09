    package com.example.shlom.ginali.UI;

    import android.app.Activity;
    import android.content.Context;
    import android.net.Uri;
    import android.support.annotation.NonNull;
    import android.support.v7.widget.RecyclerView;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import com.example.shlom.ginali.Database.Park;
    import com.example.shlom.ginali.ParksThread.GetParks;
    import com.example.shlom.ginali.R;
    import com.google.android.gms.maps.model.LatLng;
    import com.squareup.picasso.Picasso;

    import java.util.List;

    public class ParkListAdapterFavorites extends RecyclerView.Adapter<ParkListAdapterFavorites.FavoritesViewHolder> {

        private Activity mActivity;
        private Context mContext;
        private List<Park> parkList;
        private LatLng myLatLng;
        private String[]parkPhotos;



        public ParkListAdapterFavorites(Context mContext, Activity activity, List<Park> parkList, LatLng mLatLng) {
            this.mActivity = activity;
            this.mContext = mContext;
            this.parkList = parkList;
            this.myLatLng =mLatLng;

        }

        @NonNull
        @Override
        public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ///need to make
            final View view = inflater.inflate(R.layout.park_cardview_favorites,null);
            return new ParkListAdapterFavorites.FavoritesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
            final Park park = parkList.get(position);
            holder.txtName.setText(park.getName());
            holder.txtAddress.setText(park.getAddress());
            if(park.getPhoto()!=null){
                parkPhotos = park.getPhoto().split("\\s+");
                String photo = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+parkPhotos[0]+"&key="+mContext.getString(R.string.google_maps_key)+"";
                Uri googleUri = Uri.parse(photo);
                Picasso.with(mContext).load(googleUri).into(holder.imageView);

            }
        }

        @Override
        public int getItemCount() {
            return parkList.size();
        }

        class FavoritesViewHolder extends RecyclerView.ViewHolder {

            public TextView txtName,txtAddress;
            public ImageView imageView;


            public FavoritesViewHolder(final View itemView) {
                super(itemView);
                txtName = itemView.findViewById(R.id.textViewParkName);
                txtAddress = itemView.findViewById(R.id.textViewParkAddress);
                imageView =itemView.findViewById(R.id.imageViewParkFav);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        Park park =(Park) parkList.get(pos);
                        GetParks getParks = new GetParks();
                        getParks.getDistanceFromPlace(myLatLng,new LatLng(park.getLatitude(),park.getLongitude()),mContext,mActivity,park);


                    }
                });


            }
        }



    }
