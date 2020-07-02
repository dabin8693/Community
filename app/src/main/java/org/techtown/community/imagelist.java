package org.techtown.community;

import java.io.InputStream;

public class imagelist {
    String image;//글수정
    InputStream inputStream;//글쓰기

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public imagelist(String image){

        this.image = image;//이미지파일 스트링 형식
    }


    public imagelist(InputStream inputStream){//글쓰기
        this.inputStream = inputStream;//이미지uri를 스트림파일로 변환후 여기에저장
    }
}
