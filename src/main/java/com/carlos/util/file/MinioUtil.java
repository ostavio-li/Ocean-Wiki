package com.carlos.util.file;

import com.carlos.ocean.pojo.EditRecord;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Carlos Li
 * @date 2021/5/27
 */

public class MinioUtil {

    @Autowired
    private MinioClient client;

    @SneakyThrows
    public boolean existsBucket(String bucketName) {
        return client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    @SneakyThrows
    public boolean makeBucket(String bucketName) {
        if (!existsBucket(bucketName)) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            return true;
        } else {
            return false;
        }

    }

    @SneakyThrows
    public List<EditRecord> listEditRecords() {
        Iterable<Result<Item>> iterable = client.listObjects(ListObjectsArgs.builder().bucket("articles").build());
        List<EditRecord> list = new ArrayList<>();
        for (Result<Item> result : iterable) {
            String name = result.get().objectName();
            if (name.contains("_")) {
                String[] split = name.split("_");
                list.add(new EditRecord(split[0], split[1]));
            }
        }
        return list;
    }

    @SneakyThrows
    public List<String> listArticleTitles() {
        Iterable<Result<Item>> iterable = client.listObjects(ListObjectsArgs.builder().bucket("articles").build());
        List<String> list = new ArrayList<>();
        for (Result<Item> result : iterable) {
            String objectName = result.get().objectName();
            if (!objectName.contains("_")) {
                list.add(objectName);
            }
        }
        return list;
    }

    @SneakyThrows
    public List<String> listArticleTitlesContains(String s) {
        Iterable<Result<Item>> iterable = client.listObjects(ListObjectsArgs.builder().bucket("articles").build());
        List<String> list = new ArrayList<>();
        for (Result<Item> result : iterable) {
            String objectName = result.get().objectName();
            if (objectName.contains(s)) {
                list.add(objectName);
            }
        }
        return list;
    }

    @SneakyThrows
    public List<String> listArticleTitlesEndsWith(String endsWith) {
        Iterable<Result<Item>> iterable = client.listObjects(ListObjectsArgs.builder().bucket("articles").build());
        List<String> list = new ArrayList<>();
        for (Result<Item> result : iterable) {
            String objectName = result.get().objectName();
            if (objectName.endsWith(endsWith)) {
                list.add(objectName);
            }
        }
        return list;
    }

    @SneakyThrows
    public boolean uploadArticle(String title, String article) {
        byte[] bytes = article.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        client.putObject(PutObjectArgs.builder().bucket("articles").object(title).stream(bais, bais.available(), -1).build());
        return true;
    }


    public String getArticle(String articleTitle) {
        try {
            InputStream inputStream = client.getObject(GetObjectArgs.builder().bucket("articles").object(articleTitle).build());
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) != -1) {
                result.write(bytes, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8);
        } catch (MinioException e) {
            return null;
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @SneakyThrows
    public void deleteArticle(String articleTitle) {
        client.removeObject(
                RemoveObjectArgs.builder()
                        .bucket("articles")
                        .object(articleTitle)
                        .build()
        );
        System.out.println("完成删除 " + articleTitle);
    }

}
