package org.techtown.community;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class imagelistAdapter extends RecyclerView.Adapter<imagelistAdapter.ViewHolder> implements OnlistItemClickListener2 {
    OnlistItemClickListener2 listener;
    ArrayList<imagelist> items = new ArrayList<imagelist>();
    public void addItem(imagelist item){
        items.add(item);
    }
    public void resetItem(){
        items.clear();
    }
    public void removeItem(int position){
        items.remove(position);
    }
    public void setItems(ArrayList<imagelist> items){
        this.items = items;
    }
    public imagelist getItem(int position){
        return items.get(position);
    }
    public void setItem(int position, imagelist item){
        items.set(position, item);
    }


    @NonNull
    @Override
    public imagelistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.write_item, parent, false);
        return new ViewHolder(itemView,this);//뷰홀더 객체생성(뷰 객체전달) return
    }


    @Override
    public void onBindViewHolder(@NonNull imagelistAdapter.ViewHolder holder, int position) {
        imagelist item = items.get(position);//item = arry의 데이터
        holder.setItem(item);//뷰홀더에 item저장

    }

    @Override
    public int getItemCount() {
        return items.size();//arraylist list
    }

    public void setOnItemClickListener(OnlistItemClickListener2 listener){
        this.listener = listener;
    }

    public void onItemClick(ViewHolder holder, View view, int position){
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image, exit;

        public ViewHolder(@NonNull View itemView, final OnlistItemClickListener2 listener) {
            super(itemView);

            image = itemView.findViewById(R.id.imageitem);
            exit = itemView.findViewById(R.id.imageitemexit);

            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });

        }

        public void setItem(imagelist item){//뷰에 item데이터로 수정

            if(item.getImage() != null){//글수정
                byte[] decodedByteArray = Base64.decode(item.getImage(), Base64.NO_WRAP);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

                image.setImageBitmap(decodedBitmap);

            }else {//글쓰기
                Bitmap img = BitmapFactory.decodeStream(item.getInputStream());
                image.setImageBitmap(img);
            }

        }



    }
}
