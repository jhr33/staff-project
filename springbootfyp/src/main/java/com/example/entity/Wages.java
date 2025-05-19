package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 工资管理
 */
@Data
@TableName("wages")
public class Wages  {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	/** 员工账户 */
	@TableField(exist = false)
	private String card;
	/** 员工姓名 */
	@Alias("员工姓名")
	private String employeeName;
	/** 年月 */
	@Alias("年月")
	private String ym;
	/** 发放时间 */
	@Alias("发放时间")
	private String wageTime;
	/** 发放薪资 */
	@Alias("发放薪资")
	private Double wageNumber;
	/** 备注说明 */
	@Alias("备注说明")
	private String description;
	/** 员工Id */
	private Integer employeeId;



}
