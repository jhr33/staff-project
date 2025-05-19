package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * 任务提交
 */
@Data
@TableName("qjrecord")
public class QJRecord {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	/** 员工姓名 */
	private String ename;
    private Integer userid;

private String status;
private String reason;
private String time;
    private String title;
    private String text;

private String starttime;
private String endtime;
}
