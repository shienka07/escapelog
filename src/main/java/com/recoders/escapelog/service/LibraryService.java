package com.recoders.escapelog.service;

import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.domain.Recode;
import com.recoders.escapelog.domain.Theme;
import com.recoders.escapelog.dto.EditDto;
import com.recoders.escapelog.dto.RecodeDto;
import com.recoders.escapelog.repository.LibraryRepository;
import com.recoders.escapelog.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final ThemeRepository themeRepository;

    public void saveRecode(Member member, RecodeDto recodeDto) {

        Theme theme = null;
        if (recodeDto.getThemeNo() != null) {
            theme = themeRepository.findById(recodeDto.getThemeNo()).get();
        }

        Recode recode = Recode.builder()
                .member(member)
                .theme(theme)
                .title(recodeDto.getTitle())
                .contents(recodeDto.getContents())
                .rating(recodeDto.getRating())
                .breakTime(recodeDto.getBreakTime())
                .hint(recodeDto.getHint())
                .secret(recodeDto.getSecret())
                .success(recodeDto.getSuccess())
                .playerNum(recodeDto.getPlayerNum())
                .regdate(LocalDateTime.now().withNano(0))
                .imageUrl(recodeDto.getImageUrl())
                .build();

        libraryRepository.save(recode);
    }

    @Transactional
    public List<Recode> getRecodeList() {

        List<Recode> recodeEntities = libraryRepository.findAll();
        List<Recode> recodeList = new ArrayList<>();

        for (Recode recodes : recodeEntities) {
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
    public List<Recode> getMemberRecodeList(String libraryName) {
        List<Recode> recodeEntities = libraryRepository.findByMember_LibraryNameOrderByRegdateDesc(libraryName);
        List<Recode> recodeList = new ArrayList<>();

        for (Recode recodes : recodeEntities) {
            Recode recode = Recode.builder()
                    .no(recodes.getNo())
                    .title(recodes.getTitle())
                    .regdate(recodes.getRegdate())
                    .theme(recodes.getTheme())
                    .secret(recodes.getSecret())
                    .build();

            recodeList.add(recode);
        }

        return recodeList;
    }

    @Transactional
    public Recode getRecode(Long no) {
        Optional<Recode> optionalRecode = libraryRepository.findById(no);

        if (optionalRecode.isEmpty()) {
            throw new IllegalArgumentException("wrong recode no");
        }

        Recode recode = optionalRecode.get();
        recode.setNlString(System.getProperty("line.separator").toString());
        return recode;
    }


    @Transactional
    public void delete(Long no) {
        libraryRepository.deleteByNo(no);
    }

    @Transactional
    public List<Recode> searchRecode(String libraryName, String keyword) {
        List<Recode> searchEntities = libraryRepository.findByMember_LibraryNameAndTheme_ThemeNameContaining(libraryName, keyword);
        List<Recode> searchRecodeList = new ArrayList<>();

        if (searchEntities.isEmpty()) {
            return searchRecodeList;
        }

        for (Recode searchRecode : searchEntities) {
            Recode search = Recode.builder()
                    .no(searchRecode.getNo())
                    .title(searchRecode.getTitle())
                    .regdate(searchRecode.getRegdate())
                    .theme(searchRecode.getTheme())
                    .secret(searchRecode.getSecret())
                    .build();

            searchRecodeList.add(search);
        }

        return searchRecodeList;
    }

    @Transactional
    public Recode updateRecode(Long no, Member member, EditDto editDto) {
        Recode recode = libraryRepository.findByNo(no).get();
        recode.update(editDto);
        return recode;
    }

    public List<Recode> getAllReviewEntities(Long themeNo) {
        return libraryRepository.findByThemeNoOrderByRegdateDesc(themeNo);
    }

    public List<Recode> getReviewFilterEntities(Long themeNo, Integer rating) {

        return libraryRepository.findByThemeNoAndRatingOrderByRegdateDesc(themeNo, rating);
    }

    public List<RecodeDto> getReviewList(List<Recode> entities) {

        List<RecodeDto> recodeList = new ArrayList<>();

        for (Recode recodes : entities) {
            Recode recode = Recode.builder()
                    .member(recodes.getMember())
                    .no(recodes.getNo())
                    .secret(recodes.getSecret())
                    .title(recodes.getTitle())
                    .contents(recodes.getContents())
                    .regdate(recodes.getRegdate())
                    .success(recodes.getSuccess())
                    .rating(recodes.getRating())
                    .build();
            recodeList.add(RecodeDto.reviewForm(recode));
        }

        return recodeList;

    }

}

