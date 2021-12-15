package com.recoders.escapelog.service;

import com.recoders.escapelog.domain.Feedback;
import com.recoders.escapelog.domain.FeedbackType;
import com.recoders.escapelog.domain.Theme;
import com.recoders.escapelog.dto.FeedbackDto;
import com.recoders.escapelog.repository.FeedbackRepository;
import com.recoders.escapelog.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ThemeRepository themeRepository;

    @Transactional
    public void saveNewThemeFeedback(FeedbackDto feedbackForm){
        Feedback feedback = Feedback.builder()
                .feedbackType(FeedbackType.OPEN)
                .areaType(feedbackForm.getAreaType())
                .newThemeName(feedbackForm.getNewThemeName())
                .contents(feedbackForm.getContents())
                .build();
        feedbackRepository.save(feedback);
    }

    @Transactional
    public void saveThemeInfoFeedback(FeedbackDto feedbackForm){

        Optional<Theme> optionalTheme = themeRepository.findById(feedbackForm.getThemeNo());
        if (optionalTheme.isEmpty()){
            throw new IllegalArgumentException("wrong theme no");
        }

        Feedback feedback = Feedback.builder()
                .feedbackType(feedbackForm.getFeedbackType())
                .theme(optionalTheme.get())
                .contents(feedbackForm.getContents())
                .build();
        feedbackRepository.save(feedback);
    }

}
