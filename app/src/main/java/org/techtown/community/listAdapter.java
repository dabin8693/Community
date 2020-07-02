package org.techtown.community;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class listAdapter extends RecyclerView.Adapter<listAdapter.ViewHolder> implements OnlistItemClickListener {
    OnlistItemClickListener listener;
    ArrayList<list> items = new ArrayList<list>();
    public void addItem(list item){
        items.add(item);
    }
    public void resetItem(){
        items.clear();
    }
    public void setItems(ArrayList<list> items){
        this.items = items;
    }
    public list getItem(int position){
        return items.get(position);
    }
    public void setItem(int position, list item){
        items.set(position, item);
    }


    @NonNull
    @Override
    public listAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView, this);//뷰홀더 객체생성(뷰 객체전달) return
    }

    @Override
    public void onBindViewHolder(@NonNull listAdapter.ViewHolder holder, int position) {
        list item = items.get(position);//item = arry의 데이터
        holder.setItem(item);//뷰홀더에 item저장

    }

    @Override
    public int getItemCount() {
        return items.size();//arraylist list
    }

    public void setOnItemClickListener(OnlistItemClickListener listener){
        this.listener = listener;
    }

    public void onItemClick(ViewHolder holder, View view, int position){
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, nickname, time, shownumber, comment, board;
        ImageView image;

        public ViewHolder(@NonNull View itemView, final OnlistItemClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            nickname = itemView.findViewById(R.id.nickname);
            time = itemView.findViewById(R.id.time);
            shownumber = itemView.findViewById(R.id.shownumber);
            comment = itemView.findViewById(R.id.comment);
            image = itemView.findViewById(R.id.image);
            board = itemView.findViewById(R.id.itembd);

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

        public void setItem(list item){//뷰에 item데이터로 수정
            title.setText(item.getTitle());
            nickname.setText(item.getNickname());
            time.setText(item.getTime());
            String shownumber1 = Integer.toString(item.getShownumber());
            shownumber.setText(shownumber1);
            String comment1 = Integer.toString(item.getComment());
            comment.setText(comment1);
            if(item.isImage() == true){
                image.setImageResource(R.drawable.imagefile);
            }
            int boardnumber = item.getBoard();//"자유게시판", "공지사항", "사건/사고", "이벤트", "중고거래"
            if(boardnumber == 0){
                board.setText("자유게시판");
            }else if(boardnumber == 1){
                board.setText("공지사항");
            }else if(boardnumber == 2){
                board.setText("사건/사고");
            }else if(boardnumber == 3){
                board.setText("이벤트");
            }else if(boardnumber == 4){
                board.setText("중고거래");
            }
        }

    }
}
