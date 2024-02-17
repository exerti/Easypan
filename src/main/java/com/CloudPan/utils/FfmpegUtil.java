package com.CloudPan.utils;

import com.CloudPan.entity.File;

import java.util.logging.Logger;

public class FfmpegUtil {

    //日志

    /**
     *  转换视频文件为ts文件
     * @param videoPath
     * @param saveTsPath
     * desc: 将文件转化为ts格式，全称是MPEG2-TS
     */
    public  static  void transferTS(String videoPath ,  String saveTsPath){
        // "ffmpeg -y -i %s -vcodec copy -acodec copy -vbsf h264_mp4toannexb %s"
        String command = "ffmpeg -y -i " + videoPath + " -vcodec copy -acodec copy -vbsf h264_mp4toannexb " + saveTsPath;
        ProcessBuilder builder = new ProcessBuilder(command.split(" "));
        try {
            Process process = builder.start();
            //等待子进程执行完毕
            process.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(FfmpegUtil.class.getName()).info("转换ts文件失败");
            throw new RuntimeException("视频转换失败");
        }

    }

    /**
     * 视频切片
     * @param tsPath
     * @param m38uPath
     * @param tsFloder
     * @param fileId
     */
    public static void cutTs(String tsPath,
                             String m38uPath,
                             String tsFloder,
                             Integer fileId){
        //  "ffmpeg -i %s -c copy -map 0 -f segment -segment_list %s -segment_time 30 %s/%s_%%4d.ts"

        String command = "ffmpeg -i " + tsPath + " -c copy -map 0 -f segment -segment_list "
                + m38uPath + " -segment_time 30 " + tsFloder + "/" + fileId + "_%%4d.ts";
        ProcessBuilder builder = new ProcessBuilder(command.split(" "));
        try {
            Process process = builder.start();
            //等待子进程执行完毕
            process.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(FfmpegUtil.class.getName()).info("文件切片失败");
            throw  new RuntimeException("视频转化失败");

        }

    }

    public static  void createTargetThumbnail(File sourceFile, Integer width, File targetFile){
        // "ffmpeg -i %s -y -vframes 1 -vf scale=%d:%d/a %s"
       String command = "ffmpeg -i " + sourceFile.getAbsolutePath() + " -y -vframes 1 -vf scale=" + width + ":? " + targetFile.getAbsolutePath();
            ProcessBuilder builder = new ProcessBuilder(command.split(" "));
            try {

                Process process = builder.start();
                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
                Logger.getLogger(FfmpegUtil.class.getName()).info("视频转化失败");
                throw  new RuntimeException("视频转化失败");

            }
    }

}
