package com.recoders.escapelog.service;

import com.recoders.escapelog.domain.AreaType;
import com.recoders.escapelog.domain.Theme;
import com.recoders.escapelog.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

//    @PostConstruct
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
}
