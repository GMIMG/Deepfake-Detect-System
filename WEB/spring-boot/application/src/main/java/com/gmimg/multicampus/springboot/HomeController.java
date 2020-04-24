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
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.gmimg.multicampus.springboot.mapper.IMemMapper;
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

	@RequestMapping("/")
	public String indexPage() {
		return "index";
	}

    @RequestMapping("/uploadForm")
    public String uploadForm() {
        return "uploadForm";
    }

    @RequestMapping(value = "/getididx", method = RequestMethod.GET)
    @ResponseBody
    public int getididx(HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	Member member = (Member) session.getAttribute("sessionMem");
    	
        return member.getMemIdx();
    public int getididx(HttpServletRequest request) {
        System.out.println(member);
        if (member == null){
            return 0;
        }
        else {
            return member.getMemIdx();
        }
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public ModelAndView result(String results, String filename, ModelAndView mav) { // , @RequestParam

        
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
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        mav.setViewName("result");
        mav.addObject("filename", filename);
        
        return mav;
    }



    @RequestMapping(value="/mypage", method=RequestMethod.GET)
    public ModelAndView mypage(ModelAndView mav, HttpServletRequest request) throws Exception {
    	HttpSession session = request.getSession();
    	Member member = (Member) session.getAttribute("sessionMem");
    	
    	System.out.println(member.toString());

        // session의 id index 가져와서 저장
        int mem_idx = member.getMemIdx();

        // session의 id가 가진 item 저장
        List<Item> items = mapper.findItem(mem_idx);

        List<Integer> iss = new ArrayList<>();
        List<String> fs = new ArrayList<>();
        List<Float> accs = new ArrayList<>();

        int i;
        String f;
        float acc;

        bucketName = "deepfake-thumb";
        

        for (Item item: items){
            i = item.getIditem();
            f = item.getFilename();
            acc = item.getAcc();

            iss.add(i);
            fs.add(f);
            accs.add(acc);

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
            } catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                System.exit(1);
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }

        }
        
        mav.setViewName("myPage");
        mav.addObject("iss", iss);
        mav.addObject("fs", fs);
        mav.addObject("accs", accs);

        return mav;
    }

}