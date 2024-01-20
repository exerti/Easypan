package com.CloudPan.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public String file(){
        return "file";
    }


}
