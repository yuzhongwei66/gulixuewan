package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.Subject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-04-20
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
private EduSubjectService service;
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {
        try {
            InputStream in = file.getInputStream();
            EasyExcel.read(in,SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
        }    catch(Exception e){}
    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //1.查询出所有的一级分类
        LambdaQueryWrapper<EduSubject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EduSubject::getParentId,0);
        List<EduSubject> onesubjectList = baseMapper.selectList(queryWrapper);
        //2.查询出所有的二级分类
        LambdaQueryWrapper<EduSubject> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.ne(EduSubject::getParentId,0);
        List<EduSubject> twosubjectList = baseMapper.selectList(queryWrapper2);
        //创建list集合，用于存储最终封装数据
        List<OneSubject> finalList =new ArrayList<>();
        //3.封装一级分类
        //onesubjectList.stream().map()
        for (EduSubject eduSubject : onesubjectList) {
            OneSubject oneSubject=new OneSubject();
      //      oneSubject.setId(eduSubject.getId());
     //       oneSubject.setTitle(eduSubject.getTitle());
            BeanUtils.copyProperties(eduSubject, oneSubject);
            List<TwoSubject> two = new ArrayList<>();
            for (EduSubject eduSubject2 : twosubjectList) {
                TwoSubject twosubject = new TwoSubject();
                BeanUtils.copyProperties(eduSubject2, twosubject);
                if(eduSubject2.getParentId().equals(oneSubject.getId()))
                { two.add(twosubject);}
            }
            oneSubject.setChildren(two);
            finalList.add(oneSubject);
        }
        //4.封装二级分类
        return finalList;
    }
}

