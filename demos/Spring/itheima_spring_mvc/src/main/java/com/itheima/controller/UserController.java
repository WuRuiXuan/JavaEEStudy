package com.itheima.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.domain.User;
import com.itheima.domain.VO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    // 请求地址：http://localhost:8080/user/quick
    @RequestMapping(value = "/quick", method = RequestMethod.GET, params = {"username"})
    public String save() {
        System.out.println("Controller save running...");
        return "success";
    }

    @RequestMapping(value = "/quick2")
    public ModelAndView save2() {
        /**
         * Model: 模型 作用是封装数据
         * View: 视图 作用是展示数据
         */
        ModelAndView modelAndView = new ModelAndView();
        // 设置模型数据
        modelAndView.addObject("username", "itcast");
        // 设置视图名称
        modelAndView.setViewName("success");
        return modelAndView;
    }

    @RequestMapping(value = "/quick3")
    public ModelAndView save3(ModelAndView modelAndView) {
        modelAndView.addObject("username", "itheima");
        modelAndView.setViewName("success");
        return modelAndView;
    }

    @RequestMapping(value = "/quick4")
    public String save4(Model model) {
        model.addAttribute("username", "博学谷");
        return "success";
    }

    // 不常用
    @RequestMapping(value = "/quick5")
    public String save5(HttpServletRequest request) {
        request.setAttribute("username", "酷丁鱼");
        return "success";
    }

    @RequestMapping(value = "/quick6")
    public void save6(HttpServletResponse response) throws IOException {
        response.getWriter().print("hello itcast");
    }

    @RequestMapping(value = "/quick7")
    @ResponseBody // 标注不跳转直接进行数据响应
    public String save7() {
        return "hello itheima";
    }

    @RequestMapping(value = "/quick8")
    @ResponseBody
    public String save8() {
        return "{\"name\": \"zhangsan\", \"age\": 18}";
    }

    @RequestMapping(value = "/quick9")
    @ResponseBody
    public String save9() throws JsonProcessingException {
        User user = new User();
        user.setName("lisi");
        user.setAge(30);
        // 使用json转换工具将对象转成json格式字符串再返回
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        return json;
    }

    @RequestMapping(value = "/quick10")
    @ResponseBody
    // 期望SpringMVC自动将User转换成json格式的字符串
    public User save10() {
        User user = new User();
        user.setName("wangwu");
        user.setAge(32);
        return user;
    }

    @RequestMapping(value = "/quick11")
    @ResponseBody
    // ?name=zhangsan&age=18
    public void save11(String name, int age) {
        System.out.println(name);
        System.out.println(age);
    }

    @RequestMapping(value = "/quick12")
    @ResponseBody
    // ?name=zhangsan&age=18
    public void save12(User user) {
        System.out.println(user);
    }

    @RequestMapping(value = "/quick13")
    @ResponseBody
    // ?strs=aaa&strs=bbb&strs=ccc
    public void save13(String[] strs) {
        System.out.println(Arrays.asList(strs));
    }

    @RequestMapping(value = "/quick14")
    @ResponseBody
    public void save14(VO vo) {
        System.out.println(vo);
    }

    @RequestMapping(value = "/quick15")
    @ResponseBody
    public void save15(@RequestBody List<User> userList) {
        System.out.println(userList);
    }

    @RequestMapping(value = "/quick16")
    @ResponseBody
    public void save16(@RequestParam(value = "username", required = false, defaultValue = "itcast") String name) {
        System.out.println(name);
    }

    @RequestMapping(value = "/quick17/{name}", method = RequestMethod.GET)
    @ResponseBody
    // localhost:8080/user/quick17/zhangsan
    public void save17(@PathVariable(value = "name", required = true) String name) {
        System.out.println(name);
    }

    @RequestMapping(value = "/quick18")
    @ResponseBody
    // localhost:8080/user/quick18?date=2021-3-8
    public void save18(Date date) {
        System.out.println(date);
    }

    @RequestMapping(path = "/quick19")
    @ResponseBody
    // localhost:8080/user/quick19?date=2021-3-8
    public void save19(HttpServletRequest request, HttpServletResponse response, HttpSession session, Date date) {
        System.out.println("request: " + request);
        System.out.println("response: " + response);
        System.out.println("session: " + session);
        System.out.println("date: " + date);
    }

    @RequestMapping(path = "/quick20")
    @ResponseBody
    public void save20(@RequestHeader(value = "User-Agent", required = false) String user_agent) {
        System.out.println(user_agent);
    }

    @RequestMapping(path = "/quick21")
    @ResponseBody
    public void save21(@CookieValue(value = "JSESSIONID") String jsessionId) {
        System.out.println(jsessionId);
    }

    @RequestMapping(path = "/quick22")
    @ResponseBody
    public void save22(String username, MultipartFile uploadFile) throws IOException {
        System.out.println(username);
        System.out.println(uploadFile);
        // 获得上传文件的名称
        String fileName = uploadFile.getOriginalFilename();
        uploadFile.transferTo(new File("C:\\Users\\hp650\\Desktop\\upload\\" + fileName));
    }

    @RequestMapping(path = "/quick23")
    @ResponseBody
    public void save23(String username, MultipartFile[] uploadFiles) throws IOException {
        System.out.println(username);
        for (MultipartFile file : uploadFiles) {
            String fileName = file.getOriginalFilename();
            file.transferTo(new File("C:\\Users\\hp650\\Desktop\\upload\\" + fileName));
        }
    }
}
