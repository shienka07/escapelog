package com.recoders.escapelog.service;

import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.domain.Recode;
import com.recoders.escapelog.domain.Theme;
import com.recoders.escapelog.dto.EditDto;
import com.recoders.escapelog.dto.RecodeDto;
import com.recoders.escapelog.dto.RecodeInfoDto;
import com.recoders.escapelog.repository.LibraryRepository;
import com.recoders.escapelog.repository.ThemeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                .imagePath(recodeDto.getImagePath())
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
    public Page<Recode> getMemberRecodeList(String libraryName, Pageable pageable) {
        return libraryRepository.findByMember_LibraryNameOrderByRegdateDesc(libraryName, pageable);
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
    public Page<Recode> searchRecode(String libraryName, String keyword, Pageable pageable) {
        return libraryRepository.findByMember_LibraryNameAndTheme_ThemeNameContaining(libraryName, keyword, pageable);
    }


    @Transactional
    public Recode updateRecode(Long no, Member member, EditDto editDto) {

        if (editDto.getThemeNo() != null) {
            editDto.setTheme(themeRepository.findById(editDto.getThemeNo()).get());
        }

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

    public List<RecodeInfoDto> getReviewList(List<Recode> entities) {

        List<RecodeInfoDto> recodeList = new ArrayList<>();

        for (Recode recode : entities) {
            recodeList.add(getReviewInfoDto(recode));
        }
        return recodeList;
    }

    public RecodeInfoDto getReadInfoDto(Recode recode){
        Theme theme = recode.getTheme();
        Member member = recode.getMember();
        String themeName=null, themeShopName=null, themeImgUrl=null;

        if (theme!=null){
            themeName = theme.getThemeName();
            themeShopName = theme.getShopName();
            themeImgUrl = AmazonS3Service.getImageUrl(theme.getImagePath());
        }

        return RecodeInfoDto.builder()
                .no(recode.getNo())
                .memberNo(member.getNo())
                .nickname(member.getNickname())
                .libraryName(member.getLibraryName())
                .title(recode.getTitle())
                .contents(recode.getContents())
                .secret(recode.getSecret())
                .regdate(recode.getRegdate())
                .success(recode.getSuccess())
                .rating(recode.getRating())
                .hint(recode.getHint())
                .playerNum(recode.getPlayerNum())
                .breakTime(recode.getBreakTime())
                .nlString(recode.getNlString())
                .themeName(themeName)
                .themeShopName(themeShopName)
                .themeImageUrl(themeImgUrl)
                .recodeImageUrl(AmazonS3Service.getImageUrl(recode.getImagePath()))
                .build();
    }



    public RecodeInfoDto getReviewInfoDto(Recode recode){
       return RecodeInfoDto.builder()
                .no(recode.getNo())
                .nickname(recode.getMember().getNickname())
                .secret(recode.getSecret())
                .title(recode.getTitle())
                .contents(recode.getContents())
                .regdate(recode.getRegdate())
                .success(recode.getSuccess())
                .rating(recode.getRating())
                .build();
    }

    public RecodeInfoDto getEditInfoDto(Recode recode){

        Theme theme = recode.getTheme();
        Member member = recode.getMember();
        String themeName=null, themeShopName=null;
        Long themeNo = null;

        if (theme!=null){
            themeNo = theme.getNo();
            themeName = theme.getThemeName();
            themeShopName = theme.getShopName();
        }

        return RecodeInfoDto.builder()
                .no(recode.getNo())
                .memberNo(member.getNo())
                .nickname(member.getNickname())
                .libraryName(member.getLibraryName())
                .title(recode.getTitle())
                .contents(recode.getContents())
                .secret(recode.getSecret())
                .success(recode.getSuccess())
                .rating(recode.getRating())
                .hint(recode.getHint())
                .playerNum(recode.getPlayerNum())
                .breakTime(recode.getBreakTime())
                .nlString(recode.getNlString())
                .themeNo(themeNo)
                .themeName(themeName)
                .themeShopName(themeShopName)
                .recodeImageUrl(AmazonS3Service.getImageUrl(recode.getImagePath()))
                .build();
    }


    public List<RecodeInfoDto> getRecodeInfoDtoList(Page<Recode> recodeList){
        List<RecodeInfoDto> recodeInfoDtoList = new ArrayList<>();

        for (Recode recode : recodeList){

            Theme theme = recode.getTheme();
            String themeName=null, themeImgUrl=null;

            if (theme!=null){
                themeName = theme.getThemeName();
                themeImgUrl = AmazonS3Service.getImageUrl(theme.getImagePath());
            }

            RecodeInfoDto infoDto = RecodeInfoDto.builder()
                    .no(recode.getNo())
                    .title(recode.getTitle())
                    .secret(recode.getSecret())
                    .regdate(recode.getRegdate())
                    .themeName(themeName)
                    .themeImageUrl(themeImgUrl)
                    .build();

            recodeInfoDtoList.add(infoDto);
        }

        return recodeInfoDtoList;
    }
}

