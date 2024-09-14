package com.atguigu;

import io.minio.*;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * MinIO上传样例代码
 * @author Sam
 * @create 2024-09-14 11:21 AM
 */
public class TestMinio {

  public static void main(String[] args) {

    String endpoint = "http://192.168.56.101:9000/";
    String accessKey = "minioadmin";
    String secretKey = "minioadmin";
    String bucketName = "hello-minio";

    // 获取minioClient对象
    MinioClient minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();

    try {
      boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
      if (!bucketExists) {
        //创建hello-minio桶
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        //设置hello-minio桶的访问权限
        String policy = """
                {
                  "Statement" : [ {
                    "Action" : "s3:GetObject",
                    "Effect" : "Allow",
                    "Principal" : "*",
                    "Resource" : "arn:aws:s3:::%s/*"
                  } ],
                  "Version" : "2012-10-17"
                }
                """.formatted(bucketName);

        minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policy).build());
      }
      //上传图片
      minioClient.uploadObject(
              UploadObjectArgs.builder()
                      .filename("/home/sam/JAVA/apartment/Doc/2.资料/7.images/公寓-外观.jpg")
                      .bucket(bucketName)
                      .object("公寓-外观.jpg")
                      .build());
      System.out.println("上传成功");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
