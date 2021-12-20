package com.recoders.escapelog.controller;

import com.google.gson.JsonObject;
import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.dto.ChangePwDto;
import com.recoders.escapelog.dto.FindPwDto;
import com.recoders.escapelog.dto.MemberDto;
import com.recoders.escapelog.dto.SignupDto;
import com.recoders.escapelog.security.CurrentMember;
import com.recoders.escapelog.service.EmailService;
import com.recoders.escapelog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.recoders.escapelog.domain.Recode;
import com.recoders.escapelog.dto.RecodeDto;
import com.recoders.escapelog.service.LibraryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.recoders.escapelog.dto.FeedbackDto;
import com.recoders.escapelog.dto.ThemeDto;
import com.recoders.escapelog.service.FeedbackService;
import com.recoders.escapelog.service.ThemeService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class MainController {



    private final MemberService memberService;
    private final EmailService emailService;
    private final LibraryService libraryService;

    private final ThemeService themeService;
    private final FeedbackService feedbackService;

    @GetMapping("/")
    public String index(){
        return "index";
    }
    
    //TODO - 임시 테마정보 삽입
    @GetMapping("/insert_theme")
    public String insertTheme() {
        try {
            themeService.saveCsvThemeInfo();
        }catch (IOException e){
            return "index";
        }
        return "redirect:/themes";
    }

    @GetMapping("/signup")
    public String signUpForm(Model model){
        model.addAttribute("signupForm", new SignupDto());
        return "member/signup";
    }

    @ResponseBody
    @PostMapping("/check_nickname")
    public String checkNicknameDuplicate(String nickname){
        JsonObject object = new JsonObject();
        try {
            memberService.checkNicknameDuplicate(nickname);
            object.addProperty("duplicateResult", true);
        }catch (IllegalArgumentException e){
            object.addProperty("duplicateResult", false);
        }
        return object.toString();
    }


    @PostMapping("/signup")
    public String signUpSubmit(@Validated @ModelAttribute("signupForm")SignupDto signupForm, BindingResult result){

        if (result.hasErrors()){
            return "member/signup";
        }

        if (!signupForm.getPassword().equals(signupForm.getRePassword())){
            FieldError fieldError = new FieldError("signupForm", "rePassword","비밀번호가 일치하지 않습니다.");
            result.addError(fieldError);
            return "member/signup";
        }

        try {
            memberService.checkEmailDuplicate(signupForm.getEmail());
            memberService.processNewUser(signupForm);

        }catch (IllegalArgumentException e){
            FieldError fieldError = new FieldError("signupForm", "email","이미 존재하는 이메일입니다.");
            result.addError(fieldError);
            return "member/signup";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "errorMsg", required = false) String msg,
                        Model model){
        if (error != null && error.equals("true")){
            model.addAttribute("error",error);
            model.addAttribute("errorMsg", msg);
        }
        return "member/login";
    }

    @GetMapping("/find_pw")
    public String findPw(Model model){
        model.addAttribute("findForm", new FindPwDto());
        return "member/find_pw";
    }

    @ResponseBody
    @PostMapping("/check_email")
    public String checkEmailExistence(String email){

        String msg = "";
        JsonObject object = new JsonObject();

        try {
            boolean result = memberService.checkEmailExistence(email).isEmailVerified();
            if (!result){
                msg = "이메일 인증을 하지 않은 계정입니다. 이메일 인증을 원할 시 관리자에게 문의하세요.";
            }
            object.addProperty("verifiedResult", result);
            object.addProperty("existenceResult", true);
        }catch (IllegalArgumentException e){
            msg = "계정을 찾을 수 없습니다.";
            object.addProperty("existenceResult", false);
        }
        object.addProperty("msg",msg);
        return object.toString();
    }

    @ResponseBody
    @PostMapping("/send_check_token_email")
    public String sendCheckTokenEmail(@CurrentMember Member member){
        JsonObject object = new JsonObject();
        try {
            emailService.sendCheckEmail(member);
            object.addProperty("result",true);
        }catch (IllegalArgumentException e){
            object.addProperty("result",false);
        }
        return object.toString();
    }

    @GetMapping("/email_check_token")
    public String emailCheckToken(String token, String email, Model model){

        try {
            memberService.checkEmailToken(email, token);
        }catch (IllegalArgumentException | NullPointerException e){
            model.addAttribute("error", "wrong");
        }

        return "member/email_result";
    }

    @ResponseBody
    @PostMapping("/send_code_email")
    public String sendCodeEmail(String email){
        JsonObject object = new JsonObject();
        try {
            emailService.sendAuthenticationCodeMail(memberService.checkEmailExistence(email));
            object.addProperty("result",true);
        }catch (IllegalArgumentException e){
            object.addProperty("result",false);
        }
        return object.toString();
    }

    @ResponseBody
    @PostMapping("/check_code")
    public String checkAuthenticationCode(String email, String code){
        JsonObject object = new JsonObject();
        try {
            boolean result = memberService.checkAuthenticationCode(email,code);
            object.addProperty("result",result);
        }catch (IllegalArgumentException | NullPointerException e){
            object.addProperty("result",false);
        }

        return object.toString();
    }

    @PostMapping("/find_change_pw")
    public String changPw(@Validated  @ModelAttribute("findForm")FindPwDto findForm, BindingResult result){

        if (result.hasErrors()){
            return "member/find_pw";
        }

        if (!findForm.getNewPassword().equals(findForm.getRePassword())){
            FieldError fieldError = new FieldError("findForm", "rePassword","비밀번호가 일치하지 않습니다.");
            result.addError(fieldError);
            return "member/find_pw";
        }

        try {
            memberService.changeUserPassword(findForm);
        }catch (IllegalArgumentException e){
            return "member/find_pw";
        }

        return "member/login";
    }

    @GetMapping("/user/mypage")
    public String myPageMain(@CurrentMember Member member,Model model){
        model.addAttribute("currentMember", MemberDto.memberMyPageInfo(member));
        model.addAttribute("changeForm",new ChangePwDto());
        return "member/mypage";
    }

    @ResponseBody
    @PostMapping("/change_nickname")
    public String changeNickname(@CurrentMember Member member, String nickname){

        JsonObject object = new JsonObject();
        try {
            memberService.checkNicknameDuplicate(nickname);
            memberService.changeUserNickname(member, nickname);
            object.addProperty("duplicateResult", true);
        }catch (IllegalArgumentException e){
            object.addProperty("duplicateResult", false);
        }
        return object.toString();
    }

    @ResponseBody
    @PostMapping("/change_pw")
    public String changUserPw(@CurrentMember Member member, @Validated  @ModelAttribute("changeForm")ChangePwDto changeForm, BindingResult result,Model model) {

        JsonObject object = new JsonObject();

        if (result.hasErrors() || !(changeForm.getNewPassword().equals(changeForm.getRePassword()))) {
            object.addProperty("changePwResult", false);
            return object.toString();
        }

        if (!memberService.changeUserPassword(member, changeForm)) {
            object.addProperty("changePwResult", false);
        } else {
            object.addProperty("changePwResult", true);
        }
        return object.toString();
    }

    //책장 목록
    @GetMapping("/library")
    public String library(Model model, @CurrentMember Member member){
        if(member.getLibraryName()==null){
            return "library/library_name";
        }

        return memberLibrary(member.getLibraryName(), model, member);

    }


    @GetMapping("/library/{libraryName}")
    public String memberLibrary(@PathVariable String libraryName, Model model, @CurrentMember Member member) {

        List<Recode> MemberRecodeList = libraryService.getMemberRecodeList(libraryName);
        model.addAttribute("recodeList",MemberRecodeList);
        model.addAttribute("member", member);
        return "library/library_list";
    }

    @PostMapping("/setLibrary")
    public String saveLibraryName(@RequestParam(value = "libraryName") String libraryName, Model model, @CurrentMember Member member){
        memberService.saveLibraryName(member, libraryName);
        return "redirect:/";
    }




    //글쓰기 페이지
    @GetMapping("/recode")
    public String recode(Model model, @CurrentMember Member member){
        model.addAttribute("recodeDto", new RecodeDto());
        model.addAttribute("member", member);
        model.addAttribute("feedbackForm", new FeedbackDto());
        return "library/library_write";
    }

    //글쓰기 보내기
    @PostMapping("/recode")
    public String write(RecodeDto recodeDto, @CurrentMember Member member){
        libraryService.saveRecode(member, recodeDto);
        return "redirect:/library";
    }


    //글 읽기 페이지
    @GetMapping("/read/{no}")
    public String read(@PathVariable Long no, Model model, @CurrentMember Member member){

        Recode readRecode = libraryService.getRecode(no);
        model.addAttribute("recode", readRecode);
        model.addAttribute("member", member);

        return "library/library_detail";
    }

    //글 수정
    @PostMapping("/read/{no}")
    public String modify(){
        return null;
    }

    //글 삭제하기
    @PostMapping("/delete/{no}")
    public String delete(@PathVariable Long no, Model model){

        libraryService.deleteRecode(no);
        model.addAttribute("recodeResult", true);

        return "redirect:/library";
    }

    @GetMapping("/library_search")
    public String search(Model model, @CurrentMember Member member, @RequestParam(value = "keyword") String keyword){

        List<Recode> searchRecodeList = libraryService.searchRecode(keyword);
        model.addAttribute("member", memberService.getMember(member));
        model.addAttribute("searchRecodeList",searchRecodeList);

        return "redirect:/library";
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

    @GetMapping("/recode/theme_search")
    public String themeSearchModal(@RequestParam String keyword,Model model){
        model.addAttribute("themeList",themeService.getThemeList(themeService.searchThemeKeyword(keyword)));
        return "library/library_write :: #theme-list";
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
