package com.CloudPan.service.impl;

import com.CloudPan.entity.File;
import com.CloudPan.mapper.FileMapper;
import com.CloudPan.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
