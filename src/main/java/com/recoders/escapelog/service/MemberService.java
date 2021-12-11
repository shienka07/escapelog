package com.recoders.escapelog.service;

import com.recoders.escapelog.domain.GradeType;
import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.domain.MemberType;
import com.recoders.escapelog.dto.FindPwDto;
import com.recoders.escapelog.dto.SignupDto;
import com.recoders.escapelog.repository.MemberRepository;
import com.recoders.escapelog.security.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optional = memberRepository.findByEmail(username);
        if (optional.isEmpty()){
            throw new UsernameNotFoundException(username);
        }
        return new CustomUser(optional.get());
    }

    public void checkEmailDuplicate(String email){
        Optional<Member> member = memberRepository.findByEmail(email);
        if(!member.isEmpty()){
            throw new IllegalArgumentException("email already exists");
        }
    }

    public Member checkEmailExistence(String email){
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isEmpty()){
            throw new IllegalArgumentException("no email");
        }
        return member.get();
    }


    public void checkNicknameDuplicate(String nickname){
        Optional<Member> member = memberRepository.findByNickName(nickname);
        if(!member.isEmpty()){
            throw new IllegalArgumentException("nickname already exists");
        }
    }

    public boolean checkAuthenticationCode(String email, String code){
        Member member = checkEmailExistence(email);
        if (code.equals(member.getAuthenticationCode())){
            return true;
        }
        return false;
    }

    @Transactional
    public void changeUserPassword(FindPwDto findForm){
        Member member = checkEmailExistence(findForm.getEmail());
        member.setPassword(passwordEncoder.encode(findForm.getPassword()));
        memberRepository.save(member);
    }

    @Transactional
    public Member processNewUser(SignupDto signUpDto){

        Member member = Member.builder()
                .email(signUpDto.getEmail())
                .nickName(signUpDto.getNickname())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .memberType(MemberType.ROLE_USER)
                .gradeType(GradeType.LEVEL1)
                .joinedAt(LocalDateTime.now())
                .build();

        memberRepository.save(member);

        return member;
    }


}
