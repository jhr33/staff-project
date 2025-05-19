package com.example.service;

import com.example.common.config.TokenUtils;
import com.example.entity.Account;
import com.example.entity.OverWork;
import com.example.mapper.OverWorkMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverWorkService {

    @Resource
    private OverWorkMapper overworkMapper;

    /**
     * 新增
     */
    public void add(OverWork overwork) {

        overworkMapper.insert(overwork);
       // Account currentUser= TokenUtils.getCurrentUser();

        //overwork.setTime(DateUtil.now());
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        overworkMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            overworkMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(OverWork overwork) {
        if (overwork.getId() == null) {
            throw new RuntimeException("ID不能为空");
        }

        // 检查记录是否存在
        OverWork existing = overworkMapper.selectById(overwork.getId());
        if (existing == null) {
            throw new RuntimeException("记录不存在");
        }

        // 只更新允许修改的字段
        OverWork updateRecord = new OverWork();
        updateRecord.setId(overwork.getId());
        if (overwork.getTitle() != null) {
            updateRecord.setTitle(overwork.getTitle());
        }
      
        if (overwork.getStatus() != null) {
            updateRecord.setStatus(overwork.getStatus());
        }
        if (overwork.getReason() != null) {
            updateRecord.setReason(overwork.getReason());
        }

        int affectedRows = overworkMapper.updateById(updateRecord);
        if (affectedRows == 0) {
            throw new RuntimeException("更新失败，记录可能不存在");
        }
        if (overwork.getId() == null) {
            throw new RuntimeException("ID不能为空");
        }

        // 创建一个新的对象，只更新允许修改的字段

    }

    /**
     * 根据ID查询
     */
    public OverWork selectById(Integer id) {
        return overworkMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<OverWork> selectAll(OverWork overwork) {
        return overworkMapper.selectAll(overwork);
    }

    /**
     * 分页查询
     */
    public PageInfo<OverWork> selectPage(OverWork overwork, Integer pageNum, Integer pageSize) {
Account currentUser=TokenUtils.getCurrentUser();
if ("USER".equals(currentUser.getRole())){
    overwork.setUserid(currentUser.getId());
    overwork.setEmname(currentUser.getUsername());
}
        if ("employee".equals(currentUser.getRole())) {
            // 员工只能查看自己的记录
           overwork.setUserid(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<OverWork> list = this.selectAll(overwork);


        return PageInfo.of(list);
    }

}