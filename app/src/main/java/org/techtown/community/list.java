package org.techtown.community;

public class list {
    String title;
    String nickname;
    String time;
    int shownumber;
    int comment;
    int board;


    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    boolean image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getShownumber() {
        return shownumber;
    }

    public int getBoard() {
        return board;
    }

    public void setBoard(int board) {
        this.board = board;
    }

    public void setShownumber(int shownumber) {
        this.shownumber = shownumber;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public list(String title, String nickname, String time, int shownumber, int comment, boolean image, int board){
        this.title = title;
        this.nickname = nickname;
        this.time = time;
        this.shownumber = shownumber;
        this.comment = comment;
        this.image = image;//리사이클러뷰 이미지 썸네일 // 이미지가 있으면 썸네일이 있고 없으면 없고
        this.board = board;
    }

}
