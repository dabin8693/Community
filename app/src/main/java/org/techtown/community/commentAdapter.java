package org.techtown.community;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class commentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnlistItemClickListener3, OnlistItemClickListener4, OnlistItemClickListener33, OnlistItemClickListener44, OnlistItemClickListener333, OnlistItemClickListener444{

    OnlistItemClickListener3 listener;
    OnlistItemClickListener4 listener1;
    OnlistItemClickListener33 listener0;
    OnlistItemClickListener44 listener11;
    OnlistItemClickListener333 listener00;
    OnlistItemClickListener444 listener111;
    ArrayList<commentlist> items = new ArrayList<commentlist>();
    public void addItem(commentlist item){
        items.add(item);
    }
    public void resetItem(){
        items.clear();
    }
    public void removeItem(int position){
        items.remove(position);
    }
    public void setItems(ArrayList<commentlist> items){
        this.items = items;
    }
    public commentlist getItem(int position){
        return items.get(position);
    }
    public void setItem(int position, commentlist item){
        items.set(position, item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == 0)
        {
            view = inflater.inflate(R.layout.comment_item, parent, false);
            return new CenterViewHolder(view,listener, listener0, listener00);
        }
        else if(viewType == 1)
        {
            view = inflater.inflate(R.layout.recomment_item, parent, false);
            return new LeftViewHolder(view,listener1, listener11, listener111);
        }
        else
        {
            view = inflater.inflate(R.layout.comment_item, parent, false);
            return new CenterViewHolder(view,listener, listener0, listener00);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        if(viewHolder instanceof CenterViewHolder)
        {
            ((CenterViewHolder) viewHolder).nickname.setText(items.get(position).getNickname());
            ((CenterViewHolder) viewHolder).body.setText(items.get(position).getBody());
            ((CenterViewHolder) viewHolder).time.setText(items.get(position).getTime());
            if(items.get(position).getImage() != null) {
                byte[] decodedByteArray = Base64.decode(items.get(position).getImage(), Base64.NO_WRAP);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
                ((CenterViewHolder) viewHolder).image.setImageBitmap(decodedBitmap);
            }
        }
        else if(viewHolder instanceof LeftViewHolder)
        {
            ((LeftViewHolder) viewHolder).nickname.setText(items.get(position).getNickname());
            ((LeftViewHolder) viewHolder).body.setText(items.get(position).getBody());
            ((LeftViewHolder) viewHolder).time.setText(items.get(position).getTime());
            if(items.get(position).getTonickname() != null) {
                ((LeftViewHolder) viewHolder).tonickname.setText(items.get(position).getTonickname());
            }
            if(items.get(position).getImage() != null) {
                byte[] decodedByteArray = Base64.decode(items.get(position).getImage(), Base64.NO_WRAP);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
                ((LeftViewHolder) viewHolder).image.setImageBitmap(decodedBitmap);
            }
        }
        else
        {

        }
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }
    public void setOnItemClickListener(OnlistItemClickListener3 listener){
        this.listener = listener;
    }
    public void setOnItemClickListener1(OnlistItemClickListener4 listener1){
        this.listener1 = listener1;
    }
    public void setOnItemClickListener0(OnlistItemClickListener33 listener0){
        this.listener0 = listener0;
    }
    public void setOnItemClickListener00(OnlistItemClickListener333 listener00){
        this.listener00 = listener00;
    }
    public void setOnItemClickListener11(OnlistItemClickListener44 listener11){
        this.listener11 = listener11;
    }
    public void setOnItemClickListener111(OnlistItemClickListener444 listener111){
        this.listener111 = listener111;
    }
    public void onItemClick(commentAdapter.CenterViewHolder holder, View view, int position){
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }
    public void onItemClick0(commentAdapter.CenterViewHolder holder, View view, int position){
        if(listener0 != null){
            listener0.onItemClick(holder, view, position);
        }
    }
    public void onItemClick00(commentAdapter.CenterViewHolder holder, View view, int position){
        if(listener00 != null){
            listener00.onItemClick(holder, view, position);
        }
    }
    public void onItemClick(commentAdapter.LeftViewHolder holder, View view, int position){
        if(listener1 != null){
            listener1.onItemClick(holder, view, position);
        }
    }
    public void onItemClick11(commentAdapter.LeftViewHolder holder, View view, int position){
        if(listener11 != null){
            listener11.onItemClick(holder, view, position);
        }
    }
    public void onItemClick111(commentAdapter.LeftViewHolder holder, View view, int position){
        if(listener111 != null){
            listener111.onItemClick(holder, view, position);
        }
    }
    public class CenterViewHolder extends RecyclerView.ViewHolder{//0//댓글
        TextView nickname, body, time, re;
        ImageView image, menu;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        CenterViewHolder(View itemView, final OnlistItemClickListener3 listener, final OnlistItemClickListener33 listener0, final OnlistItemClickListener333 listener00)
        {
            super(itemView);

            menu = itemView.findViewById(R.id.cmenu);
            image = itemView.findViewById(R.id.commentimage);
            nickname = itemView.findViewById(R.id.commentnickname);
            body = itemView.findViewById(R.id.commentbody);
            time = itemView.findViewById(R.id.commenttime);
            re = itemView.findViewById(R.id.recomment);
            image.setBackground(new ShapeDrawable(new OvalShape()));
            image.setClipToOutline(true);
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener00 != null){
                        listener00.onItemClick(commentAdapter.CenterViewHolder.this, v, position);
                    }
                }
            });
            re.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(commentAdapter.CenterViewHolder.this, v, position);
                    }
                }
            });
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener0 != null){
                        listener0.onItemClick(commentAdapter.CenterViewHolder.this,v,position);
                    }
                }
            });
        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder{//1//답글
        //TextView content;
        TextView nickname, body, time, re, tonickname;
        ImageView image, menu;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        LeftViewHolder(View itemView, final OnlistItemClickListener4 listener1, final OnlistItemClickListener44 listener11, final OnlistItemClickListener444 listener111)
        {
            super(itemView);

            menu = itemView.findViewById(R.id.remenu);
            image = itemView.findViewById(R.id.commentimage1);
            nickname = itemView.findViewById(R.id.commentnickname1);
            body = itemView.findViewById(R.id.commentbody1);
            time = itemView.findViewById(R.id.commenttime1);
            re = itemView.findViewById(R.id.recomment1);
            tonickname = itemView.findViewById(R.id.tonickname);
            image.setBackground(new ShapeDrawable(new OvalShape()));
            image.setClipToOutline(true);
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener111 != null){
                        listener111.onItemClick(commentAdapter.LeftViewHolder.this,v,position);
                    }
                }
            });
            re.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener1 != null){
                        listener1.onItemClick(commentAdapter.LeftViewHolder.this, v, position);
                    }
                }
            });
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener11 != null){
                        listener11.onItemClick(commentAdapter.LeftViewHolder.this,v,position);
                    }
                }
            });
        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        TextView name;
        ImageView image;

        RightViewHolder(View itemView)
        {
            super(itemView);


        }
    }

}