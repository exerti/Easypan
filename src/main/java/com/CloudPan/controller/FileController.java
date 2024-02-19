package com.CloudPan.controller;



import com.CloudPan.controller.utils.ResultData;
import com.CloudPan.entity.File;
import com.CloudPan.entity.dto.NewFolderDTO;
import com.CloudPan.entity.enums.FileTypeEnums;
import com.CloudPan.service.impl.FileServiceImpl;
import com.CloudPan.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Wxz
 * @since 2024-01-18
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired

    private  FileServiceImpl fileService ;
    @Value("${file.desc}")
    private String fileDesc;

    @GetMapping
    public String file(){
        return "test";
    }

    @GetMapping("/filelist")
    public ResultData<List<File>> filelist(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize , String Categroy){
        Page<File>  page   = new PageDTO<>(pageNo-1, pageSize);
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_category",Categroy);

        return ResultData.success( fileService.list(page,queryWrapper));
    }

    @GetMapping("/allFileSize")
    public ResultData<String> allFileSize(HttpServletRequest request){
        Integer uid = (Integer)   request.getSession().getAttribute("uid");
        return ResultData.success(fileService.getAllFileSize(uid));
    }


    @PostMapping("/uploadFile")
    public ResultData<String> uploadFile(HttpServletRequest request,
                                         @RequestParam("file") MultipartFile file,
                                         String fileName,
                                         Integer fileType,
                                         String filePid,
                                         String md5
    ) throws IOException {

        boolean  hasFile = fileService.getFileByMd5(md5);
        if(hasFile){
            return ResultData.success("文件已存在");
        }


//      String filesID = UUID.randomUUID().toString();
        Integer uid= (Integer) request.getSession().getAttribute("uid");
        String FilePath = fileDesc + file.getOriginalFilename();
        String Suffix = FileUtil.getFileSuffix(file);
        if(fileName==null){
            fileName = file.getOriginalFilename();
        }
        if(fileType==null){
            fileType =  FileTypeEnums.getBySuffix(Suffix).getType();
        }
        File Files  =new File(uid,md5,file.getSize(),fileName,FilePath, LocalDateTime.now(),fileType);
        fileService.save(Files);
        fileService.SaveFile(file);
        return ResultData.success("");

    }


    /**
     * 新建文件夹
     * @param folder
     * @param request
     * @return
     */

    @PostMapping("/newFolder")
    public File newFolder(@RequestBody NewFolderDTO folder , HttpServletRequest request){
        return  fileService.newFloder(folder,(Integer) request.getSession().getAttribute("uid"));
    }

//    @GetMapping("/getFolderInfo")
//    public ResultData getFolderInfo(HttpServletRequest request , @NotEmpty String path){
//        return File
//    }


}
