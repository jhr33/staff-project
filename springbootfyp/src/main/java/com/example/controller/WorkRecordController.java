package com.example.controller;

// 导入 Hutool 工具包的集合工具类，用于集合相关操作
import cn.hutool.core.collection.CollectionUtil;
// 导入 Hutool 工具包的 IO 工具类，用于 IO 操作
import cn.hutool.core.io.IoUtil;
// 导入 Hutool 工具包的 Excel 工具类，用于 Excel 操作
import cn.hutool.poi.excel.ExcelUtil;
// 导入 Hutool 工具包的 Excel 写入器类，用于写入 Excel 文件
import cn.hutool.poi.excel.ExcelWriter;
// 导入自定义的通用返回结果类
import com.example.common.Result;
// 导入工作记录实体类
import com.example.entity.WorkRecord;
// 导入公告服务类
import com.example.service.NoticeService;
// 导入工作记录服务类
import com.example.service.WorkRecordService;
// 导入 PageHelper 的分页信息类，用于封装分页查询结果
import com.github.pagehelper.PageInfo;
// 导入 Jakarta 注解的资源注入注解
import jakarta.annotation.Resource;
// 导入 Jakarta Servlet 的输出流类，用于响应输出
import jakarta.servlet.ServletOutputStream;
// 导入 Jakarta Servlet 的 HTTP 响应类，用于处理 HTTP 响应
import jakarta.servlet.http.HttpServletResponse;
// 导入 Spring MVC 的请求映射注解，用于定义 RESTful 接口
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
// 导入 Java 集合框架的 ArrayList 类，用于创建动态数组
import java.util.ArrayList;
// 导入 Java 集合框架的 LinkedHashMap 类，用于创建有序的键值对集合
import java.util.LinkedHashMap;
// 导入 Java 集合框架的 List 接口，用于表示列表
import java.util.List;
// 导入 Java 集合框架的 Map 接口，用于表示键值对集合
import java.util.Map;

/**
 * 描述: 任务审核相关接口
 */
@RestController
@RequestMapping("/workrecord")
public class WorkRecordController {

    // 注入工作记录服务类的实例
    @Resource
    WorkRecordService workrecordService;

    /**
     * 新增工作记录
     * @param workrecord 要新增的工作记录对象，通过请求体传入
     * @return 通用返回结果，表示操作成功
     */


    public Result add(@RequestBody WorkRecord workrecord) {
        // 调用工作记录服务类的新增方法
        workrecordService.add(workrecord);
        // 返回操作成功的结果
        return Result.success();
    }

    /**
     * 根据 ID 删除单个工作记录
     * @param id 要删除的工作记录的 ID，通过路径变量传入
     * @return 通用返回结果，表示操作成功
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        // 调用工作记录服务类的根据 ID 删除方法
        workrecordService.deleteById(id);
        // 返回操作成功的结果
        return Result.success();
    }

    /**
     * 批量删除工作记录
     * @param ids 要删除的工作记录的 ID 列表，通过请求体传入
     * @return 通用返回结果，表示操作成功
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        // 调用工作记录服务类的批量删除方法
        workrecordService.deleteBatch(ids);
        // 返回操作成功的结果
        return Result.success();
    }

    /**
     * 更新工作记录信息
     * @param workRecord 包含更新信息的工作记录对象，通过请求体传入
     * @return 通用返回结果，表示操作成功
     */
    @PutMapping("/update")
    public Result update(@RequestBody WorkRecord workRecord) {
        // 调用工作记录服务类的根据 ID 更新方法
        workrecordService.updateById(workRecord);
        // 返回操作成功的结果
        return Result.success();
    }

