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
public class SubtasksService {
    @Resource
    WorkRecordMapper workRecordMapper;

    @Resource
    private SubtasksMapper subtasksMapper;

    /**
     * 新增
     */
    public void add(Subtasks subtasks) {
        subtasksMapper.insert(subtasks);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        subtasksMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            subtasksMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Subtasks subtasks) {

        subtasksMapper.updateById(subtasks);

    }

    /**
     * 根据ID查询
     */
    public Subtasks selectById(Integer id) {
        return subtasksMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Subtasks> selectAll(Subtasks subtasks) {
        return subtasksMapper.selectAll(subtasks);
    }

    /**
     * 分页查询
     */
    public PageInfo<Subtasks> selectPage(Subtasks subtasks, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Subtasks> list = this.selectAll(subtasks);

        return PageInfo.of(list);
    }

}