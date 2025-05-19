package com.example.controller;

import com.example.common.config.TokenUtils;
import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageInfo;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.*;
import com.example.service.*;
import com.example.exception.CustomException;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.*;

import java.util.List;

/**
*  描述：任务管理相关接口
*/
@RestController
@RequestMapping("/taskManagement")
public class TaskManagementController {

    @Resource
    TaskManagementService taskManagementService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody TaskManagement taskManagement) {
		List<String> ddlSlot = taskManagement.getDdlSlot();
		if (CollectionUtil.isNotEmpty(ddlSlot)) {
			taskManagement.setDdl(ddlSlot.get(0) + " ~ " + ddlSlot.get(1));
		}

        taskManagementService.add(taskManagement);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        taskManagementService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        taskManagementService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody TaskManagement taskManagement) {
		List<String> ddlSlot = taskManagement.getDdlSlot();
		if (CollectionUtil.isNotEmpty(ddlSlot)) {
			taskManagement.setDdl(ddlSlot.get(0) + " ~ " + ddlSlot.get(1));
		}

        taskManagementService.updateById(taskManagement);
        return Result.success();
    }

    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        TaskManagement taskManagement = taskManagementService.selectById(id);
        return Result.success(taskManagement);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(TaskManagement taskManagement) {
        List<TaskManagement> list = taskManagementService.selectAll(taskManagement);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            TaskManagement taskManagement,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<TaskManagement> pageInfo = taskManagementService.selectPage(taskManagement, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 描述：批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			taskManagementService.deleteById(id);
		}
		return Result.success();
	}

	/**
	 * 描述：批量导出到excel
	 */
	@GetMapping("/export")
	public void export(HttpServletResponse response) throws IOException {
		List<TaskManagement> all = taskManagementService.selectAll(new TaskManagement());
		List<Map<String, Object>> list = new ArrayList<>(all.size());
		if (CollectionUtil.isEmpty(all)) {
			Map<String, Object> row = new LinkedHashMap<>();
			row.put("任务名", null);
			row.put("任务总量", null);
			row.put("交付时限", null);
			row.put("任务信息", null);
			list.add(row);
		} else {
			for (TaskManagement taskManagement : all) {
				Map<String, Object> row = new LinkedHashMap<>();
				row.put("任务名", taskManagement.getTaskName());
				row.put("任务总量", taskManagement.getTaskNumber());
				row.put("交付时限", taskManagement.getDdl());
				row.put("任务信息", taskManagement.getTaskInformation());
				list.add(row);
			}
		}
		ExcelWriter writer = ExcelUtil.getWriter(true);
		writer.write(list, true);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
		response.setHeader("Content-Disposition","attachment;filename=taskManagementInfoExcel.xlsx");
		ServletOutputStream out = response.getOutputStream();
		writer.flush(out, true);
		writer.close();
		IoUtil.close(System.out);
	}


}
