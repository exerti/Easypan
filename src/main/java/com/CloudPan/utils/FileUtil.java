package com.CloudPan.utils;

import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;

public class FileUtil {
    /**
     * 根据文件大小转换为B、KB、MB、GB单位字符串显示
     * @param filesize 文件的大小（long型）
     * @return 返回 转换后带有单位的字符串
     */
    public  static String GetLength(long filesize){

        String strFileSize = null;
        if(filesize < 1024){
            strFileSize = filesize+"B";
            return strFileSize;
        }

        DecimalFormat df = new DecimalFormat("######0.00");

        if ((filesize >= 1024) && (filesize < 1024*1024)){//KB
            strFileSize = df.format(((double)filesize)/1024)+"KB";
        }else if((filesize >= 1024*1024)&&(filesize < 1024*1024*1024)){//MB
            strFileSize = df.format(((double)filesize)/(1024*1024))+"MB";
        }else{//GB
            strFileSize = df.format(((double)filesize)/(1024*1024*1024))+"GB";
        }
        return strFileSize;
    }

    public static   String getFileSuffix(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        String ext = "." +
                originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        return  ext;
    }

}
