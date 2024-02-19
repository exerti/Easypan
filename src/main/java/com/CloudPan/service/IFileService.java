package com.CloudPan.service;

import com.CloudPan.entity.File;
import com.CloudPan.entity.dto.NewFolderDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Wxz
 * @since 2024-01-18
 */
public interface IFileService extends IService<File> {
    /**
     * 新建目录
     * @param folderDTO  目录信息
     * @param userId  用户ID
     * @return
     */
    File  newFloder(NewFolderDTO folderDTO, Integer userId);

    List<File> listFolderByIds(String[] ids);
}
