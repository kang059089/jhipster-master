package com.kang.jhipster.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PictureUtil {
    private final Logger log = LoggerFactory.getLogger(PictureUtil.class);

    /**
     * 生成的图片绝对路径+文件名
     * @param request
     * @param documentName
     * @param userId
     * @return
     */
    public static String makeImgFilePath(HttpServletRequest request, String documentName, Long userId){
        //保存的图片名称
        String pictureName = documentName + userId + ".png";
        //获取Web项目的全路径
        String strDirPath  = request.getSession().getServletContext().getRealPath("/");
        //判断保存图片的文件夹是否存在，不存在则创建
        File dirFile = new File( strDirPath + "/content/" + documentName);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        //生成图片保存路径
        String imgFilePath = strDirPath + "/content/" + documentName + "/" + pictureName;
        //判断图片保存路径是否已存在名称相同的图片，存在则删除
        File file = new File(imgFilePath);
        if (file.exists()) {
            file.delete();
        }
        return imgFilePath;
    }

    /**
     * 将前端上传的图片存储到服务器上
     * @param avatarUpload 头像图片参数
     * @param imgFlogder 头像存储路径常量部分
     * @param documentName 文件夹名（存储路径中的文件夹）
     * @param userId 用户id
     * @param fileName 图片名称
     * @return 返回存储路径
     */
    public static String makeImgPath(MultipartFile avatarUpload, String imgFlogder, String documentName, String userId, String fileName) {
        //判断服务器上保存图片的文件夹是否存在，不存在则创建
        File dirFile = new File( imgFlogder + documentName + "/" + userId);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        //生成图片存储路径
        String imgFilePath = documentName + "/" + userId + "/" + fileName;
        //判断图片存储路径下是否已存在名称相同的图片，存在则删除
        File file = new File(imgFlogder + imgFilePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            //保存文件
            avatarUpload.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgFilePath;
    }

    /**
     * base64字节生成图片是否成功
     * @param base64 base64字符串
     * @param imgFilePath 生成的图片绝对路径+文件名
     * @return
     */
    public static boolean makePicture(String base64, String imgFilePath) {
        if (base64 == null) {
            return false;
        };
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(base64);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }

            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
