package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 子任务
 */
@Data
@TableName("subtasks")
public class Subtasks  {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	/** 子任务名 */
	private String subtasksName;
	/** 所属任务 */
	@TableField(exist = false)
	private String belong;
	/** 任务数量 */
	private Integer stNumber;
	private Integer number;
	/** 当前进度 */
	private Integer progress;
	/** 任务管理Id */
	private Integer taskManagementId;



}
