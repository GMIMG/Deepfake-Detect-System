package com.gmimg.multicampus.springboot;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.gmimg.multicampus.springboot.member.Member;
import com.gmimg.multicampus.springboot.service.MemService;

@Controller
@SessionAttributes("sessionMem")
public class MemberController {

	@Autowired
	MemService service; 
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	//@Resource(name = "C:/Users/JC Lee/Desktop/NEW")
	String uploadPath = "C:/Users/JC Lee/Desktop/NEW";
	
	//회원가입
	@RequestMapping(value = "/member/registerForm", method = RequestMethod.GET)
	public String registerForm() {
		return "register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView register(ModelAndView mav, Member member) throws Exception {
		
		System.out.println("1차 확인 : " + member);
		System.out.println("1차 확인 : " + member.getMemId());
				
		String inputPass = member.getMemPw();
		
		System.out.println("2차확인 : "+ inputPass);
		String pass = passwordEncoder.encode(inputPass);
		member.setMemPw(pass);
		
		System.out.println(member);
		
		service.insertMem(member);
		mav.setViewName("redirect:/");
		
		return mav;
	}
	
	//아이디 중복확인 
	@ResponseBody
	@RequestMapping(value = "/idChk", method = {RequestMethod.POST,RequestMethod.GET})
	public int postIdCheck(Member member) throws Exception {

		Member check = service.idCheckMem(member);
		
		int result = 0;
		
		if (check != null) {
			result = 1;
		} 
		return result;
	}

	//로그인 
	@RequestMapping(value = "/member/loginForm")
	public String loginForm() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(ModelAndView mav, Model model,
			Member member, HttpServletRequest request) throws Exception {
		
		HttpSession session = request.getSession();
		Member mem = service.findMem(member);
		
		boolean passMatch = passwordEncoder.matches(member.getMemPw(), mem.getMemPw());
		System.out.println(passMatch);
		if (mem != null && passMatch) {
			session.setAttribute("sessionMem", mem); 
			mav.setViewName("redirect:/");			
		} else {
			session.setAttribute("sessionMem", null); 
			mav.setViewName("redirect:/member/loginForm");
		}
		return mav;
	}

	@RequestMapping(value = "/member/logOut")
	public String logOut(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/loading")
	public String loading() {
		return "loading";
	}
	
	
}
