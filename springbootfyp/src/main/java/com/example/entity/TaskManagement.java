package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 任务管理
 */
@Data
@TableName("task_management")
public class TaskManagement  {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	/** 任务名 */
	private String taskName;
	/** 任务总量 */
	private Integer taskNumber;
	/** 样衣 */
	private String clothImg;
	/** 交付时限 */
	private String ddl;
	/** 任务信息 */
	private String taskInformation;
	/** 交付时限 */
	@TableField(exist = false)
	private List<String> ddlSlot;



}
