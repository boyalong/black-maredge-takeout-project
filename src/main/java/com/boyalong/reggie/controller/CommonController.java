package com.boyalong.reggie.controller;

import com.boyalong.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 *  文件上传和下载
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath ;

    /**
     * 文件的上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //这个file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除

        //拿到文件的原始名
        String originalFilename = file.getOriginalFilename();
        //拿到文件的原始后缀名
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用uuid生成的作为文件名的一部分，这样可以防止文件名相同造成的文件覆盖
        String FileName = UUID.randomUUID().toString() + substring;

//        如果当前文件路径不存在，创建一个新的路径
        File file1 = new File(basePath);
        if(!file1.exists()){
            //文件目录不存在，直接创建一个目录
            file1.mkdirs();
        }

        try {
            //把前端传过来的文件进行转存
            file.transferTo(new File(basePath + FileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(FileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流，通过输入流读取文件内容  这里的name是前台用户需要下载的文件的文件名
            //new File(basePath + name) 是为了从存储图片的地方获取用户需要的图片对象
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));

            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            //设置写回去的文件类型
            response.setContentType("image/jpeg");

            //定义缓存区，准备读写文件
            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
