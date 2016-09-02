package com.yyw.yhyc.order.controller;

import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.order.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangqiang on 2016/8/5.
 */
@Controller
public class LoginController extends BaseJsonController {

    @RequestMapping("/login")
    public ModelAndView login(){
        ModelAndView view = new ModelAndView("login/login");
        return view;
    }

    @RequestMapping("/doLogin")
    public ModelAndView doLogin(UserDto userDto) throws ServletException, IOException {
        super.session.setAttribute("user",userDto);

        ModelAndView view = new ModelAndView("login/success");
        UserDto user = super.getLoginUser();
        view.addObject("user",user);
        return view;
    }
}
