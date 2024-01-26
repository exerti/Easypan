package com.CloudPan.utils;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@RestController
@Slf4j
public class KaptchaUtil {

    @Autowired
    private Producer kaptchaProducer;
//    private RedisTemplate  redisTemplate;


    //获取验证码图片
    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        //生成验证码
        String text = kaptchaProducer.createText();

        //生成图片
        BufferedImage image = kaptchaProducer.createImage(text);
        //将验证码存入session
        session.setAttribute(Constants.KAPTCHA_SESSION_CONFIG_KEY,text);
        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image,"png",outputStream);
        }catch (Exception e){
            log.error("验证码获取失败:"+e.getMessage());
        }
    }
    //效验验证码
    @PostMapping("/kaptcha")
    public Boolean cheakKaptcha(@RequestParam("code") String code , HttpServletRequest request){
        HttpSession session = (HttpSession) request.getSession();
        //不使用redis
        String  expected = (String) session.getAttribute(Constants.KAPTCHA_SESSION_CONFIG_KEY);
        System.out.println(expected);
        if(!code.equalsIgnoreCase(expected)){
            request.setAttribute("error","验证码错误");
            log.error("验证码出错");
            return false;
        }
        return true;
    }
}
