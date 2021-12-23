package com.recoders.escapelog.service;

import com.recoders.escapelog.domain.AreaType;
import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.domain.Theme;
import com.recoders.escapelog.dto.ThemeDto;
import com.recoders.escapelog.dto.ThemeRecodeDto;
import com.recoders.escapelog.repository.LibraryRepository;
import com.recoders.escapelog.repository.ThemeRepository;
import com.recoders.escapelog.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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
            String[] arr = s.replaceAll("^\"|\"$", "").split("\\|");
            int level = -1;
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
                    .build();

            themeList.add(theme);
        }
        themeRepository.saveAll(themeList);
    }

    public List<Theme> getAllThemeEntities(){
        return themeRepository.findAll();
    }

    public List<Theme> searchThemeKeyword(String keyword){
        List<Theme> themeList = themeRepository.searchThemeKeyword(keyword);
        return themeList;
    }

    public List<Theme> searchTheme(Map<String, Object> searchForm){
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

        List<Theme> themeList = themeRepository.searchTheme(keyword, areaType, detailArea, closeExclude);

        return themeList;
    }

    public List<ThemeRecodeDto> searchTheme(Member member, Map<String, Object> searchForm){
        String keyword = searchForm.get("keyword").toString();
        String areaName = searchForm.get("area").toString();
        AreaType areaType = null;
        String detailArea = "";
        List<ThemeRecodeDto> themeList;
        Boolean closeExclude = searchForm.get("closeExclude").equals("true");
        Boolean stampExclude = searchForm.get("stampExclude").equals("true");

        if (AreaType.nameOf(areaName) != null){
            areaType = AreaType.nameOf(areaName);
        }else if(!areaName.equals("전체")){
            detailArea = areaName;
        }

        if (stampExclude){
            themeList = themeRepository.searchThemeStampExclude(member, keyword, areaType, detailArea, closeExclude);

        }else{
            themeList = themeRepository.searchTheme(member, keyword, areaType, detailArea, closeExclude);
        }
        Collections.shuffle(themeList);
        return themeList;
    }

    public List<ThemeRecodeDto> getAllThemeList(Member member){
        List<ThemeRecodeDto> themeList = themeRepository.findAllTheme(member);
        Collections.shuffle(themeList);
        return themeList;
    }

    public List<ThemeDto> getThemeList(List<Theme> entities) {
        List<ThemeDto> themeList = new ArrayList<>();

        for (Theme themes : entities){
            Theme theme = Theme.builder()
                    .no(themes.getNo())
                    .themeName(themes.getThemeName())
                    .shopName(themes.getShopName())
                    .imageUrl(themes.getImageUrl())
                    .openStatus(themes.getOpenStatus())
                    .build();
            themeList.add(ThemeDto.simpleForm(theme));
        }
        Collections.shuffle(themeList);
        return themeList;
    }

    public ThemeDto getThemeInfo(Long no){
        Optional<Theme> optionalTheme = themeRepository.findById(no);
        if (optionalTheme.isEmpty()){
            throw new IllegalArgumentException("wrong theme no");
        }
        int totalRatingNum = libraryRepository.countRecodeByThemeNo(no);

        return ThemeDto.detailForm(optionalTheme.get(),totalRatingNum,getThemeRatingCnt(no));
    }

    public Map<Integer, Integer> getThemeRatingCnt(Long themeNo){
        Map<Integer, Integer> map = new HashMap<>();
        for (int i=1; i<=5; i++){
            map.put(i, libraryRepository.countRecodeRating(themeNo, i));
        }
        return map;
    }


}
