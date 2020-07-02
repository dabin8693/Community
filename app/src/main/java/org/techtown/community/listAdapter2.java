package org.techtown.community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class listAdapter2 extends RecyclerView.Adapter<listAdapter2.ViewHolder> implements OnlistItemClickListener5 {
    OnlistItemClickListener5 listener;
    ArrayList<array> items = new ArrayList<array>();
    public void addItem(array item){
        items.add(item);
    }
    public void resetItem(){
        items.clear();
    }
    public void setItems(ArrayList<array> items){
        this.items = items;
    }
    public array getItem(int position){
        return items.get(position);
    }
    public void setItem(int position, array item){
        items.set(position, item);
    }


    @NonNull
    @Override
    public listAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.newitem, parent, false);
        return new ViewHolder(itemView, this);//뷰홀더 객체생성(뷰 객체전달) return
    }

    @Override
    public void onBindViewHolder(@NonNull listAdapter2.ViewHolder holder, int position) {
        array item = items.get(position);//item = arry의 데이터
        holder.setItem(item);//뷰홀더에 item저장

    }

    @Override
    public int getItemCount() {
        return items.size();//arraylist list
    }

    public void setOnItemClickListener(OnlistItemClickListener5 listener){
        this.listener = listener;
    }

    public void onItemClick(ViewHolder holder, View view, int position){
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, body, link;
        ImageView image;

        public ViewHolder(@NonNull View itemView, final OnlistItemClickListener5 listener) {
            super(itemView);

            title = itemView.findViewById(R.id.title5);
            body = itemView.findViewById(R.id.body5);
            //link = itemView.findViewById(R.id.link);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });

        }

        public void setItem(array item){//뷰에 item데이터로 수정
            title.setText(item.getResult_title_list());
            body.setText(item.getResutl_description_list());
            //link.setText(item.getResult_link_list());

        }

    }
}
