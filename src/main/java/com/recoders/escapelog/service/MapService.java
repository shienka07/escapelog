package com.recoders.escapelog.service;

import com.recoders.escapelog.domain.AreaGroup;
import com.recoders.escapelog.domain.AreaType;
import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.dto.MapAreaDto;
import com.recoders.escapelog.dto.MapDto;
import com.recoders.escapelog.repository.LibraryRepository;
import com.recoders.escapelog.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {

    private final LibraryRepository libraryRepository;
    private final ThemeRepository themeRepository;


    public MapDto getMapInfo(Member member){
        Long memberNo = member.getNo();
        List<MapAreaDto> areaInfoList = new ArrayList<>();
        List<String> areaList;
        String[] areaArr;

        MapDto mapInfo = new MapDto(
                (int) themeRepository.count(),
                libraryRepository.countTotalVisitNum(memberNo),
                libraryRepository.countTotalSuccessNum(memberNo)
        );


        for (AreaGroup group : AreaGroup.values()){
            areaList = AreaType.getGroupList(group);
            areaArr = areaList.toArray(new String[areaList.size()]);

            areaInfoList.add(new MapAreaDto(
                    group,
                    sumAreaThemeNum(areaArr),
                    sumVisitNum(memberNo,areaArr),
                    sumSuccessNum(memberNo, areaArr)
            ));
        }

        mapInfo.setAreaInfoList(areaInfoList);

        return mapInfo;
    }

    public int sumAreaThemeNum(String[] areaArr){
        int sum = 0;
        for (String areaName : areaArr){
            sum += themeRepository.countByAreaType(AreaType.valueOf(areaName));
        }
        return sum;
    }

    public int sumVisitNum(Long no, String[] areaArr){
        int sum = 0;
        for (String areaName : areaArr){
            sum += libraryRepository.countVisitNum(no, areaName);
        }
        return sum;
    }

    public int sumSuccessNum(Long no, String[] areaArr){
        int sum = 0;
        for (String areaName : areaArr){
            sum += libraryRepository.countSuccessNum(no, areaName);
        }
        return sum;
    }

}
