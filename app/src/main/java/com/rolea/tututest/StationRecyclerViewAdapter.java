package com.rolea.tututest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rolea.tututest.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class StationRecyclerViewAdapter extends RecyclerView.Adapter<StationRecyclerViewAdapter.ViewHolder> {

    private final int type;
    private  List<City> mOriginalValues;
    private  List<ListItem> mValues;
    private final SearchStationFragment.OnSearchFragmentInteractionListener mListener;

    public StationRecyclerViewAdapter(List<City> items, int type, SearchStationFragment.OnSearchFragmentInteractionListener listener) {
        mOriginalValues = items;
        this.type = type;
        mListener = listener;
        initializeList(mOriginalValues);
    }

    private void initializeList(List<City> values) {
        mValues = new ArrayList<>();
        for (City city : values){
            mValues.add(new HeaderItem(city));
            for (com.rolea.tututest.model.Station station : city.getStations()){
                mValues.add(new StationItem(station));
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ListItem.TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_search_station, parent, false);
            return new ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_header_item, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == ListItem.TYPE_HEADER){
            HeaderItem item = (HeaderItem) mValues.get(position);
            holder.mContentView.setText(item.getCity().getCityTitle());
        }
        if (viewType == ListItem.TYPE_STATION){
            final StationItem item = (StationItem) mValues.get(position);
            holder.mContentView.setText(item.getSation().getStationTitle());
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onChooseInteraction(item.getSation(), type);
                    }
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        return mValues.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.title_item);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public void filter(String text){
        mValues.clear();
        if (text.isEmpty()){
            initializeList(mOriginalValues);
        } else {
            List<ListItem> filterResults = new ArrayList<>();

            for (City city:mOriginalValues){
                boolean cityAdded = false;
                for (com.rolea.tututest.model.Station station:city.getStations()){
                    if (station.getStationTitle().toLowerCase().contains(text)){
                        if (!cityAdded){
                            cityAdded = true;
                            filterResults.add(new HeaderItem(city));
                        }
                        filterResults.add(new StationItem(station));
                    }
                }
            }
            mValues = filterResults;
        }
        notifyDataSetChanged();
    }
    private abstract class ListItem{
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_STATION = 1;

        abstract public int getType();
    }

    private class HeaderItem extends ListItem{

        private City city;

        // here getters and setters
        // for title and so on, built
        // using date

        public HeaderItem(City city) {
            this.city = city;
        }

        public City getCity() {
            return city;
        }

        public void setCity(City city) {
            this.city = city;
        }

        @Override
        public int getType() {
            return TYPE_HEADER;
        }
    }

    private class StationItem extends ListItem{

        private com.rolea.tututest.model.Station sation;

        public StationItem(com.rolea.tututest.model.Station sation) {
            this.sation = sation;
        }
// here getters and setters
        // for title and so on, built
        // using date

        public com.rolea.tututest.model.Station getSation() {
            return sation;
        }

        public void setSation(com.rolea.tututest.model.Station sation) {
            this.sation = sation;
        }

        @Override
        public int getType() {
            return TYPE_STATION;
        }
    }

}
