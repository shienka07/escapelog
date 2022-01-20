package com.recoders.escapelog.service;

import com.recoders.escapelog.domain.Feedback;
import com.recoders.escapelog.domain.FeedbackType;
import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.domain.Theme;
import com.recoders.escapelog.repository.FeedbackRepository;
import com.recoders.escapelog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final MemberRepository memberRepository;

    @Transactional
    public void generateEmailCheckToken(Member member){
        member.setEmailCheckToken(UUID.randomUUID().toString());
        memberRepository.save(member);
    }

    @Transactional
    public void getAuthenticationCode(Member member){
        Random random = new Random();
        List<String> codeKeyList = new ArrayList<>();

        for(int i =0; i<4;i++) {
            codeKeyList.add(String.valueOf((char)(random.nextInt( 26) + 65)));
        }
        for(int i =0; i<4;i++) {
            codeKeyList.add(String.valueOf(random.nextInt(10)));
        }
        Collections.shuffle(codeKeyList);
        member.setAuthenticationCode(String.join("",codeKeyList));
        memberRepository.save(member);
    }

    public void sendCheckEmail(Member member) {

        generateEmailCheckToken(member);

        //TODO - 추후에 https로 바꿔야함.
        String path = "http://localhost:8080/email_check_token?token="+member.getEmailCheckToken()+"&email=" + member.getEmail();

        String content = "<div style=\"font-family: 'Apple SD Gothic Neo','sans-serif' !important; width: 540px; height: 600px; border-top: 4px solid #464248; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">\n" +
                "\t\n" +
                "\t<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">\n" +
                "\t\t<img src='http://drive.google.com/uc?export=view&id=1lAzqJ2oj-9NXobWu81dvLpMk_g3cgTsW'><br/>\n" +
                "\t\t<span style=\"color: #464248; font-weight: bold ;\">메일인증</span> 안내입니다.\n" +
                "\t</h1>\n" +
                "\t<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">\n" +
                "\t\t안녕하세요.<br />\n" +
                "\t\t<b style=\"color: #eec76d;\">탈출 일지</b> 홈페이지를 이용해 주셔서 진심으로 감사드립니다.<br />\n" +
                "\t\t아래 <b style=\"color: #eec76d;\">'메일 인증'</b> 버튼을 클릭하여 인증을 완료해 주세요.<br />\n" +
                "\t\t감사합니다.\n" +
                "\t</p>\n" +
                "\n" +
                "\t<a style=\"color: #FFF; text-decoration: none; text-align: center;\" href=\""+path+"\" target=\"_blank\">\n" +
                "\t\t<p style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #eec76d; line-height: 45px; vertical-align: middle; font-size: 16px;\">\n" +
                "\t\t\t메일 인증</p></a>\n" +
                "\n" +
                "\n" +
                "\t<div style=\"border-top: 1px solid #DDD; padding: 5px;\">\n" +
                "\t\t<p style=\"font-size: 13px; line-height: 21px; color: #555;\">\n" +
                "\t\t\t만약 버튼이 정상적으로 클릭되지 않는다면, 아래 링크를 복사하여 접속해 주세요.<br />\n" +
                "\t\t\t"+path+"\n" +
                "\t\t</p>\n" +
                "\t</div>\n" +
                "</div>";

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            message.setTo(member.getEmail());
            message.setSubject("Escape Log 회원 가입 인증");
            message.setText(content,true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e ) {
            e.printStackTrace();
        }
    }

    public void sendAuthenticationCodeMail(Member member) {
        getAuthenticationCode(member);

        String content = "<div style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 540px; height: 600px; border-top: 4px solid #464248; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">\n" +
                "\t<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">\n" +
                "\t\t<img src='http://drive.google.com/uc?export=view&id=1lAzqJ2oj-9NXobWu81dvLpMk_g3cgTsW'><br/>\n" +
                "\t\t<span style=\"color: #464248; font-weight: bold;\">비밀번호 재설정</span> 안내입니다.\n" +
                "\t</h1>\n" +
                "\t<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">\n" +
                "\t\t안녕하세요.<br />\n" +
                "\t\t비밀번호 재설정을 위한 <b style=\"color: #eec76d;\">인증번호</b>를 발급하였습니다.<br />\n" +
                "\t\t탈출일지 비밀번호 재설정 페이지에서 아래의 <b style=\"color: #eec76d;\">인증번호</b>를 입력하여\n" +
                "\t\t비밀번호를 재설정하실 수 있습니다.<br />\n" +
                "\t\t감사합니다.\n" +
                "\t</p>\n" +
                "\n" +
                "\t<p style=\"font-size: 16px; margin: 40px 5px 20px; line-height: 10px;\">\n" +
                "\t\t인증번호: <br /><br/>\n" +
                "\t\t<span style=\"display: inline-block; width: 250px; height: 45px; background: #eec76d; line-height: 45px; vertical-align: middle; font-size: 16px; text-align: center;\">\n" +
                "\t\t\t<span style=\"font-size: 24px; font-weight: bold; margin: 20px 20px;\">"+member.getAuthenticationCode()+"</span>\n" +
                "\t\t</span>\n" +
                "\t</p>\n" +
                "\n" +
                "</div>";

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            message.setTo(member.getEmail());
            message.setSubject("Escape Log 비밀번호 재설정");
            message.setText(content,true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e ) {
            e.printStackTrace();
        }
    }

}

