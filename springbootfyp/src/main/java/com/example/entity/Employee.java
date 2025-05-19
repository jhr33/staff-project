package com.example.entity;

import cn.hutool.core.annotation.Alias;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 员工
 */
@Data
@TableName("employee")
public class Employee extends Account {

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
	/** 账号 */
	@Alias("账号")
	private String username;
	/** 头像 */
	private String avatar;
	/** 密码 */
	private String password;
	/** 名称 */
	@Alias("名称")
	private String name;
	/** 年龄 */
	@Alias("年龄")
	private Integer age;
	/** 性别 */
	@Alias("性别")
	private String sex;
	/** 职业技能 */
	@Alias("职业技能")
	private String skill;
	/** 手机 */
	@Alias("手机")
	private String phone;
	/** 邮箱 */
	@Alias("邮箱")
	private String email;
	/** 职业技能 */
	@TableField(exist = false)
	private List<String> skillList;



}
