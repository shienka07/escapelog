package com.recoders.escapelog.controller;

import com.google.gson.JsonObject;
import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.dto.*;
import com.recoders.escapelog.security.CurrentMember;
import com.recoders.escapelog.service.*;
import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.recoders.escapelog.domain.Recode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

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
    private final MapService mapService;

    private final AmazonS3Service amazonS3Service;

    @GetMapping("/")
    public String index(@CurrentMember Member member, Model model){
        if (member != null){
            model.addAttribute("currentMember", MemberDto.memberBasicInfo(member));
        }

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

    @GetMapping("/admin/theme/add")
    public String addThemeForm(@CurrentMember Member member, Model model){
        model.addAttribute("themeForm",new ThemeBasicDto());
        model.addAttribute("currentMember", member);
        return "admin/theme_add";
    }

    @PostMapping("/add_theme")
    public String addTheme(@Validated @ModelAttribute("themeForm")ThemeBasicDto themeDto, BindingResult result, MultipartFile file){

        if (result.hasErrors()){
            return "admin/theme_add";
        }

        try {
            if (file != null && !file.isEmpty()){
                String filePath = amazonS3Service.getThemeImgFilePath(themeDto.getAreaType().name().toLowerCase(),file.getOriginalFilename());
                themeDto.setImagePath(amazonS3Service.upload(file,filePath));
            }
            themeService.saveThemeInfo(themeDto);
        }catch (IOException e){
            return "admin/theme_add";
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
        boolean duplicateResult = memberService.checkNicknameDuplicate(nickname);
        object.addProperty("duplicateResult", duplicateResult);

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

        if (!memberService.checkNicknameDuplicate(signupForm.getNickname())){
            FieldError fieldError = new FieldError("signupForm", "nickname","이미 존재하는 닉네임입니다.");
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
        boolean duplicateResult = memberService.checkNicknameDuplicate(nickname);
        if (duplicateResult){
            memberService.changeUserNickname(member, nickname);
        }
        object.addProperty("duplicateResult", duplicateResult);
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

    //책장
    @GetMapping("/library")
    public String library(Model model, @CurrentMember Member member,
                          @PageableDefault(page=0, size=9, sort="no", direction = Sort.Direction.DESC)Pageable pageable,
                          String keyword){

        if(member.getLibraryName()==null){
            return "library/library_name";
        }

        return memberLibrary(member.getLibraryName(), model, member, pageable, keyword);

    }

    @ResponseBody
    @PostMapping("/check_libraryName")
    public String checkLibraryName(String libraryName) {

        JsonObject object = new JsonObject();

        try {
            memberService.checkLibraryName(libraryName);
            object.addProperty("result", true);
        } catch (IllegalArgumentException e) {
            object.addProperty("result", false);
        }

        return object.toString();

    }




    @GetMapping("/library/{libraryName}")
    public String memberLibrary(@PathVariable String libraryName, Model model, @CurrentMember Member member,
                                @PageableDefault(page=0, size=9, sort="no", direction = Sort.Direction.DESC)Pageable pageable,
                                String keyword) {

        Page<Recode> memberRecodeList = null;

        if(keyword == null) {
            memberRecodeList = libraryService.getMemberRecodeList(libraryName, pageable);
        } else {
            memberRecodeList = libraryService.searchRecode(libraryName, keyword, pageable);
        }

        int nowPage = memberRecodeList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage -2, 1);
        int endPage = Math.min(nowPage+2, memberRecodeList.getTotalPages());


        model.addAttribute("libraryMember", memberService.getLibraryMember(libraryName));
        model.addAttribute("recodeList",libraryService.getRecodeInfoDtoList(memberRecodeList));
        model.addAttribute("currentMember", MemberDto.memberBasicInfo(memberService.getMember(member)));
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "library/library_list";
    }

    @PostMapping("/setLibrary")
    public String saveLibraryName(@RequestParam(value = "libraryName") String libraryName, @CurrentMember Member member){
        memberService.saveLibraryName(member, libraryName);
        return "redirect:/library";
    }


    //글쓰기 페이지
    @GetMapping("/recode")
    public String recode(Model model, @CurrentMember Member member){
        model.addAttribute("recodeDto", new RecodeDto());
        model.addAttribute("member", memberService.getMember(member));
        model.addAttribute("feedbackForm", new FeedbackDto());
        return "library/library_write";
    }

    //글쓰기 보내기
    @PostMapping("/recode")
    public String write(RecodeDto recodeDto, Model model, MultipartFile file, @CurrentMember Member member){

        try {
            if (file != null && !file.isEmpty()){
                recodeDto.setImagePath(amazonS3Service.uploadRecodeImg(file));
            }
            libraryService.saveRecode(member, recodeDto);
        }catch (IOException e){
            return "library/library_write";
        }

        memberService.updateCount(member);
        memberService.updateGrade(member);
        memberService.updateSuccess(member);


        model.addAttribute("recodeResult", true);
        return "redirect:/library";
    }


    //글 읽기 페이지
    @GetMapping("/read/{no}")
    public String read(@PathVariable Long no, Model model, @CurrentMember Member member){

        model.addAttribute("recode", libraryService.getReadInfoDto(libraryService.getRecode(no)));
        model.addAttribute("currentMember", MemberDto.memberBasicInfo(memberService.getMember(member)));

        return "library/library_detail";
    }

    //글 수정
    @GetMapping("/edit/{no}")
    public String edit(@PathVariable Long no, Model model, @CurrentMember Member member){
        model.addAttribute("editRecode", libraryService.getEditInfoDto(libraryService.getRecode(no)));
        model.addAttribute("currentMember", MemberDto.memberBasicInfo(memberService.getMember(member)));
        model.addAttribute("editDto", new EditDto());
        model.addAttribute("feedbackForm", new FeedbackDto());
        return "library/library_edit";
    }

    @PostMapping("/modify/{no}")
    public String modify(@PathVariable Long no, EditDto editDto,MultipartFile file, @CurrentMember Member member){

        String imagePath = libraryService.getRecode(no).getImagePath();
        boolean imageChanges = editDto.getImageChanges();

        try {

            if (imagePath != null && imageChanges){
                if (file != null && !file.isEmpty()){
                    editDto.setImagePath(amazonS3Service.uploadRecodeImg(file));
                }
                amazonS3Service.delete(imagePath);
            }else if (imageChanges && file != null && !file.isEmpty()){
                editDto.setImagePath(amazonS3Service.uploadRecodeImg(file));
            }

            libraryService.updateRecode(no, member, editDto);
        }catch (IOException e){
            return "library/library_edit";
        }


        return "redirect:/library";
    }


    //글 삭제하기
    @PostMapping("/delete/{no}")
    public String delete(@PathVariable Long no, Model model, @CurrentMember Member member){

        libraryService.delete(no);
        model.addAttribute("deleteResult", true);

        memberService.updateCount(member);
        memberService.updateGrade(member);
        memberService.updateSuccess(member);

        return "redirect:/library";
    }


    @GetMapping("/themes")
    public String themeList(@CurrentMember Member member, Model model){

        if (member!=null){
            List<ThemeInfoDto> themeList = themeService.getQThemeList(themeService.getAllThemeEntities(member));
            model.addAttribute("themeList", themeList);
            model.addAttribute("checkU",true);
        }else {
            List<ThemeInfoDto> themeList = themeService.getThemeList(themeService.getAllThemeEntities());
            model.addAttribute("themeList",themeList);
            model.addAttribute("checkU",false);
        }
        model.addAttribute("feedbackForm", new FeedbackDto());
        return "theme/theme_list";
    }

    @GetMapping("/themes/{no}")
    public String themeDetail(@PathVariable Long no, @CurrentMember Member member, Model model){

        try{
            model.addAttribute("themeInfo", themeService.getThemeInfo(no));
            model.addAttribute("nlString",System.getProperty("line.separator"));
            model.addAttribute("feedbackForm", new FeedbackDto());
            model.addAttribute("themeReviewList", libraryService.getReviewList(libraryService.getAllReviewEntities(no)));
            if(member != null){
                model.addAttribute("currentMember",MemberDto.memberBasicInfo(member));
            }
        }catch (IllegalArgumentException e){
            return "redirect:/themes";
        }
        return "theme/theme_detail";
    }

    @GetMapping("/review_filter")
    public String themeReviewFilter(Long themeNo, Integer rating, Model model){
        if (rating == 0){
            model.addAttribute("themeReviewList", libraryService.getReviewList(libraryService.getAllReviewEntities(themeNo)));
        }else{
            model.addAttribute("themeReviewList", libraryService.getReviewList(libraryService.getReviewFilterEntities(themeNo,rating)));
        }

        return "theme/theme_detail :: #theme-review-list";
    }

    @ResponseBody
    @GetMapping("/theme_search")
    public List<ThemeInfoDto> themeSearch(@CurrentMember Member member, @RequestParam Map<String, Object> searchForm){

        if (member!=null){
            return themeService.getQThemeList(themeService.searchThemeEntities(member,searchForm));
        }else{
            return themeService.getThemeList(themeService.searchThemeEntities(searchForm));
        }
    }

    @GetMapping("/recode/theme_search")
    public String themeSearchModal(@RequestParam String keyword,Model model){
        model.addAttribute("themeList",themeService.getThemeList(themeService.searchThemeKeyword(keyword)));
        return "library/library_write :: #theme-list";
    }


    @ResponseBody
    @PostMapping("/feedback/add")
    public String feedBackNewTheme(@CurrentMember Member member, FeedbackDto feedbackForm){
        feedbackService.saveNewThemeFeedback(member,feedbackForm);
        return "theme/theme_list";
    }

    @ResponseBody
    @PostMapping("/feedback/info")
    public String feedBackThemeInfo(@CurrentMember Member member, FeedbackDto feedbackForm){
        try{
            feedbackService.saveThemeInfoFeedback(member,feedbackForm);
        }catch (IllegalArgumentException e){
            return "redirect:/themes";
        }
        return "theme/theme_detail";
    }

    @GetMapping("/map")
    public String escapeMap(@CurrentMember Member member,Model model){
        model.addAttribute("stamp","1");
        model.addAttribute("mapInfo",mapService.getMapInfo(member));
        model.addAttribute("currentMember", MemberDto.memberBasicInfo(member));
        return "map/escape_map";
    }

}
