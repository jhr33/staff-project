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
public class WagesService {

    @Resource
    private WagesMapper wagesMapper;

    /**
     * 新增
     */
    public void add(Wages wages) {
        wagesMapper.insert(wages);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        wagesMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            wagesMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Wages wages) {
        wagesMapper.updateById(wages);
    }

    /**
     * 根据ID查询
     */
    public Wages selectById(Integer id) {
        return wagesMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Wages> selectAll(Wages wages) {
        return wagesMapper.selectAll(wages);
    }

    /**
     * 分页查询
     */
    public PageInfo<Wages> selectPage(Wages wages, Integer pageNum, Integer pageSize) {
		Account currentUser = TokenUtils.getCurrentUser();
		if ("employee".equals(currentUser.getRole())) {
			wages.setEmployeeId(currentUser.getId());
		}

        PageHelper.startPage(pageNum, pageSize);
        List<Wages> list = this.selectAll(wages);

        return PageInfo.of(list);
    }

}