package com.gmimg.multicampus.springboot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import com.gmimg.multicampus.springboot.mapper.IItemMapper;
import com.gmimg.multicampus.springboot.member.Item;
import com.gmimg.multicampus.springboot.member.Member;

@Controller
public class HomeController {

    @Autowired
    IItemMapper mapper;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    private AmazonS3 s3;
    
    String staticPath = "/static/";
//    String staticPath = "E:/dev/git/Final_Project/WEB/spring-boot/application/src/main/resources/static/deepImg/";

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        final String endPoint = "https://kr.object.ncloudstorage.com";
        final String regionName = "kr-standard";
        s3 = AmazonS3ClientBuilder.standard()
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    String bucketName;
    String objectName;
    String downloadPath;

    // 메인페이지 매핑
	@RequestMapping("/")
	public String indexPage() {
		return "index";
	}

    @RequestMapping("/uploadForm")
    public String uploadForm() {
        return "uploadForm";
    }

    // 아이디에 따른 인덱스 가져오는 매핑
    @RequestMapping(value = "/getididx", method = RequestMethod.GET)
    @ResponseBody
    public int getididx(HttpServletRequest request) {

    	HttpSession session = request.getSession();
    	Member member = (Member) session.getAttribute("sessionMem");

        if (member == null){
            return 0;
        }
        else {
            return member.getMemIdx();
        }
    }
    
    //분석한 뒤 로컬 저장소에 저장되어있는 파일 삭제하는 매핑
    @ResponseBody
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public Integer del(String filename) {
        System.out.println(filename);
        mapper.deleteItem(filename);
        bucketName = "deepfake";
        filename = filename + ".webm";
        System.out.println(filename);
        try {
            s3.deleteObject(bucketName, filename);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            // System.exit(1);
        }
        return 0;
    }
    
    // 마이페이지 내 동영상 출력 매핑
    @ResponseBody
    @RequestMapping(value = "/viewvid", method = RequestMethod.GET)
    public Integer viewvid(String filename) { // , @RequestParam
        
        bucketName = "deepfake";
        objectName = filename + ".webm";
        downloadPath = staticPath + objectName;

        try {
            S3Object o = s3.getObject(bucketName, objectName);
            S3ObjectInputStream s3is = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(new File(downloadPath));
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            // System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            // System.exit(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            // System.exit(1);
        }
        return 1;
    }
    // 결과창 매핑
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public ModelAndView result(String results, String filename, String frames, String count, String fake_num, ModelAndView mav) { // , @RequestParam
        
        bucketName = "deepfake";
        objectName = filename + ".webm";
        downloadPath = staticPath + objectName;

        try {
            S3Object o = s3.getObject(bucketName, objectName);
            S3ObjectInputStream s3is = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(new File(downloadPath));
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
            mav.setViewName("result");
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            mav.setViewName("error/error");
            // System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            mav.setViewName("error/error");
            // System.exit(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            mav.setViewName("error/error");
            // System.exit(1);
        }

        Float r = Float.parseFloat(results);
        String res = String.format("%.4f", r*100);
        mav.addObject("filename", filename);
        mav.addObject("res", res);
        mav.addObject("frames", frames);
        mav.addObject("count", count);
        mav.addObject("fake_num", fake_num);
        return mav;
    }
    // 마이페이지 매핑
    @RequestMapping(value="/mypage", method=RequestMethod.GET)
    public ModelAndView mypage(ModelAndView mav, HttpServletRequest request) throws Exception {
    	HttpSession session = request.getSession();
    	Member member = (Member) session.getAttribute("sessionMem");

        // session의 id index 가져와서 저장
        int mem_idx = member.getMemIdx();

        // session의 id가 가진 item 저장
        List<Item> items = mapper.findItem(mem_idx);

        List<Integer> iss = new ArrayList<>();
        List<String> fs = new ArrayList<>();
        List<Float> accs = new ArrayList<>();
        List<Integer> deletes = new ArrayList<>();

        int i;
        String f;
        float acc;
        int d;

        bucketName = "deepfake-thumb";
        

        for (Item item: items){
            i = item.getIditem();
            f = item.getFilename();
            acc = item.getAcc();
            d = item.getDel();

            iss.add(i);
            fs.add(f);
            accs.add(acc);
            deletes.add(d);

            objectName = Integer.toString(i) + ".jpg";
            downloadPath = staticPath + objectName;

            System.out.println(objectName);
                
            try {
                S3Object o = s3.getObject(bucketName, objectName);
                S3ObjectInputStream s3is = o.getObjectContent();
                FileOutputStream fos = new FileOutputStream(new File(downloadPath));
                byte[] read_buf = new byte[1024];
                int read_len = 0;
                while ((read_len = s3is.read(read_buf)) > 0) {
                    fos.write(read_buf, 0, read_len);
                }
                s3is.close();
                fos.close();
                mav.setViewName("myPage");
            } catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                mav.setViewName("error/error");
                // System.exit(1);
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                mav.setViewName("error/error");
                // System.exit(1);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                mav.setViewName("error/error");
                // System.exit(1);
            }

        }
        
        mav.addObject("iss", iss);
        mav.addObject("fs", fs);
        mav.addObject("accs", accs);
        mav.addObject("deletes", deletes);
        System.out.println(deletes);

        return mav;
    }

}