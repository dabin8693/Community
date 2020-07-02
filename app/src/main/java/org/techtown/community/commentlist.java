package org.techtown.community;

public class commentlist {
    String image, nickname, body, time, tonickname;
    int type;// 0 = 댓글 1 = 답글

    public commentlist(String image, String nickname, String body, String time, int type, String tonickname) {
        this.image = image;
        this.nickname = nickname;
        this.body = body;
        this.time = time;
        this.type = type;
        this.tonickname = tonickname;
    }

    public String getTonickname() {
        return tonickname;
    }

    public void setTonickname(String tonickname) {
        this.tonickname = tonickname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
