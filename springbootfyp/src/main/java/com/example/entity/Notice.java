package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 公告
 */
@Data
@TableName("notice")
public class Notice  {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	/** 标题 */
	private String title;
	/** 正文 */
	private String text;



}
