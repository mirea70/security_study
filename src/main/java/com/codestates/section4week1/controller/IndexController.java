package com.codestates.section4week1.controller;

import com.codestates.section4week1.config.auth.PrincipalDetails;
import com.codestates.section4week1.model.Member;
import com.codestates.section4week1.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/")
    public @ResponseBody String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println(principalDetails.getMember());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/join")
    public String join() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(Member member) {
        member.setRole("ROLE_USER");
        String rawPassword = member.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        member.setPassword(encPassword);
        memberRepository.save(member);
        return "redirect:/login";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "info";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "data";
    }

    @GetMapping("loginTest")
    public @ResponseBody String loginTest(Authentication authentication) {
        System.out.println("================/loginTest=============");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication = " + principalDetails.getMember());
        return "세션 정보 확인";
    }
    @GetMapping("/loginTest2")
    public @ResponseBody String loginTest2(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("================/loginTest2=============");
        System.out.println("userDetails :" + principalDetails.getMember());
        return "세션 정보 확인2";
    }

    @GetMapping("/loginTest3")
    public @ResponseBody String loginOAuthTest(Authentication authentication,
                                               @AuthenticationPrincipal OAuth2User oauth,
                                                @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("================/loginOAuthTest=============");
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : " + oAuth2User.getAttributes());
        System.out.println("oauth2User : " + oauth.getAttributes());
        System.out.println("principalDetails = " + principalDetails.getAttributes());;
        return "세션 정보 확인3";
    }
}
