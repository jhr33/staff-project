package com.example.service;

// 导入 Hutool 工具包中的日期工具类，用于处理日期相关操作
import cn.hutool.core.date.DateUtil;
// 导入 Hutool 工具包中的对象工具类，当前文件未使用该类，可考虑移除
import cn.hutool.core.util.ObjectUtil;
// 导入自定义的 Token 工具类，用于获取当前用户信息
import com.example.common.config.TokenUtils;
// 导入 Account 实体类，用于表示用户账户信息
import com.example.entity.Account;
// 导入 Subtasks 实体类，用于表示子任务信息
import com.example.entity.Subtasks;
// 导入 WorkRecord 实体类，用于表示工作记录信息
import com.example.entity.WorkRecord;
// 导入 SubtasksMapper 接口，用于操作子任务相关的数据库操作
import com.example.mapper.SubtasksMapper;
// 导入 WorkRecordMapper 接口，用于操作工作记录相关的数据库操作
import com.example.mapper.WorkRecordMapper;
// 导入 PageHelper 类，用于实现分页查询功能
import com.github.pagehelper.PageHelper;
// 导入 PageInfo 类，用于封装分页查询结果
import com.github.pagehelper.PageInfo;
// 导入 Jakarta 注解中的 Resource 注解，用于依赖注入
import jakarta.annotation.Resource;
// 导入 Spring 注解中的 Service 注解，用于将该类标识为服务层组件
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * WorkRecordService 类，作为服务层组件，负责处理工作记录相关的业务逻辑。
 * 该类依赖 WorkRecordMapper 和 SubtasksMapper 进行数据库操作。
 */
@Service
public class WorkRecordService {
    // 注入 WorkRecordMapper 实例，用于操作工作记录相关的数据库
    @Resource
    WorkRecordMapper workRecordMapper;
    // 注入 SubtasksMapper 实例，用于操作子任务相关的数据库
    @Resource
    SubtasksMapper subtasksMapper;
    // 重复注入 WorkRecordMapper 实例，可考虑移除其中一个
    @Resource
    private WorkRecordMapper workrecordMapper;

    /**
     * 新增工作记录
     * @param workrecord 要新增的工作记录对象
     */
    public void add(WorkRecord workrecord) {
        // 设置工作记录的状态为待审核
        workrecord.setStatus("待审核");
        // 设置工作记录的时间为当前时间
        workrecord.setTime(DateUtil.now());
        // 调用 WorkRecordMapper 的 insert 方法将工作记录插入数据库
        workrecordMapper.insert(workrecord);

        // 以下代码为注释掉的代码，暂不执行
        // subtasks.setProgress(subtasks.getProgress()+subtasks.getNumber());
        // workRecordMapper.insert(workrecord);
        // Subtasks subtasks= subtasksMapper.selectById(workrecord.getTaskid());
        // subtasks.setProgress(subtasks.progress+subtasks.getNumber());
    }

    /**
     * 根据 ID 删除工作记录
     * @param id 要删除的工作记录的 ID
     */
    public void deleteById(Integer id) {
        // 调用 WorkRecordMapper 的 deleteById 方法根据 ID 删除工作记录
        workrecordMapper.deleteById(id);
    }

    /**
     * 批量删除工作记录
     * @param ids 要删除的工作记录的 ID 列表
     */
    public void deleteBatch(List<Integer> ids) {
        // 遍历 ID 列表，逐个删除对应的工作记录
        for (Integer id : ids) {
            workrecordMapper.deleteById(id);
        }
    }

    /**
     * 根据 ID 修改工作记录信息，并在状态变为审核通过时更新关联子任务的进度
     * @param workRecord 包含更新信息的工作记录对象
     */
    public void updateById(WorkRecord workRecord) {
        // 获取数据库中原始的工作记录信息
        WorkRecord dbWorkRecord = workRecordMapper.selectById(workRecord.getId());

        // 调用 WorkRecordMapper 的 updateById 方法更新工作记录信息
        workRecordMapper.updateById(workRecord);

        // 只有当状态变为"审核通过"且原始状态不是"审核通过"时才更新进度
        if ("审核通过".equals(workRecord.getStatus()) &&
                !"审核通过".equals(dbWorkRecord.getStatus())) {
            // 获取关联的子任务信息
            Subtasks subtasks = subtasksMapper.selectById(workRecord.getTaskid());
            if (subtasks != null) {
                // 计算新的进度，假设 number 是完成数量
                Integer newProgress = subtasks.getProgress() + workRecord.getNumber();
                // 设置子任务的新进度
                subtasks.setProgress(newProgress);
                // 调用 SubtasksMapper 的 updateById 方法更新子任务信息
                subtasksMapper.updateById(subtasks);
            }
        }
    }

    /**
     * 根据 ID 查询工作记录
     * @param id 要查询的工作记录的 ID
     * @return 对应 ID 的工作记录对象，如果未找到则返回 null
     */
    public WorkRecord selectById(Integer id) {
        // 调用 WorkRecordMapper 的 selectById 方法根据 ID 查询工作记录
        return workrecordMapper.selectById(id);
    }

    /**
     * 查询所有符合条件的工作记录
     * @param workrecord 包含查询条件的工作记录对象
     * @return 符合条件的工作记录列表
     */
    public List<WorkRecord> selectAll(WorkRecord workrecord) {
        // 调用 WorkRecordMapper 的 selectAll 方法查询所有符合条件的工作记录
        return workrecordMapper.selectAll(workrecord);
    }

    /**
     * 分页查询工作记录
     * @param workrecord 包含查询条件的工作记录对象
     * @param pageNum 页码
     * @param pageSize 每页显示的记录数
     * @return 封装了分页查询结果的 PageInfo 对象
     */
    public PageInfo<WorkRecord> selectPage(WorkRecord workrecord, Integer pageNum, Integer pageSize) {
        // 获取当前用户信息
        Account currentUser = TokenUtils.getCurrentUser();
        // 如果当前用户角色是 USER，则设置查询条件为当前用户的 ID 和用户名
        if ("USER".equals(currentUser.getRole())) {
            workrecord.setUserid(currentUser.getId());
            workrecord.setEname(currentUser.getUsername());
        }
        // 如果当前用户角色是 employee，则设置查询条件为当前用户的 ID
        if ("employee".equals(currentUser.getRole())) {
            // 员工只能查看自己的记录
            workrecord.setUserid(currentUser.getId());
        }
        // 开启分页查询，设置当前页码和每页显示的记录数
        PageHelper.startPage(pageNum, pageSize);
        // 调用 selectAll 方法查询所有符合条件的工作记录
        List<WorkRecord> list = this.selectAll(workrecord);
        // 以下代码为注释掉的代码，暂不执行
        /*
        for (WorkRecord dbworkrecord :list){
            Integer subtasksid=dbworkrecord.getTaskid();
            Subtasks subtasks=subtasksMapper.selectById(subtasksid);
            if (ObjectUtil.isNotEmpty(subtasks)){
                dbworkrecord.setNumber(subtasks.getNumber());
            }
        }
         */
        // 将查询结果封装到 PageInfo 对象中并返回
        return PageInfo.of(list);
    }
}
