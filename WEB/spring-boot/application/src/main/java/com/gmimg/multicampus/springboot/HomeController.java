package com.gmimg.multicampus.springboot;

import java.io.File;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    // @Resource(name = "uploadPath")
	String uploadPath = "/static/";

    @RequestMapping("/uploadForm")
    public String uploadForm() {
        //uploadForm.jsp 페이지로 포워딩
        return "uploadForm";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST )
    public String uploadForm(MultipartFile file, ModelAndView mav) throws Exception {
        // logger.info(file.getOriginalFilename());
        // logger.info(file.getContentType());
        // logger.info("파일 크기 : "+file.getSize());
        
        String savedName = file.getOriginalFilename();

        savedName = "test1.mp4";

        File target = new File(uploadPath, savedName);
        
        //임시디렉토리에 저장된 업로드 파일을 지정된 디렉토리로 복사
        //FileCopyUtils.copy(바이트 배열, 파일 객체)
        FileCopyUtils.copy(file.getBytes(), target);
        
        // mav.setViewName("uploadResult");
        // mav.addObject("savedName", savedName);
        
        return "uploadResult";
    }
}