@EnableScheduling
@Component
@RequiredArgsConstructor
class EmailScheduler {

    private final JavaMailSender mailSender;
    private final FeedbackRepository feedbackRepository;

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void sendFeedbackMail() {
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        List<Feedback> feedbackList = feedbackRepository.findByRegdate(yesterdayDate);
        int newThemeNo = 1, themeInfoNo = 1;
        FeedbackType feedbackType;
        String content = "";


        String newThemeFeedbackContent = "<table style=\"border-collapse: separate;border-spacing: 1.5px;text-align: center;line-height: 1.5;margin: 5px 5px;\">\n" +
                "    <caption style=\"border-color: #474249;border-width: 0 0 0 6px;border-style: solid;padding: 1px 0 0 12px;line-height: 1.7;text-align: left; margin: 1.5px;font-weight: bold;font-size:large;\">테마 추가 문의</caption>\n" +
                "    <thead>\n" +
                "    <tr>\n" +
                "        <th scope=\"cols\" style=\"width: 45px;padding: 10px;font-weight: bold;vertical-align: top;color: #fff;background: #474249;\">번호</th>\n" +
                "        <th scope=\"cols\" style=\"width: 80px;padding: 10px;font-weight: bold;vertical-align: top;color: #fff;background: #474249;\">작성자</th>\n" +
                "        <th scope=\"cols\" style=\"width: 100px;padding: 10px;font-weight: bold;vertical-align: top;color: #fff;background: #474249;\">작성날짜</th>\n" +
                "        <th scope=\"cols\" style=\"width: 100px;padding: 10px;font-weight: bold;vertical-align: top;color: #fff;background: #474249;\">문의타입</th>\n" +
                "        <th scope=\"cols\" style=\"width: 100px;padding: 10px;font-weight: bold;vertical-align: top;color: #fff;background: #474249;\">지역이름</th>\n" +
                "        <th scope=\"cols\" style=\"width: 100px;padding: 10px;font-weight: bold;vertical-align: top;color: #fff;background: #474249;\">테마이름</th>\n" +
                "        <th scope=\"cols\" style=\"width: 80px;padding: 10px;font-weight: bold;vertical-align: top;color: #fff;background: #474249;\">문의내용</th>\n" +
                "    </tr>\n" +
                "    </thead>";

        String themeInfoFeedbackContent = "<table style=\"border-collapse: separate;border-spacing: 1.5px;text-align: center;line-height: 1.5;margin: 5px 5px;\">\n" +
                "    <caption style=\"border-color: #474249;border-width: 0 0 0 6px;border-style: solid;padding: 1px 0 0 12px;line-height: 1.7;text-align: left; margin: 1.5px;font-weight: bold;font-size:large;\">잘못된 테마정보 문의</caption>\n" +
                "    <thead>\n" +
                "    <tr>\n" +
                "        <th scope=\"cols\" style=\"width: 45px;padding: 10px;font-weight: bold;vertical-align: top;color: #fff;background: #474249;\">번호</th>\n" +
                "        <th scope=\"cols\" style=\"width: 80px;padding: 10px;font-weight: bold;vertical-align: top;color: #fff;background: #474249;\">작성자</th>\n" +
                "        <th scope=\"cols\" style=\"width: 100px;padding: 10px;font-weight: bold;vertical-align: top;color: #fff;background: #474249;\">작성날짜</th>\n" +
                "        <th scope=\"cols\" style=\"width: 100px;padding: 10px;font-weight: bold;vertical-align: top;color: #fff;background: #474249;\">문의타입</th>\n" +
                "        <th scope=\"cols\" style=\"width: 100px;padding: 10px;font-weight: bold;vertical-align: top;color: #fff;background: #474249;\">테마번호</th>\n" +
                "        <th scope=\"cols\" style=\"width: 100px;padding: 10px;font-weight: bold;vertical-align: top;color: #fff;background: #474249;\">테마이름</th>\n" +
                "        <th scope=\"cols\" style=\"width: 80px;padding: 10px;font-weight: bold;vertical-align: top;color: #fff;background: #474249;\">문의내용</th>\n" +
                "    </tr>\n" +
                "    </thead>";


        for (Feedback feedback : feedbackList) {

            feedbackType = feedback.getFeedbackType();

            if (feedbackType == FeedbackType.OPEN) {
                newThemeFeedbackContent += getNewThemeFeedbackTd((newThemeNo++), feedback);
            } else {
                themeInfoFeedbackContent += getThemeInfoFeedbackTd((themeInfoNo++), feedback);
            }

        }
        content = newThemeFeedbackContent + "</table><br/>" + themeInfoFeedbackContent + "</table>";

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            message.setTo(adminEmail);
            message.setSubject("Escape Log " + yesterdayDate + " 문의");
            message.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String getNewThemeFeedbackTd(int no, Feedback feedback) {
        String writer = feedback.getMember().getEmail();
        LocalDate date = feedback.getRegdate();
        FeedbackType feedbackType = feedback.getFeedbackType();
        String areaName = feedback.getAreaType().getKrName();
        String newThemeName = feedback.getNewThemeName();
        String content = feedback.getContents();
        return "<tr>\n" +
                "        <td style=\"width: 45px;padding: 10px;vertical-align: top;background: #f5efe5;\">" + no + "\n" +
                "        </td><td style=\"width: 180px;padding: 10px;vertical-align: top;background: #f5efe5;\">" + writer + "\n" +
                "        </td><td style=\"width: 100px;padding: 10px;vertical-align: top;background: #f5efe5;\">" + date + "\n" +
                "        </td><td style=\"width: 80px;padding: 10px;vertical-align: top;background: #f5efe5;\">"+ feedbackType +"\n" +
                "        </td><td style=\"width: 80px;padding: 10px;vertical-align: top;background: #f5efe5;\">" + areaName + "\n" +
                "        </td><td style=\"width: 180px;padding: 10px;vertical-align: top;background: #f5efe5;\">" + newThemeName + "\n" +
                "        </td><td style=\"width: 300px;padding: 10px;vertical-align: top;background: #f5efe5;\">" + content + "\n" +
                "    </td></tr>";

    }

    public String getThemeInfoFeedbackTd(int no, Feedback feedback) {
        String writer = feedback.getMember().getEmail();
        LocalDate date = feedback.getRegdate();
        FeedbackType feedbackType = feedback.getFeedbackType();
        Theme theme = feedback.getTheme();
        long themeNo = theme.getNo();
        String themeName = theme.getThemeName();
        String content = feedback.getContents();
        return "<tr>\n" +
                "        <td style=\"width: 45px;padding: 10px;vertical-align: top;background: #f5efe5;\">" + no + "\n" +
                "        </td><td style=\"width: 180px;padding: 10px;vertical-align: top;background: #f5efe5;\">" + writer + "\n" +
                "        </td><td style=\"width: 100px;padding: 10px;vertical-align: top;background: #f5efe5;\">" + date + "\n" +
                "        </td><td style=\"width: 80px;padding: 10px;vertical-align: top;background: #f5efe5;\">" + feedbackType + "\n" +
                "        </td><td style=\"width: 80px;padding: 10px;vertical-align: top;background: #f5efe5;\">" + themeNo + "\n" +
                "        </td><td style=\"width: 180px;padding: 10px;vertical-align: top;background: #f5efe5;\">" + themeName + "\n" +
                "        </td><td style=\"width: 300px;padding: 10px;vertical-align: top;background: #f5efe5;\">" + content + "\n" +
                "    </td></tr>";

    }
}
