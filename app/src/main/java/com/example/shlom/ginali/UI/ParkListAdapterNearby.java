    package com.example.shlom.ginali.UI;

    import android.app.Activity;
    import android.content.Context;
    import android.support.annotation.NonNull;
    import android.support.v7.widget.RecyclerView;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;

    import com.example.shlom.ginali.Database.Park;
    import com.example.shlom.ginali.ParksThread.GetParks;
    import com.example.shlom.ginali.R;
    import com.google.android.gms.maps.model.LatLng;

    import java.util.List;

    public class ParkListAdapterNearby extends RecyclerView.Adapter<ParkListAdapterNearby.NearbyViewHolder> {

        private Activity mActivity;
        private Context mContext;
        private List<Park> parkList;
        private LatLng myLatLng;


        public ParkListAdapterNearby(Context mContext, Activity activity, List<Park> parkList, LatLng mLatLng) {
            this.mActivity = activity;
            this.mContext = mContext;
            this.parkList = parkList;
            this.myLatLng =mLatLng;
        }

        @NonNull
        @Override
        public NearbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ///need to make
            final View view = inflater.inflate(R.layout.park_cardview_nearby,null);
            return new NearbyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NearbyViewHolder holder, int position) {
            final Park park = parkList.get(position);
                holder.txtName.setText(park.getName());
                holder.txtAddress.setText(park.getAddress());
        }

        @Override
        public int getItemCount() {
            return parkList.size();
        }

        class NearbyViewHolder extends RecyclerView.ViewHolder {

            public TextView txtName,txtAddress;


            public NearbyViewHolder(final View itemView) {
                super(itemView);
                txtName = itemView.findViewById(R.id.textViewParkName);
                txtAddress = itemView.findViewById(R.id.textViewParkAddress);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        Park park =(Park) parkList.get(pos);
                        GetParks getParks = new GetParks();
                        getParks.getParkInfo(myLatLng,park.getPlaceId(),mContext,mActivity);


                    }
                });


            }
        }



    }
