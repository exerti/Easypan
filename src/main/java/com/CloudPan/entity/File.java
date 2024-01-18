package com.CloudPan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Wxz
 * @since 2024-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_file")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    @TableId(value = "file_id", type = IdType.AUTO)
    private Long fileId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 文件MD5值
     */
    @TableField("file_md5")
    private String fileMd5;

    /**
     * 文件大小
     */
    @TableField("file_size")
    private String fileSize;

    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 预览封面
     */
    @TableField("file_cover")
    private String fileCover;

    /**
     * 文件路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @TableField("last_update_time")
    private LocalDateTime lastUpdateTime;

    /**
     * 0：目录  1：文件
     */
    @TableField("folder_type")
    private Integer folderType;

    /**
     * 文件分类：1、视频、2、音频、3、图片、4、文档、5、其他
     */
    @TableField("file_category")
    private Integer fileCategory;

    /**
     * 文件类型：1、视频、2、音频、3、图片、4、pdf、5、doc、6、excel、7、txt、8、code、9、zip、10、其他
     */
    @TableField("file_type")
    private Integer fileType;

    /**
     * 0:转码中 1：转码失败 2：转码成功
     */
    @TableField("status")
    private Integer status;

    /**
     * 进入回收站时间
     */
    @TableField("recovery_time")
    private LocalDateTime recoveryTime;

    /**
     * 标记删除  0:删除 1：回收站 2 : 正常
     */
    @TableField("del_flag")
    private Integer delFlag;


}
