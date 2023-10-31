package com.ssafy.journeymate.comment.conrtroller;


import com.ssafy.journeymate.comment.model.Mate;
import com.ssafy.journeymate.comment.model.LoginInfo;
import com.ssafy.journeymate.comment.repository.MateRepository;
import com.ssafy.journeymate.comment.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
//@RequestMapping("/comment-service")
public class MateController {

    private final MateRepository mateRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/mate")
    public String mates() {
        return "/comment-service/mate";
    }

    @GetMapping("/mates")
    @ResponseBody
    public List<Mate> mate() {
        List<Mate> mates = mateRepository.findAllMate();
        mates.stream().forEach(mate -> mate.setUserCount(mateRepository.getUserCount(mate.getMateId())));
        return mates;
    }

    @PostMapping("/mate")
    @ResponseBody
    public Mate createMate(@RequestParam String name) {
        return mateRepository.createMate(name);
    }

    @GetMapping("/mate/enter/{mateId}")
    public String mateDetail(Model model, @PathVariable String mateId) {
        model.addAttribute("mateId", mateId);
        return "/comment-service/matedetail";
    }

    @GetMapping("/mate/{mateId}")
    @ResponseBody
    public Mate mateInfo(@PathVariable String mateId) {
        return mateRepository.findMateById(mateId);
    }

    @GetMapping("/user")
    @ResponseBody
    public LoginInfo getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return LoginInfo.builder().name(name).token(jwtTokenProvider.generateToken(name)).build();
    }
}