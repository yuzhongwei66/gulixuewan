package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin
public class EduLoginController {
    @PostMapping("/login")
    public R login()
    {
        return R.ok().data("token","admin");
    }
    @GetMapping("/info")
    public R info() {
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://picx.zhimg.com/v2-845d11005ce47d0f1f906d5bcacc225c_r.jpg?source=1940ef5c");
    }
}
