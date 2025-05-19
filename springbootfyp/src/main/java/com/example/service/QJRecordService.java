package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.config.TokenUtils;
import com.example.entity.Account;
import com.example.entity.QJRecord;
import com.example.entity.TaskManagement;
import com.example.mapper.QJRecordMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QJRecordService {

    @Resource
    private QJRecordMapper qjrecordMapper;

    /**
     * 新增
     */
    public void add(QJRecord qjrecord) {

        qjrecordMapper.insert(qjrecord);
       // Account currentUser= TokenUtils.getCurrentUser();

        //qjrecord.setTime(DateUtil.now());
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        qjrecordMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            qjrecordMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(QJRecord qjrecord) {
        if (qjrecord.getId() == null) {
            throw new RuntimeException("ID不能为空");
        }

        // 检查记录是否存在
        QJRecord existing = qjrecordMapper.selectById(qjrecord.getId());
        if (existing == null) {
            throw new RuntimeException("记录不存在");
        }

        // 只更新允许修改的字段
        QJRecord updateRecord = new QJRecord();
        updateRecord.setId(qjrecord.getId());
        if (qjrecord.getTitle() != null) {
            updateRecord.setTitle(qjrecord.getTitle());
        }
        if (qjrecord.getText() != null) {
            updateRecord.setText(qjrecord.getText());
        }
        if (qjrecord.getStatus() != null) {
            updateRecord.setStatus(qjrecord.getStatus());
        }
        if (qjrecord.getReason() != null) {
            updateRecord.setReason(qjrecord.getReason());
        }

        int affectedRows = qjrecordMapper.updateById(updateRecord);
        if (affectedRows == 0) {
            throw new RuntimeException("更新失败，记录可能不存在");
        }
        if (qjrecord.getId() == null) {
            throw new RuntimeException("ID不能为空");
        }

        // 创建一个新的对象，只更新允许修改的字段

    }

    /**
     * 根据ID查询
     */
    public QJRecord selectById(Integer id) {
        return qjrecordMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<QJRecord> selectAll(QJRecord qjrecord) {
        return qjrecordMapper.selectAll(qjrecord);
    }

    /**
     * 分页查询
     */
    public PageInfo<QJRecord> selectPage(QJRecord qjrecord, Integer pageNum, Integer pageSize) {
Account currentUser=TokenUtils.getCurrentUser();
if ("USER".equals(currentUser.getRole())){
    qjrecord.setUserid(currentUser.getId());
    qjrecord.setEname(currentUser.getUsername());
}
        if ("employee".equals(currentUser.getRole())) {
            // 员工只能查看自己的记录
            qjrecord.setUserid(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<QJRecord> list = this.selectAll(qjrecord);


        return PageInfo.of(list);
    }

}