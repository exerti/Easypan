package com.CloudPan.service.impl;

import com.CloudPan.entity.File;
import com.CloudPan.mapper.FileMapper;
import com.CloudPan.service.IFileService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Wxz
 * @since 2024-01-18
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {
    @Value("${file.desc}")
    private String fileDesc;
    public Long getAllFileSize(Integer uid) {
       QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id" ,uid);
        return   baseMapper.selectCount(queryWrapper);
    }

    public boolean getFileByMd5(String md5) {
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_md5" ,md5);
        return   baseMapper.selectCount(queryWrapper) > 0;
    }
    public Boolean SaveFile(MultipartFile file) throws IOException {
       try{
//           String uuid = UUID.randomUUID().toString();
           String originalFilename = file.getOriginalFilename();
//           String ext = "." +
//                   originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
           String  filename =  originalFilename;
           String filePath = fileDesc + filename;
           java.io.File newFile = new java.io.File(fileDesc + filename);
           if (!newFile.getParentFile().exists())
           {
                newFile.getParentFile().mkdirs();
           }
           file.transferTo(newFile );
       }catch (IOException e){
            e.printStackTrace();
            return false;
       }
       return true;
    }
    public  String getFileType(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
          String ext = "." +
                   originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
          return  ext;
    }
}
