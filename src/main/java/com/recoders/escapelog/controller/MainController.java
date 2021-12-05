package com.recoders.escapelog.controller;

import com.recoders.escapelog.dto.FeedbackDto;
import com.recoders.escapelog.dto.ThemeDto;
import com.recoders.escapelog.service.FeedbackService;
import com.recoders.escapelog.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final ThemeService themeService;
    private final FeedbackService feedbackService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/themes")
    public String themeList(Model model){
        List<ThemeDto> themeList = themeService.getThemeList(themeService.getAllThemeEntities());
        model.addAttribute("themeList",themeList);
        model.addAttribute("feedbackForm", new FeedbackDto());
        return "theme/theme_list";
    }

    @GetMapping("/themes/{no}")
    public String themeDetail(@PathVariable Long no, Model model){

        try{
            model.addAttribute("themeInfo", themeService.getThemeInfo(no));
            model.addAttribute("nlString",System.getProperty("line.separator"));
            model.addAttribute("feedbackForm", new FeedbackDto());
        }catch (IllegalArgumentException e){
            return "redirect:/themes";
        }
        return "theme/theme_detail";
    }

    @GetMapping("/theme_search")
    public String themeSearch(@RequestParam Map<String, Object> searchForm, Model model){

        model.addAttribute("themeList",themeService.getThemeList(themeService.searchTheme(searchForm)));
        return "theme/theme_list :: #theme-list";
    }

    @ResponseBody
    @PostMapping("/feedback/add")
    public String feedBackNewTheme(FeedbackDto feedbackForm){
        feedbackService.saveNewThemeFeedback(feedbackForm);
        return "theme/theme_list";
    }

    @ResponseBody
    @PostMapping("/feedback/info")
    public String feedBackThemeInfo(FeedbackDto feedbackForm){
        try{
            feedbackService.saveThemeInfoFeedback(feedbackForm);
        }catch (IllegalArgumentException e){
            return "redirect:/themes";
        }
        return "theme/theme_detail";
    }

}
