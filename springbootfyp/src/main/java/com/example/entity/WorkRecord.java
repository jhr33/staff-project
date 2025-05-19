package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 任务提交
 */
@Data
@TableName("workrecord")
public class WorkRecord {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	/** 员工id */
	@TableField("user_id")
	private Integer userid;
	/** 任务id */
	@TableField("task_id")
	private Integer taskid;
private String status;
private String reason;
	private String name;
	private String ename;
private Integer number;

	private String time;
}
