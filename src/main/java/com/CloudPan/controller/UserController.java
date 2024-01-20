package com.CloudPan.controller;


import com.CloudPan.controller.utils.ResultData;
import com.CloudPan.controller.utils.ReturnCodeEnum;
import com.CloudPan.entity.User;
import com.CloudPan.service.impl.UserServiceImpl;
import com.CloudPan.utils.JwtUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.NetworkInterface;
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
    public ResultData<String> register(@RequestBody User user ,HttpServletRequest request) {
        //先查数据库中是否由该对象，没有该对象就添加
        String macAddress = getMacAddress(request);
        user.setMacAddress(macAddress);
        boolean IsAdded = userService.save(user);
        if (IsAdded)
            return ResultData.success("注册成功");
        return ResultData.fail(999, "操作失败");
    }
    @GetMapping("/login")
    public ResultData<User> login(@RequestParam("username") String username, @RequestParam("password") String password) {

        User user = userService.login(username, password);
        if (user != null){
            String token = JwtUtils.generateToken();
            user.setToken(token);
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