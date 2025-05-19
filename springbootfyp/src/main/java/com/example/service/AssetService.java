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
public class AssetService {

    @Resource
    private AssetMapper assetMapper;

    /**
     * 新增
     */
    public void add(Asset asset) {
        assetMapper.insert(asset);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        assetMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            assetMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Asset asset) {
        assetMapper.updateById(asset);
    }

    /**
     * 根据ID查询
     */
    public Asset selectById(Integer id) {
        return assetMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Asset> selectAll(Asset asset) {
        return assetMapper.selectAll(asset);
    }

    /**
     * 分页查询
     */
    public PageInfo<Asset> selectPage(Asset asset, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Asset> list = this.selectAll(asset);

        return PageInfo.of(list);
    }

}