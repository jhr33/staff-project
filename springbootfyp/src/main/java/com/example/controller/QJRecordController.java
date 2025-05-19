package com.example.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.example.common.Result;
import com.example.common.config.TokenUtils;
import com.example.entity.Account;
import com.example.entity.QJRecord;
import com.example.service.QJRecordService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.common.config.JwtInterceptor.log;

/**
*  描述:请假相关接口
*/
@RestController
@RequestMapping("/qjrecord")
public class QJRecordController {

    @Resource
    QJRecordService qjrecordService;

    /**
     * 新增

    @PostMapping("/add")
    public Result add(@RequestBody QJRecord qjrecord) {
        List<String> timeSlot = qjrecord.getTimeSlot();
        if (CollectionUtil.isNotEmpty(timeSlot)) {
            qjrecord.setTime(timeSlot.get(0) + " ~ " + timeSlot.get(1));
        }
        qjrecordService.add(qjrecord);
        return Result.success();
    }


    /**
     * 删除
     */
    @PostMapping("/add")
    public Result add(@RequestBody QJRecord qjrecord) {
        try {
            qjrecordService.add(qjrecord);
            return Result.success("请假申请提交成功");
        } catch (Exception e) {
            return Result.error("提交失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result update(@RequestBody QJRecord qjrecord) {
        try {
            qjrecordService.updateById(qjrecord);
            return Result.success("请假信息更新成功");
        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        qjrecordService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        qjrecordService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新
     */
    /*@PutMapping("/update")
    public Result update(@RequestBody QJRecord qjrecord) {

        qjrecordService.updateById(qjrecord);
        return Result.success();
    }

     */
  /*  @PutMapping("/update")
    public Result update(@RequestBody QJRecord qjrecord) {
        try {
            if (qjrecord.getId() == null) {
                return Result.error("400", "ID不能为空");
            }

            QJRecord existing = qjrecordService.selectById(qjrecord.getId());
            if (existing == null) {
                return Result.error("404", "记录不存在");
            }

            Account currentUser = TokenUtils.getCurrentUser();

            // 权限验证
            if (!"admin".equals(currentUser.getRole()) ){
                if (!currentUser.getId().equals(existing.getUserid())) {
                    return Result.error("403", "无权限操作");
                }
                // 员工只能编辑"待审核"的记录
                if (!"待审核".equals(existing.getStatus())) {
                    return Result.error("400", "只能编辑待审核的记录");
                }
            }

            qjrecordService.updateById(qjrecord);
            return Result.success("更新成功");
        } catch (Exception e) {
            Result log = null;
            log.error("更新请假记录失败", String.valueOf(e));
            return Result.error("500", "更新失败: " + e.getMessage());
        }
    }
    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        QJRecord qjrecord = qjrecordService.selectById(id);
        return Result.success(qjrecord);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(QJRecord qjrecord) {
        List<QJRecord> list = qjrecordService.selectAll(qjrecord);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            QJRecord qjrecord,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<QJRecord> pageInfo = qjrecordService.selectPage(qjrecord, pageNum, pageSize);
        return Result.success(pageInfo);
    }

	/**
	 * 描述：批量删除
	 */
	@PutMapping("/batchDel")
	public Result deleteBatch(@RequestBody List<Integer> list) {
		for (Integer id : list) {
			qjrecordService.deleteById(id);
		}
		return Result.success();
	}

	/**
	 * 描述：批量导出到excel

	@GetMapping("/export")
	public void export(HttpServletResponse response) throws IOException {
		List<QJRecord> all = qjrecordService.selectAll(new QJRecord());
		List<Map<String, Object>> list = new ArrayList<>(all.size());
		if (CollectionUtil.isEmpty(all)) {
			Map<String, Object> row = new LinkedHashMap<>();
			row.put("标题", null);
			row.put("正文", null);
			list.add(row);
		} else {
			for (QJRecord qjrecord : all) {
				Map<String, Object> row = new LinkedHashMap<>();
				row.put("标题", qjrecord.getTitle());
				row.put("正文", qjrecord.getText());
				list.add(row);
			}
		}
		ExcelWriter writer = ExcelUtil.getWriter(true);
		writer.write(list, true);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
		response.setHeader("Content-Disposition","attachment;filename=qjrecordInfoExcel.xlsx");
		ServletOutputStream out = response.getOutputStream();
		writer.flush(out, true);
		writer.close();
		IoUtil.close(System.out);
	}*/



}
