package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
@Autowired
private VodClient vodClient;
    @Override
    public void removeByCourseId(String courseId) {
        LambdaQueryWrapper<EduVideo> query = new LambdaQueryWrapper<>();
        query.eq(EduVideo::getCourseId,courseId);
        query.select(EduVideo::getVideoSourceId);
        List<EduVideo> eduVideos = baseMapper.selectList(query);
        List<String> Ids =new ArrayList<>();
        for (EduVideo eduVideo : eduVideos) {
            if(!StringUtils.isEmpty(eduVideo.getVideoSourceId()))
            {Ids.add(eduVideo.getVideoSourceId());}
        }
        if(Ids.size()>0)
        { vodClient.deleteBatch(Ids);}
        LambdaQueryWrapper<EduVideo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EduVideo::getCourseId, courseId);
        baseMapper.delete(queryWrapper);
    }
}
