package org.techtown.community;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail {
    String user = "보안상";
    String password = "보안상";
    String code;

    public void sendSecurityCode(String sendTo) {//회원가입
        try {
            GMailSender gMailSender = new GMailSender(user, password);
            code = gMailSender.getEmailCode();
            gMailSender.sendMail("[커뮤니티]인증번호", "인증번호 : "+code, sendTo);

            //Toast.makeText(context, "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
        } catch (SendFailedException e) {
            //Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            //Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void sendPasswordCode(String password, String sendTo) {//비번찾기
        try {
            GMailSender gMailSender = new GMailSender(user, this.password);
            gMailSender.sendMail("비밀번호", password, sendTo);

            //Toast.makeText(context, "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
        } catch (SendFailedException e) {
            //Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            //Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

