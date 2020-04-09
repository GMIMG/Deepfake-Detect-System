package com.gmimg.multicampus.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.gmimg.multicampus.springboot.mapper.IMemMapper;
import com.gmimg.multicampus.springboot.member.Member;

@Controller
@SessionAttributes("sessionMem")
public class MemberController {

	@Autowired
	IMemMapper memMapper;
	
	
	//회원가입
	@RequestMapping(value = "/member/registerForm", method = RequestMethod.GET)
	public String registerForm() {
		return "register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView register(ModelAndView mav,
			@RequestParam("regMemId") String RegId,
			@RequestParam("regMemPw") String RegPw) {
		
		memMapper.insertMem(RegId, RegPw);
		
		mav.addObject("regId", RegId);
		mav.addObject("regPw", RegPw);
		mav.setViewName("registerOk");
		
		//String map = memMapper.findAll("heee@email.com").getMemPw();
		
		return mav;
	}
	//로그인 
	@RequestMapping(value = "/member/loginForm")
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView login(ModelAndView mav, Model model,
			@RequestParam("loginMemId") String loginId,
			@RequestParam("loginMemPw") String loginPw) {
		
		Member member = memMapper.findMem(loginId, loginPw);
		
		if (member != null) {
			
			System.out.println(member.getMemId());
			System.out.println(member.getMemPw());
			
			model.addAttribute("sessionMem", member); 
			
		
			mav.setViewName("index");			
		} else {
			mav.setViewName("login");
		}
		return mav;
	}
}
