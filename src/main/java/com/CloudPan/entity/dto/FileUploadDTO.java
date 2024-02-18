package com.CloudPan.entity.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FileUploadDTO {
    private String id;

    @NotEmpty
    private String filename;

    private String filePid;

    @NotEmpty
    private String fileMd5;

    // 当前分片
    @NotNull
    private Integer chunkIndex;

    // 总分片数量
    @NotNull
    private Integer chunks;
}
