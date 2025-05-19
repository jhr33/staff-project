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
public class TaskManagementService {

    @Resource
    private TaskManagementMapper taskManagementMapper;

    /**
     * 新增
     */
    public void add(TaskManagement taskManagement) {
        taskManagementMapper.insert(taskManagement);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        taskManagementMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            taskManagementMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(TaskManagement taskManagement) {
        taskManagementMapper.updateById(taskManagement);
    }

    /**
     * 根据ID查询
     */
    public TaskManagement selectById(Integer id) {
        return taskManagementMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<TaskManagement> selectAll(TaskManagement taskManagement) {
        return taskManagementMapper.selectAll(taskManagement);
    }

    /**
     * 分页查询
     */
    public PageInfo<TaskManagement> selectPage(TaskManagement taskManagement, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<TaskManagement> list = this.selectAll(taskManagement);
		// 处理下时间段
		for (TaskManagement dbTaskManagement : list) {
			List<String> ddlSlot = new ArrayList<>();
			String ddl = dbTaskManagement.getDdl();
			if (ObjectUtil.isNotEmpty(ddl)) {
				String[] split = ddl.split(" ~ ");
				ddlSlot.add(split[0]);
				ddlSlot.add(split[1]);
				}
			dbTaskManagement.setDdlSlot(ddlSlot);
		}

        return PageInfo.of(list);
    }

}