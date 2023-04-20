package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

public class SubjectExcelListener  extends AnalysisEventListener<SubjectData> {
    public EduSubjectService subjectService;
 public SubjectExcelListener() {

 }
 public SubjectExcelListener(EduSubjectService subjectService)
 {
     this.subjectService = subjectService;
 }

    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if(subjectData ==null)
        {
            throw new GuliException(20001,"文件数据为空");
        }
        EduSubject existOneSubject = this.existOneSubject(subjectService, subjectData.getOneSubjectName());
        if (existOneSubject == null) {
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectData.getOneSubjectName());
            subjectService.save(existOneSubject);
        }
  String pid =existOneSubject.getId();
        EduSubject existTwoSubject = this.existTwoSubject(subjectService, subjectData.getTwoSubjectName(), pid);
        if(existTwoSubject == null)
        {
            existTwoSubject = new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());
            subjectService.save(existTwoSubject);
        }
    }
    //判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService subjectService,String name){
     LambdaQueryWrapper<EduSubject> queryWrapper = new LambdaQueryWrapper();
     queryWrapper.eq(EduSubject::getTitle,name);
     queryWrapper.eq(EduSubject::getParentId,"0");
        EduSubject one = subjectService.getOne(queryWrapper);
        return one;
    }
    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid){
        LambdaQueryWrapper<EduSubject> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(EduSubject::getTitle,name);
        queryWrapper.eq(EduSubject::getParentId,pid);
        EduSubject two = subjectService.getOne(queryWrapper);
        return two;
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
