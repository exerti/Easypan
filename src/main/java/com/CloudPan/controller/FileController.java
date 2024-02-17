package com.CloudPan.controller;



import com.CloudPan.controller.utils.ResultData;
import com.CloudPan.entity.File;
import com.CloudPan.entity.User;
import com.CloudPan.service.impl.FileServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

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
    public ResultData<Page<File>> filelist(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize){
        Page<File>  page   = new PageDTO<>(pageNo-1, pageSize);
        fileService.page(page);
        return ResultData.success(page);
    }

    @GetMapping("/allFileSize")
    public ResultData<Long> allFileSize(HttpServletRequest request){
        Integer uid = (Integer)   request.getSession().getAttribute("user_id");
        return ResultData.success(fileService.getAllFileSize(uid));
    }


    @PostMapping("/uploadFile")
    public ResultData<String> uploadFile(HttpServletRequest request,
                                         @RequestParam("file") MultipartFile file,
                                         String fileName,
                                         String fileType,
                                         String filePid,
                                         String Md5
    ) throws IOException {

        boolean  hasFile = fileService.getFileByMd5(Md5);
        if(hasFile){
            return ResultData.success("文件已存在");
        }


//      String filesID = UUID.randomUUID().toString();
        Integer uid= (Integer) request.getSession().getAttribute("uid");
        System.out.println(uid);
        String FilePath = fileDesc + file.getOriginalFilename();
        String FileType = fileService.getFileType(file);
        File Files  =new File(uid,Md5,file.getSize(),file.getOriginalFilename(),FilePath, LocalDateTime.now(),fileType);
        fileService.save(Files);
        fileService.SaveFile(file);
        return ResultData.success("");

    }




}
