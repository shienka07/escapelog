package com.recoders.escapelog.controller;

import com.google.gson.JsonObject;
import com.recoders.escapelog.dto.FindPwDto;
import com.recoders.escapelog.dto.SignupDto;
import com.recoders.escapelog.service.EmailService;
import com.recoders.escapelog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final MemberService memberService;
    private final EmailService emailService;

    @GetMapping("/")
    public String index(){
        return "index";
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

        try {
            memberService.checkEmailDuplicate(signupForm.getEmail());
            memberService.processNewUser(signupForm);

        }catch (IllegalArgumentException e){
            FieldError fieldError = new FieldError("signupForm", "email","이미 존재하는 이메일입니다.");
            result.addError(fieldError);
            return "member/signup";
        }

        return "member/login";
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
        }catch (IllegalArgumentException e){
            object.addProperty("result",false);
        }

        return object.toString();
    }

    @PostMapping("/change_pw")
    public String changPw(@Validated  @ModelAttribute("findForm")FindPwDto findForm, BindingResult result){

        if (result.hasErrors()){
            return "member/find_pw";
        }

        try {
            memberService.changeUserPassword(findForm);
        }catch (IllegalArgumentException e){
            return "member/find_pw";
        }

        return "member/login";
    }
}
