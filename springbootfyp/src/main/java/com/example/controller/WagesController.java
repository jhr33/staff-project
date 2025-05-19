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
*  描述：工资管理相关接口
*/
@RestController
@RequestMapping("/wages")
public class WagesController {

    @Resource
    WagesService wagesService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Wages wages) {
		Account currentUser = TokenUtils.getCurrentUser();
		if (ObjectUtil.isEmpty(currentUser)) {
			throw new CustomException(ResultCodeEnum.USER_NOT_LOGIN);
		}
		if ("employee".equals(currentUser.getRole())) {
			wages.setEmployeeId(currentUser.getId());
		}

        wagesService.add(wages);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        wagesService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        wagesService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody Wages wages) {

        wagesService.updateById(wages);
        return Result.success();
    }

    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Wages wages = wagesService.selectById(id);
        return Result.success(wages);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Wages wages) {
        List<Wages> list = wagesService.selectAll(wages);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            Wages wages,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Wages> pageInfo = wagesService.selectPage(wages, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 描述：批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			wagesService.deleteById(id);
		}
		return Result.success();
	}

	/**
	 * 描述：批量导出到excel
	 */
	@GetMapping("/export")
	public void export(HttpServletResponse response) throws IOException {
		List<Wages> all = wagesService.selectAll(new Wages());
		List<Map<String, Object>> list = new ArrayList<>(all.size());
		if (CollectionUtil.isEmpty(all)) {
			Map<String, Object> row = new LinkedHashMap<>();
			row.put("员工账户", null);
			row.put("员工姓名", null);
			row.put("年月", null);
			row.put("发放时间", null);
			row.put("发放薪资", null);
			row.put("备注说明", null);
			list.add(row);
		} else {
			for (Wages wages : all) {
				Map<String, Object> row = new LinkedHashMap<>();
				row.put("员工账户", wages.getCard());
				row.put("员工姓名", wages.getEmployeeName());
				row.put("年月", wages.getYm());
				row.put("发放时间", wages.getWageTime());
				row.put("发放薪资", wages.getWageNumber());
				row.put("备注说明", wages.getDescription());
				list.add(row);
			}
		}
		ExcelWriter writer = ExcelUtil.getWriter(true);
		writer.write(list, true);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
		response.setHeader("Content-Disposition","attachment;filename=wagesInfoExcel.xlsx");
		ServletOutputStream out = response.getOutputStream();
		writer.flush(out, true);
		writer.close();
		IoUtil.close(System.out);
	}

	/**
	 * 描述：通过excel批量导入
	 */
	@PostMapping("/upload")
	public Result upload(MultipartFile file) throws IOException {
		List<Wages> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(Wages.class);
		if (!CollectionUtil.isEmpty(infoList)) {
			for (Wages info : infoList) {
				try {
					wagesService.add(info);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return Result.success();
	}

}
