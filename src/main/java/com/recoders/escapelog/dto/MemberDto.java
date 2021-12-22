package com.recoders.escapelog.dto;


import com.recoders.escapelog.domain.GradeType;
import com.recoders.escapelog.domain.Member;
import lombok.Getter;

@Getter
public class MemberDto {

    private long no;
    private String email;
    private String nickname;
    private GradeType gradeType;
    private boolean emailVerified;

    public MemberDto(Long no, String email, String nickname, GradeType gradeType){
        this.no = no;
        this.email = email;
        this.nickname = nickname;
        this.gradeType = gradeType;
    }

    public MemberDto(Long no, String email, String nickname, GradeType gradeType, Boolean emailVerified){
        this.no = no;
        this.email = email;
        this.nickname = nickname;
        this.gradeType = gradeType;
        this.emailVerified = emailVerified;
    }

    public static MemberDto memberBasicInfo(Member member){
        return new MemberDto(member.getNo(), member.getEmail(), member.getNickname(),
                member.getGradeType());
    }

    public static MemberDto memberMyPageInfo(Member member){
        return new MemberDto(member.getNo(), member.getEmail(), member.getNickname(),
                member.getGradeType(), member.isEmailVerified());
    }

}
