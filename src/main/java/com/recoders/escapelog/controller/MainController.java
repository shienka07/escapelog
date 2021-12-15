package com.recoders.escapelog.controller;

import com.recoders.escapelog.dto.RecodeDto;
import com.recoders.escapelog.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RequiredArgsConstructor
@Controller
public class MainController {

    private final LibraryService libraryService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    //책장 목록
    @GetMapping("/library")
    public String library() {
        return null;
    }


    //글쓰기 페이지
    @GetMapping("/recode")
    public String recode(Model model){
        model.addAttribute("recodeDto", new RecodeDto());
        return "library/library_write";
    }

    //글쓰기 보내기
    @PostMapping("/recode")
    public String write(RecodeDto recodeDto){
        libraryService.saveRecode(recodeDto);
        return "redirect:/library";
    }


    //글 읽기 페이지
    @GetMapping("/read/{no}")
    public String read(){
        return null;
    }

    //글 수정
    @PostMapping("/read/{no}")
    public String modify(){
        return null;
    }

    //글 삭제하기
    @PostMapping("/delete/{no}")
    public String delete(){
        return null;
    }

    @PostMapping("/library/search")
    public String search(){
        return null;
    }
}
