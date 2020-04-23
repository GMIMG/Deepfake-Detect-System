package com.gmimg.multicampus.springboot;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.gmimg.multicampus.springboot.mapper.IMemMapper;
import com.gmimg.multicampus.springboot.member.Member;
import com.gmimg.multicampus.springboot.util.UploadFileUtils;

@Controller
@SessionAttributes("sessionMem")
public class MemberController {

	@Autowired
	IMemMapper memMapper;
	
	//@Resource(name = "C:/Users/JC Lee/Desktop/NEW")
	String uploadPath = "C:/Users/JC Lee/Desktop/NEW";
	
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
		mav.setViewName("redirect:/");
		
		return mav;
	}
	
	//아이디 중복확인 
	@ResponseBody
	@RequestMapping(value = "/idChk", method = {RequestMethod.POST,RequestMethod.GET})
	public int postIdCheck(@RequestParam("regMemId") String userId) {

		Member check = memMapper.idCheckMem(userId);
		
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
			@RequestParam("loginMemId") String loginId,
			@RequestParam("loginMemPw") String loginPw) {
		
		Member member = memMapper.findMem(loginId, loginPw);
		
		if (member != null) {
			
			model.addAttribute("sessionMem", member); 
			mav.setViewName("redirect:/");			
		} else {
			mav.setViewName("redirect:/member/loginForm");
		}
		return mav;
	}

	@RequestMapping(value = "/member/logOut")
	public String logOut(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		
		return "redirect:/";
	}

	@RequestMapping(value = "/member/myPage")
	public String myPageForm(HttpSession session) {
		Object id = session.getAttribute("sessionMem");
		System.out.println(id);
		return "mypagetest";
	}
	
	@RequestMapping(value = "/member/myPage/register")
	public String myRegister (Member member, MultipartFile file) throws Exception {
		
		String imgUploadPath = uploadPath + File.separator + "imgUpload";
		String ymdPath = UploadFileUtils.calcPath(imgUploadPath);
		String fileName = null;
		
		System.out.println("1");
		
		if (file != null ) {
			fileName = UploadFileUtils.fileUpload(imgUploadPath, file.getOriginalFilename(), file.getBytes(), ymdPath);
		} else {
			fileName = uploadPath + File.separator + "images" + File.separator + "none.png";
		}
//		member.
		
		return "redirect:/";
	}
	
	
}
