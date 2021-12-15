package com.recoders.escapelog.service;

import com.recoders.escapelog.domain.Recode;
import com.recoders.escapelog.dto.RecodeDto;
import com.recoders.escapelog.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;


    public void saveRecode(RecodeDto recodeDto){
        Recode recode = Recode.builder()
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



}
