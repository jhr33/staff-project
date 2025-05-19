package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.config.TokenUtils;
import com.example.mapper.*;
import com.example.entity.*;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 新增
     */
    public void add(Notice notice) {
        noticeMapper.insert(notice);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        noticeMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            noticeMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Notice notice) {
        noticeMapper.updateById(notice);
    }

    /**
     * 根据ID查询
     */
    public Notice selectById(Integer id) {
        return noticeMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Notice> selectAll(Notice notice) {
        return noticeMapper.selectAll(notice);
    }

    /**
     * 分页查询
     */
    public PageInfo<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Notice> list = this.selectAll(notice);

        return PageInfo.of(list);
    }

}