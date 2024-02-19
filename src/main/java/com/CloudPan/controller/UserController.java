package com.CloudPan.controller;


import com.CloudPan.controller.utils.ResultData;
import com.CloudPan.controller.utils.ReturnCodeEnum;
import com.CloudPan.entity.User;
import com.CloudPan.service.impl.UserServiceImpl;
import com.CloudPan.utils.IpUtil;
import com.CloudPan.utils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
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
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    public ResultData<String> register(@RequestBody User user ,HttpServletRequest request) throws SocketException, UnknownHostException {
        //先查数据库中是否由该对象，没有该对象就添加
        String macAddress = IpUtil.getMacadress(request);
        user.setMacAddress(macAddress);
        boolean IsAdded = userService.save(user);
        if (IsAdded)
            return ResultData.success("注册成功");
        return ResultData.fail(999, "操作失败");
    }
    @GetMapping("/login") public ResultData<User> login(@RequestParam("username") String username, @RequestParam("password") String password  , HttpServletRequest request) {
        User user = userService.login(username, password);
        if (user != null){
            String token = JwtUtils.generateToken();
            user.setToken(token);
            request.getSession().setAttribute("uid" , user.getUserId());
            return ResultData.success(user);
        }
        return ResultData.fail(ReturnCodeEnum.USER_NOT_EXIST);
    }
    @DeleteMapping("/delete/{id}")
    public ResultData<String> delete(@PathVariable("id") Integer id) {
        boolean IsDeleted = userService.removeById(id);
        if (IsDeleted)
            return ResultData.success("删除成功");
        return ResultData.fail(999, "操作失败");
    }

    @PutMapping("/update")
    public ResultData<String> update(@RequestBody User user) {
        boolean IsUpdated =
                userService.updateById(user);
        if (IsUpdated)
            return ResultData.success("更新成功");
        return ResultData.fail(999, "操作失败");
    }

    @GetMapping("/get/{id}")
    public ResultData<User> get(@PathVariable("id") Integer id) {
        User user = userService.getById(id);
        if (user != null)
            return ResultData.success(user);
        return ResultData.fail(999, "操作失败");
    }

    @GetMapping("/list")
    public ResultData<List<User>> list() {
        List<User> userList = userService.list();
        if (userList != null)
            return ResultData.success(userList);
        return ResultData.fail(999, "操作失败");
    }

    @GetMapping("/page")
    public ResultData<Page<User>> page(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "5") Integer pageSize) {
        Page<User> page = new Page<>(pageNo-1, pageSize);
        userService.page(page);
        return ResultData.success(page);
    }

    @PostMapping("/save")
    public ResultData<String> save(@RequestBody User user) {
        boolean IsSaved = userService.save(user);
        if (IsSaved)
            return ResultData.success("保存成功");
        return ResultData.fail(999, "操作失败");
    }

    @GetMapping("/getinfo")
    public ResultData<User> getUserinfo(HttpServletRequest request) {
         Integer uid= (Integer) request.getSession().getAttribute("uid");
         User  user  =  userService .getOne(new QueryWrapper<User>().eq("user_id",uid));
         return ResultData.success(user);
    }

    @PostMapping("/logout")
    public ResultData<String> logout(HttpServletRequest request) {
        // 清除session中的用户信息
        request.getSession().removeAttribute("uid");
        return ResultData.success("退出成功");
    }

    @PostMapping("/resetPwd")
    public ResultData<String> resetPwd(HttpServletRequest request , @RequestParam  String Pwd) {
        //针对Pwd进行密码效验
        //采用邮箱验证码的技术

        Integer uid=  (Integer) request.getSession().getAttribute("uid");
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        boolean IsUpdated = userService.update(updateWrapper.eq("user_id", uid).set("password", Pwd));
        if (IsUpdated)
            return ResultData.success("修改成功");
        return ResultData.fail(999, "操作失败");
    }


    @PostMapping("/updatePwd")
    public ResultData<String> updatePwd(HttpServletRequest request , @RequestParam  String oldPwd,@RequestParam  String newPwd) {

        //针对Pwd进行密码效验

        Integer uid = (Integer) request.getSession().getAttribute("uid");
        User user = userService.getOne(new QueryWrapper<User>().eq("user_id", uid));
        if (user.getPassword().equals(oldPwd)) {
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            boolean IsUpdated = userService.update(updateWrapper.eq("user_id", uid).set("password", newPwd));
            if (IsUpdated)
                return ResultData.success("修改成功");
        }
        return ResultData.fail(999, "操作失败");
    }

    @GetMapping("getAvator/{userId}")
    public  ResultData<String> getAvator(HttpServletResponse response , @PathVariable Integer userId) {
        //通过拼接目录和用户id,文件后缀 得到文件路径，  以文件流的方式 返回给前端， 如果没有就返回默认头像

        return  ResultData.success("获取头像成功");
    }

    @PostMapping("updateAvator")
    public  ResultData<String> updateAvator(HttpServletRequest request , @RequestParam MultipartFile avator ){


        return   ResultData.success("修改头像成功");
    }


    private  void  printNoDefaultImage ( HttpServletResponse response){
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.println("请在头像目录下放置默认头像default_avatar.jpg");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("输出默认头像失败");
        }

    }

 private String getMacAddress(HttpServletRequest request) {
        String macAddress = null;
        try {
            InetAddress ipAddress = InetAddress.getByName(request.getRemoteAddr());
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ipAddress);
            byte[] macBytes = networkInterface.getHardwareAddress();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < macBytes.length; i++) {
                stringBuilder.append(String.format("%02X%s", macBytes[i], (i < macBytes.length - 1) ? "-" : ""));
            }
            macAddress = stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }



}