package com.recoders.escapelog.service;

import com.recoders.escapelog.domain.AreaType;
import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.domain.Theme;
import com.recoders.escapelog.dto.QThemeDto;
import com.recoders.escapelog.dto.ThemeBasicDto;
import com.recoders.escapelog.dto.ThemeInfoDto;
import com.recoders.escapelog.repository.LibraryRepository;
import com.recoders.escapelog.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final LibraryRepository libraryRepository;

    @Transactional
    public void saveCsvThemeInfo() throws IOException {
        ClassPathResource resource = new ClassPathResource("csv/themes.csv");

        List<String> stringList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8);

        List<Theme> themeList = new ArrayList<>();
        for(String s: stringList){
            String[] arr = s.replaceAll("^\"|\"|$", "").split("\\|");
            int level = 0;
            if (!arr[5].equals("-")){
                level = (int) Double.parseDouble(arr[5]);
            }

            Theme theme = Theme.builder()
                    .areaType(AreaType.nameOf(arr[0]))
                    .detailArea(arr[1])
                    .shopName(arr[2])
                    .themeName(arr[3])
                    .shopUrl(arr[4])
                    .level(level)
                    .playTime(Integer.parseInt(arr[6]))
                    .openStatus(arr[7].equals("TRUE")?true:false)
                    .story(arr[8].replaceAll("<br>",System.getProperty("line.separator")))
                    .filePath("") //TODO - 임시로 빈문자열 입력
                    .build();

            themeList.add(theme);
        }
        themeRepository.saveAll(themeList);
    }

    @Transactional
    public void saveThemeInfo(ThemeBasicDto themeDto){
        themeRepository.save(themeDto.toEntity());
    }

    public List<Theme> getAllThemeEntities(){
        return themeRepository.findAll();
    }

    public List<QThemeDto> getAllThemeEntities(Member member){
        return themeRepository.findAllTheme(member);
    }

    public List<Theme> searchThemeKeyword(String keyword){
        List<Theme> entities = themeRepository.searchThemeKeyword(keyword);
        return entities;
    }

    public List<Theme> searchThemeEntities(Map<String, Object> searchForm){
        String keyword = searchForm.get("keyword").toString();
        String areaName = searchForm.get("area").toString();
        AreaType areaType = null;
        String detailArea = "";
        Boolean closeExclude = searchForm.get("closeExclude").equals("true");

        if (AreaType.nameOf(areaName) != null){
            areaType = AreaType.nameOf(areaName);
        }else if(!areaName.equals("전체")){
            detailArea = areaName;
        }

        List<Theme> entities = themeRepository.searchTheme(keyword, areaType, detailArea, closeExclude);

        return entities;
    }

    public List<QThemeDto> searchThemeEntities(Member member, Map<String, Object> searchForm){
        String keyword = searchForm.get("keyword").toString();
        String areaName = searchForm.get("area").toString();
        AreaType areaType = null;
        String detailArea = "";
        List<QThemeDto> entities;
        Boolean closeExclude = searchForm.get("closeExclude").equals("true");
        Boolean stampExclude = searchForm.get("stampExclude").equals("true");

        if (AreaType.nameOf(areaName) != null){
            areaType = AreaType.nameOf(areaName);
        }else if(!areaName.equals("전체")){
            detailArea = areaName;
        }

        if (stampExclude){
            entities = themeRepository.searchThemeStampExclude(member, keyword, areaType, detailArea, closeExclude);

        }else{
            entities = themeRepository.searchTheme(member, keyword, areaType, detailArea, closeExclude);
        }
        return entities;
    }

    public List<ThemeInfoDto> getThemeList(List<Theme> entities) {
        List<ThemeInfoDto> themeList = new ArrayList<>();

        for (Theme theme : entities){
            ThemeInfoDto themeInfoDto = ThemeInfoDto.builder()
                    .no(theme.getNo())
                    .themeName(theme.getThemeName())
                    .shopName(theme.getShopName())
                    .imageUrl("https://"+ AmazonS3Service.domainName +"/"+theme.getFilePath())
                    .openStatus(theme.getOpenStatus())
                    .build();

            themeList.add(themeInfoDto);
        }
        Collections.shuffle(themeList);
        return themeList;
    }

    public List<ThemeInfoDto> getQThemeList(List<QThemeDto> entities) {
        List<ThemeInfoDto> themeList = new ArrayList<>();
        for (QThemeDto theme : entities){
            ThemeInfoDto themeInfoDto = ThemeInfoDto.builder()
                    .no(theme.getNo())
                    .themeName(theme.getThemeName())
                    .shopName(theme.getShopName())
                    .imageUrl("https://"+ AmazonS3Service.domainName +"/"+theme.getFilePath())
                    .openStatus(theme.getOpenStatus())
                    .success(theme.getSuccess())
                    .build();

            themeList.add(themeInfoDto);
        }
        Collections.shuffle(themeList);
        return themeList;
    }

    public ThemeInfoDto getThemeInfo(Long no){
        Optional<Theme> optionalTheme = themeRepository.findById(no);
        if (optionalTheme.isEmpty()){
            throw new IllegalArgumentException("wrong theme no");
        }
        int totalRatingNum = libraryRepository.countRecodeByThemeNo(no);
        Theme theme = optionalTheme.get();
        String imageUrl = "https://"+AmazonS3Service.domainName +"/"+theme.getFilePath();

        return ThemeInfoDto.builder()
                .no(theme.getNo())
                .themeName(theme.getThemeName())
                .shopName(theme.getShopName())
                .imageUrl(imageUrl)
                .openStatus(theme.getOpenStatus())
                .playTime(theme.getPlayTime())
                .level(theme.getLevel())
                .shopUrl(theme.getShopUrl())
                .story(theme.getStory())
                .totalRatingNum(totalRatingNum)
                .ratingMap(getThemeRatingCnt(no))
                .build();

    }

    public Map<Integer, Integer> getThemeRatingCnt(Long themeNo){
        Map<Integer, Integer> map = new HashMap<>();
        for (int i=1; i<=5; i++){
            map.put(i, libraryRepository.countRecodeRating(themeNo, i));
        }
        return map;
    }


}
