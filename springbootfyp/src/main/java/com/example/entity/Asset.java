package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 资产信息
 */
@Data
@TableName("asset")
public class Asset  {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	/** 设备名称 */
	@TableField("device_name")
	private String deviceName; // 使用更清晰的字段名
	//private String fName;
	/** 价值 */
	private Integer price;
	/** 数量 */
	private Integer number;
	/** 故障情况 */
	private String situation;
	/** 闲置情况 */
	private String freeSit;



}
