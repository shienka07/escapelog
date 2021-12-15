package com.recoders.escapelog.service;

import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.domain.Recode;
import com.recoders.escapelog.dto.RecodeDto;
import com.recoders.escapelog.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;


    public void saveRecode(Member member, RecodeDto recodeDto){
        Recode recode = Recode.builder()
                .member(member)
                .title(recodeDto.getTitle())
                .contents(recodeDto.getContents())
                .rating(recodeDto.getRating())
                .breakTime(recodeDto.getBreakTime())
                .hint(recodeDto.getHint())
                .success(recodeDto.getSuccess())
                .playerNum(recodeDto.getPlayerNum())
                .build();

        libraryRepository.save(recode);
    }

    @Transactional
    public List<Recode> getRecodeList() {

        List<Recode> recodeEntities = libraryRepository.findAll();
        List<Recode> recodeList = new ArrayList<>();

        for(Recode recodes : recodeEntities) {
            Recode recode = Recode.builder()
                    .title(recodes.getTitle())
                    .regdate(recodes.getRegdate())
                    .theme(recodes.getTheme())
                    .build();

            recodeList.add(recode);
        }

        return recodeList;
    }

    @Transactional
    public List<Recode> getMemberRecodeList(String nickname) {
        List<Recode> recodeEntities = libraryRepository.findByMember_NicknameOrderByRegdateDesc(nickname);
        List<Recode> recodeList = new ArrayList<>();

        for(Recode recodes : recodeEntities) {
            Recode recode = Recode.builder()
                    .no(recodes.getNo())
                    .title(recodes.getTitle())
                    .regdate(recodes.getRegdate())
//                    .theme(recodes.getTheme())
                    .build();

            recodeList.add(recode);
        }

        return recodeList;
    }



}
