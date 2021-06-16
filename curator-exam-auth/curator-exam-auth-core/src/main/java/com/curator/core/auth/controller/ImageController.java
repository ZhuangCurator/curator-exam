package com.curator.core.auth.controller;

import cn.hutool.core.util.RandomUtil;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.MinioUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传控制器
 *
 * @author Jun
 * @date 2021/6/16
 */
@RestController
public class ImageController {

    /**
     * 上传图片
     *
     * @param image
     * @return
     */
    @PostMapping("/uploadImage")
    ResultResponse<?> uploadImage(MultipartFile image) throws IOException {
        String fileUrl = MinioUtil.upload("image-bucket",
                RandomUtil.randomString(10) + ".png",
                image.getInputStream());
        Map<String, String> map = new HashMap<>(8);
        map.put("url", fileUrl);
        return ResultResponse.builder().success("图片上传成功").data(map).build();
    }
}