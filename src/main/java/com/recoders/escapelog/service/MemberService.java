package com.recoders.escapelog.service;

import com.recoders.escapelog.domain.GradeType;
import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.domain.MemberType;
import com.recoders.escapelog.dto.ChangePwDto;
import com.recoders.escapelog.dto.FindPwDto;
import com.recoders.escapelog.dto.SignupDto;
import com.recoders.escapelog.repository.LibraryRepository;
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
    private final LibraryRepository libraryRepository;

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


    public boolean checkNicknameDuplicate(String nickname){
        Optional<Member> member = memberRepository.findByNickname(nickname);
        return member.isEmpty();
    }

    public boolean checkAuthenticationCode(String email, String code){
        Member member = checkEmailExistence(email);
        if (code.equals(member.getAuthenticationCode())){
            return true;
        }
        return false;
    }

    @Transactional
    public Member processNewUser(SignupDto signUpDto){

        Member member = Member.builder()
                .email(signUpDto.getEmail())
                .nickname(signUpDto.getNickname())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .memberType(MemberType.ROLE_USER)
                .gradeType(GradeType.LEVEL1)
                .joinedAt(LocalDateTime.now())
                .build();

        memberRepository.save(member);

        return member;
    }

    @Transactional
    public void checkEmailToken(String email, String token) {
        Member member = checkEmailExistence(email);

        if (!member.getEmailCheckToken().equals(token)){
            throw new IllegalArgumentException("wrong token");
        }

        member.setEmailVerified(true);
        memberRepository.save(member);

    }

    @Transactional
    public void changeUserNickname(Member member, String nickname){
        member.setNickname(nickname);
        memberRepository.save(member);
    }
    @Transactional
    public void changeUserPassword(FindPwDto findForm){
        Member member = checkEmailExistence(findForm.getEmail());
        member.setPassword(passwordEncoder.encode(findForm.getNewPassword()));
        memberRepository.save(member);
    }

    @Transactional
    public Boolean changeUserPassword(Member member, ChangePwDto changeForm){

        boolean result = passwordEncoder.matches(changeForm.getPassword(),member.getPassword());
        if (result){
            member.setPassword(passwordEncoder.encode(changeForm.getRePassword()));
            memberRepository.save(member);
        }

        return result;
    }


    @Transactional
    public Member getMember(Member member) {

        return memberRepository.getById(member.getNo());
    }

    @Transactional
    public void saveLibraryName(Member member, String libraryName){
        member.setLibraryName(libraryName);
        memberRepository.save(member);
    }

    @Transactional
    public Member getLibraryMember(String libraryName) {
        Optional<Member> optionalMember = memberRepository.findByLibraryName(libraryName);

        if(optionalMember.isEmpty()){
            throw new IllegalArgumentException("wrong libraryName");
        }

        return optionalMember.get();
    }

    @Transactional
    public void updateCount(Member member){
       int countRecode = libraryRepository.countRecode(member.getNo());
       member.setCountRecode(countRecode);
       memberRepository.save(member);
    }

    @Transactional
    public void updateGrade(Member member){

        if(member.getCountRecode() <= 10){
            member.setGradeType(GradeType.LEVEL1);
        } else if(member.getCountRecode() <= 20){
            member.setGradeType(GradeType.LEVEL2);
        } else if(member.getCountRecode() <= 30){
            member.setGradeType(GradeType.LEVEL3);
        } else if(member.getCountRecode() <= 40){
            member.setGradeType(GradeType.LEVEL4);
        }
        memberRepository.save(member);
    }

    @Transactional
    public void updateSuccess(Member member){
        int countSuccess = libraryRepository.countSuccessRecode(member.getNo());
        member.setCountSuccessRecode(countSuccess);
        memberRepository.save(member);
    }

}
