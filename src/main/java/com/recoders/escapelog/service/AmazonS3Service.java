package com.recoders.escapelog.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    public static String domainName;

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.cloudFront.distributionDomain}")
    public void setDomainName(String domainName){
        AmazonS3Service.domainName = domainName;
    }

    @PostConstruct
    public void setS3Client(){
        AWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(this.region)
                .build();
    }

    public String upload(MultipartFile file, String filePath) throws IOException {

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        s3Client.putObject(new PutObjectRequest(bucket, filePath, file.getInputStream(),metadata)
        .withCannedAcl(CannedAccessControlList.PublicRead));

        return filePath;
    }

    public String uploadRecodeImg(MultipartFile file) throws IOException {
        String saveFileName = changeFileName(file.getOriginalFilename());
        String filePath = getRecodeImgFilePath(saveFileName);
        return upload(file,filePath);
    }

    public void delete(String currentFilePath){
        boolean isExistObject = s3Client.doesObjectExist(bucket, currentFilePath);
        if (isExistObject){
            s3Client.deleteObject(bucket, currentFilePath);
        }
    }

    public String changeFileName(String originFileName){
        String ext = originFileName.substring(originFileName.lastIndexOf('.'));
        UUID uuid = UUID.randomUUID();
        long millis = System.currentTimeMillis();
        return uuid.toString()+millis+ext;
    }

    public String getThemeImgFilePath(String areaName, String fileName){
        return "theme/"+areaName+"/"+fileName;
    }

    public String getRecodeImgFilePath(String fileName){
        return "recode/"+fileName;
    }

    public static String getImageUrl(String imagePath){
        if (imagePath != null){
            imagePath = "https://"+ domainName +"/"+imagePath;
        }
        return imagePath;
    }
}
