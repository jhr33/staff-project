package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.WorkRecord;

import java.util.List;

public interface WorkRecordMapper extends BaseMapper<WorkRecord> {

    /**
      * 查询所有
    */
    List<WorkRecord> selectAll(WorkRecord workrecord);
    /**
     * 根据工作记录的 ID 更新数据库中的工作记录信息。
     * 该方法会将传入的 WorkRecord 对象中的非空字段更新到数据库中对应 ID 的记录里。
     *
     * @param workRecord 包含要更新信息的工作记录对象，其中的 id 属性用于定位要更新的记录。
     * @return 返回更新操作影响的记录行数，更新成功返回 1，未找到对应记录或更新失败返回 0。
     */
    int updateById(WorkRecord workRecord);

    @Override
    int insert(WorkRecord workRecord);

    /**
      * 根据ID查询
    */
    WorkRecord selectById(Integer id);

    /**
      * 删除
    */
    int deleteById(Integer id);



}