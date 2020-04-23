package com.gmimg.multicampus.springboot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
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
import com.gmimg.multicampus.springboot.member.Item;
import com.gmimg.multicampus.springboot.member.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("sessionMem")
// @Configuration
public class HomeController {

    @Autowired
    IMemMapper memMapper;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    private AmazonS3 s3;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        final String endPoint = "https://kr.object.ncloudstorage.com";
        final String regionName = "kr-standard";
        s3 = AmazonS3ClientBuilder.standard()
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                // .withRegion("kr-standard")
                .build();
    }



    // String accessKey = "5qQSeTt9g4P4pUWdllTK";
    // String secretKey = "a0ljKI9ykkmNBDmitaXRa2tSUSteSDTs4CJncm2Q";

    // final String endPoint = "https://kr.object.ncloudstorage.com";
    // final String regionName = "kr-standard";
    // final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
    // .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
    // .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
    // .build();


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
    public int getididx(@ModelAttribute("sessionMem") Member member) {
        return member.getMemIdx();
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public ModelAndView result(String results, String filename, ModelAndView mav) { // , @RequestParam

        String staticPath = "/static/";
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
    public ModelAndView mypage(@ModelAttribute("sessionMem") Member member, ModelAndView mav) {

        // session의 id index 가져와서 저장
        int mem_idx = member.getMemIdx();

        // session의 id가 가진 item 저장
        List<Item> items = memMapper.findItem(mem_idx);

        List<Integer> iss = new ArrayList<>();
        List<String> fs = new ArrayList<>();
        List<Float> accs = new ArrayList<>();

        int i;
        String f;
        float acc;

        bucketName = "deepfake-thumb";
        String staticPath = "/static/";

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
    




        // bucketName = "deepfake-thumb";
        // ListObjectsV2Result result = s3.listObjectsV2(bucketName);
        // List<S3ObjectSummary> objects = result.getObjectSummaries();
        // for (S3ObjectSummary os : objects) {
        //     System.out.println("* " + os.getKey());
        // }









	// @ResponseBody
    // @RequestMapping(value = "/upload", method = RequestMethod.POST)
    // public String uploadForm(MultipartFile file, ModelAndView mav) throws Exception {
    //     // logger.info(file.getOriginalFilename());
    //     // logger.info(file.getContentType());
    //     // logger.info("파일 크기 : "+file.getSize());
        
    //     String savedName = file.getOriginalFilename();

    //     savedName = "test1.webm";

    //     File target = new File(uploadPath, savedName);
        
    //     //임시디렉토리에 저장된 업로드 파일을 지정된 디렉토리로 복사
    //     //FileCopyUtils.copy(바이트 배열, 파일 객체)
    //     FileCopyUtils.copy(file.getBytes(), target);
        
    //     // mav.setViewName("uploadResult");
    //     // mav.addObject("savedName", savedName);
        
    //     return "kk";
    // }




    // @ResponseBody
    // @RequestMapping(value = "/predictspring", method = RequestMethod.POST)
    // public String predictspring(MultipartFile file, ModelAndView mav) throws Exception {
        
        
    //     return ;
    // }