package feicuiedu.com.gitdroid.github.repos;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import feicuiedu.com.gitdroid.R;

class ReposAdapter extends BaseAdapter{


    private final ArrayList<String> data;

    public ReposAdapter(){
        data = new ArrayList<>();
    }

    @Override public int getCount() {
        return data.size();
    }

    @Override public Object getItem(int position) {
        return data.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.layout_item_repo, parent, false);
        }
        return convertView;
    }


    public void clear(){
        data.clear();
        notifyDataSetChanged();
    }

    public void add(String item){
        data.add(item);
        notifyDataSetChanged();
    }

}
