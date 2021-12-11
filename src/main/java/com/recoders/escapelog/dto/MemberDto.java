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

    public MemberDto(Long no, String email, String nickname, GradeType gradeType){
        this.no = no;
        this.email = email;
        this.nickname = nickname;
        this.gradeType = gradeType;
    }

    public static MemberDto memberBasicInfo(Member member){
        return new MemberDto(member.getNo(), member.getEmail(), member.getNickName(),
                member.getGradeType());
    }


}