    /**
     * 根据 ID 查询单个工作记录
     * @param id 要查询的工作记录的 ID，通过路径变量传入
     * @return 通用返回结果，包含查询到的工作记录对象
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        // 调用工作记录服务类的根据 ID 查询方法
        WorkRecord workrecord = workrecordService.selectById(id);
        // 返回操作成功的结果，并携带查询到的工作记录对象
        return Result.success(workrecord);
    }

    /**
     * 查询所有符合条件的工作记录
     * @param workrecord 包含查询条件的工作记录对象，通过请求参数传入
     * @return 通用返回结果，包含查询到的工作记录列表
     */
    @GetMapping("/selectAll")
    public Result selectAll(WorkRecord workrecord) {
        // 调用工作记录服务类的查询所有方法
        List<WorkRecord> list = workrecordService.selectAll(workrecord);
        // 返回操作成功的结果，并携带查询到的工作记录列表
        return Result.success(list);
    }

    /**
     * 分页查询工作记录
     * @param workrecord 包含查询条件的工作记录对象，通过请求参数传入
     * @param pageNum 页码，默认值为 1
     * @param pageSize 每页显示的记录数，默认值为 10
     * @return 通用返回结果，包含分页查询结果的 PageInfo 对象
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            WorkRecord workrecord,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        // 调用工作记录服务类的分页查询方法
        PageInfo<WorkRecord> pageInfo = workrecordService.selectPage(workrecord, pageNum, pageSize);
        // 返回操作成功的结果，并携带分页查询结果的 PageInfo 对象
        return Result.success(pageInfo);
    }

    /**
     * 描述：批量删除
     * @param list 要删除的工作记录的 ID 列表，通过请求体传入
     * @return 通用返回结果，表示操作成功
     */
    @PutMapping("/batchDel")
    public Result deleteBatch(@RequestBody List<Integer> list) {
        // 遍历 ID 列表，逐个调用工作记录服务类的根据 ID 删除方法
        for (Integer id : list) {
            workrecordService.deleteById(id);
        }
        // 返回操作成功的结果
        return Result.success();
    }

    /**
     * 描述：批量导出工作记录到 Excel 文件
     * @param response HTTP 响应对象，用于返回 Excel 文件
     * @throws IOException 当发生 IO 异常时抛出
     */
    // @GetMapping("/export")
    // public void export(HttpServletResponse response) throws IOException {
    //     // 查询所有工作记录
    //     List<WorkRecord> all = workrecordService.selectAll(new WorkRecord());
    //     // 创建一个与工作记录列表大小相同的列表，用于存储转换后的 Map 数据
    //     List<Map<String, Object>> list = new ArrayList<>(all.size());
    //     // 判断工作记录列表是否为空
    //     if (CollectionUtil.isEmpty(all)) {
    //         // 若为空，创建一个包含空标题和正文的 Map 并添加到列表中
    //         Map<String, Object> row = new LinkedHashMap<>();
    //         row.put("标题", null);
    //         row.put("正文", null);
    //         list.add(row);
    //     } else {
    //         // 若不为空，遍历工作记录列表，将每条记录转换为包含标题和正文的 Map 并添加到列表中
    //         for (WorkRecord workrecord : all) {
    //             Map<String, Object> row = new LinkedHashMap<>();
    //             row.put("标题", workrecord.getTitle());
    //             row.put("正文", workrecord.getText());
    //             list.add(row);
    //         }
    //     }
    //     // 创建一个 Excel 写入器
    //     ExcelWriter writer = ExcelUtil.getWriter(true);
    //     // 将列表数据写入 Excel 文件
    //     writer.write(list, true);
    //     // 设置响应的内容类型为 Excel 文件类型
    //     response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
    //     // 设置响应头，指定文件名
    //     response.setHeader("Content-Disposition","attachment;filename=workrecordInfoExcel.xlsx");
    //     // 获取响应的输出流
    //     ServletOutputStream out = response.getOutputStream();
    //     // 将 Excel 数据刷新到输出流
    //     writer.flush(out, true);
    //     // 关闭 Excel 写入器
    //     writer.close();
    //     // 关闭系统输出流
    //     IoUtil.close(System.out);
    // }
}
