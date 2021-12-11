package com.recoders.escapelog.security;

import com.recoders.escapelog.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class CustomUser extends User {

    private Member member;

    public CustomUser(Member member){
        super(
                member.getEmail(),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority(member.getMemberType().name()))
        );
        this.member = member;
    }
}
