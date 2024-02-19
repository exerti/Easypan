package com.CloudPan.service.impl;

import com.CloudPan.entity.File;
import com.CloudPan.entity.dto.NewFolderDTO;
import com.CloudPan.entity.enums.FileDelFlagEnums;
import com.CloudPan.entity.enums.FileFolderTypeEnums;
import com.CloudPan.entity.enums.FileStatusEnums;
import com.CloudPan.mapper.FileMapper;
import com.CloudPan.service.IFileService;
import com.CloudPan.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    public String getAllFileSize(Integer uid) {
       QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id" ,uid);
        ;
        return FileUtil.GetLength(baseMapper.selectList(queryWrapper)
                .stream()
                .mapToLong(File::getFileSize)
                .sum());
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

    @Override
    public File newFloder(NewFolderDTO folderDTO, Integer userId) {
        // 校验文件夹名
        String rename = autoRename(folderDTO.getFilePid(), userId, folderDTO.getFilename());
        // 构造属性
        File fileInfo = new File();
        fileInfo.setUserId(userId);
        fileInfo.setFileName(rename);
        fileInfo.setFilepid(folderDTO.getFilePid());
        fileInfo.setFolderType(FileFolderTypeEnums.FOLDER.getType());
        fileInfo.setStatus(FileStatusEnums.USING.getStatus());
        // 保存
        this.save(fileInfo);
        // 返回
//        FileInfoVO fileInfoVO = new FileInfoVO();
//        BeanUtils.copyProperties(fileInfo, fileInfoVO);
        return fileInfo;
    }

    @Override
    public List<File> listFolderByIds(String[] ids) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(File::getFileId, File::getFileName).in(File::getFileId, Arrays.asList(ids)).last("order by field(id, \"" + StringUtils.join(ids, "\",\"") + "\")");
        List<File> list = list(wrapper);
        List<File> fileInfoVOS = list.stream().map(item -> {
            File fileInfoVO = new File();
            return fileInfoVO;
        }).collect(Collectors.toList());
        return fileInfoVOS;
    }


    /**
     * 当文件名字相同时，重命名文件
     *
     * @param filePid  文件PID
     * @param userId   用户ID
     * @param filename 文件名
     */
    private String autoRename(String filePid, Integer userId, String filename) {
        int count = (int) this.count(new LambdaQueryWrapper<File>()
                .eq(File::getUserId, userId)
                .eq(File::getFilepid, filePid)
                .eq(File::getDelFlag, FileDelFlagEnums.USING.getFlag())
                .eq(File::getFileName, filename));
        if (count > 0) {
            filename = LocalDateTime.now().getMonth() + filename ;
        }
        return filename;
    }
}
