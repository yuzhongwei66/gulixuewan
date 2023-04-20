package com.atguigu.eduservice.controller;



import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-02-24
 */
@Api(tags = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {

    //访问地址： http://localhost:8001/eduservice/teacher/findAll
    //把service注入
    @Autowired
    private EduTeacherService teacherService;
//查询讲师表所有数据
    @GetMapping("findAll")
    public R findAllTeacher() {
        //调用service方法

        LambdaQueryWrapper<List> queryWrapper = new LambdaQueryWrapper<>();

        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }
    //删除讲师
    @DeleteMapping("{id}")
    public R removeTeacher(@PathVariable String id) {
        boolean flag=teacherService.removeById(id);
        if (flag) {
            return R.ok();
        }
        else {
      return R.error();
    }}
    //分页查询讲师的方法
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable Long current,
                             @PathVariable Long limit)
    {
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        teacherService.page(pageTeacher,null);
        Long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        return R.ok().data("total",total).data("rows",records);

    }
    @PostMapping("pageTeacherCondition/{current}/{limit}")
public R pageTeacherCondition(@PathVariable Long  current,
                              @PathVariable Long limit,
                       @RequestBody(required = false)  TeacherQuery teacherQuery)
    {
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        LambdaQueryWrapper<EduTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty((teacherQuery.getName())),EduTeacher::getName,teacherQuery.getName());
        queryWrapper.eq(!StringUtils.isEmpty(teacherQuery.getLevel()),EduTeacher::getLevel,teacherQuery.getLevel());
        queryWrapper.ge(StringUtils.hasText((teacherQuery.getBegin())),EduTeacher::getGmtCreate,teacherQuery.getBegin());
        queryWrapper.le(StringUtils.hasText(teacherQuery.getEnd()),EduTeacher::getGmtCreate,teacherQuery.getEnd());
        teacherService.page(pageTeacher,queryWrapper);
        Long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher)
    {
        boolean save = teacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        }
        else {
           return R.error();
        }
    }
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id)
    {
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("teacher", eduTeacher);
    }
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher)
    {
        boolean b = teacherService.updateById(eduTeacher);
        if (b) {
            return R.ok();
        }
     else {
         return R.error();
     }
    }
}

