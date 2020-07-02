package org.techtown.community;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class postlistAdapter extends RecyclerView.Adapter<postlistAdapter.ViewHolder> {

    ArrayList<postlist> items = new ArrayList<postlist>();
    public void addItem(postlist item){
        items.add(item);
    }
    public void resetItem(){
        items.clear();
    }
    public void setItems(ArrayList<postlist> items){
        this.items = items;
    }
    public postlist getItem(int position){
        return items.get(position);
    }
    public void setItem(int position, postlist item){
        items.set(position, item);
    }


    @NonNull
    @Override
    public postlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.post_item, parent, false);
        return new ViewHolder(itemView);//뷰홀더 객체생성(뷰 객체전달) return
    }

    @Override
    public void onBindViewHolder(@NonNull postlistAdapter.ViewHolder holder, int position) {
        postlist item = items.get(position);//item = arry의 데이터
        holder.setItem(item);//뷰홀더에 item저장

    }

    @Override
    public int getItemCount() {
        return items.size();//arraylist list
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.postimageitem);

        }

        public void setItem(postlist item){//뷰에 item데이터로 수정

            byte[] decodedByteArray = Base64.decode(item.imagefile, Base64.NO_WRAP);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

            image.setImageBitmap(decodedBitmap);


        }



    }
}
