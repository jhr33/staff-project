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
*  描述：员工相关接口
*/
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    EmployeeService employeeService;

    /**
     * 新增
        */
	@PostMapping("/add")
    public Result add(@RequestBody Employee employee) {
		List<String> skillList = employee.getSkillList();
		if (CollectionUtil.isNotEmpty(skillList)) {
			StringBuilder builder = new StringBuilder();
			for (String s : skillList) {
				builder.append(s).append(",");
			}
			employee.setSkill(builder.substring(0, builder.length() - 1));
		}

        employeeService.add(employee);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        employeeService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        employeeService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody Employee employee) {
		List<String> skillList = employee.getSkillList();
		if (CollectionUtil.isNotEmpty(skillList)) {
			StringBuilder builder = new StringBuilder();
			for (String s : skillList) {
				builder.append(s).append(",");
			}
			employee.setSkill(builder.substring(0, builder.length() - 1));
		}

        employeeService.updateById(employee);
        return Result.success();
    }

    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Employee employee) {
        List<Employee> list = employeeService.selectAll(employee);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            Employee employee,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Employee> pageInfo = employeeService.selectPage(employee, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 描述：批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			employeeService.deleteById(id);
		}
		return Result.success();
	}

	/**
	 * 描述：批量导出到excel
	 */
	@GetMapping("/export")
	public void export(HttpServletResponse response) throws IOException {
		List<Employee> all = employeeService.selectAll(new Employee());
		List<Map<String, Object>> list = new ArrayList<>(all.size());
		if (CollectionUtil.isEmpty(all)) {
			Map<String, Object> row = new LinkedHashMap<>();
			row.put("账号", null);
			row.put("名称", null);
			row.put("年龄", null);
			row.put("性别", null);
			row.put("职业技能", null);
			row.put("手机", null);
			row.put("邮箱", null);
			list.add(row);
		} else {
			for (Employee employee : all) {
				Map<String, Object> row = new LinkedHashMap<>();
				row.put("账号", employee.getUsername());
				row.put("名称", employee.getName());
				row.put("年龄", employee.getAge());
				row.put("性别", employee.getSex());
				row.put("职业技能", employee.getSkill());
				row.put("手机", employee.getPhone());
				row.put("邮箱", employee.getEmail());
				list.add(row);
			}
		}
		ExcelWriter writer = ExcelUtil.getWriter(true);
		writer.write(list, true);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
		response.setHeader("Content-Disposition","attachment;filename=employeeInfoExcel.xlsx");
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
		List<Employee> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(Employee.class);
		if (!CollectionUtil.isEmpty(infoList)) {
			for (Employee info : infoList) {
				try {
					employeeService.add(info);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return Result.success();
	}

}
