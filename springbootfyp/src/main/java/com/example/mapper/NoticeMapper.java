package com.example.mapper;

// 导入实体类包，包含所有需要用到的实体类
import com.example.entity.*;
// 导入 MyBatis 的 Param 注解，用于在方法参数前指定参数名，当前文件未使用该注解
import org.apache.ibatis.annotations.Param;
// 导入 MyBatis 的 Select 注解，用于在接口方法上直接编写 SQL 查询语句，当前文件未使用该注解
import org.apache.ibatis.annotations.Select;
// 导入 Spring 的 Repository 注解，用于将数据访问层（DAO 层）的类标识为 Spring 组件，当前文件未使用该注解
import org.springframework.stereotype.Repository;
// 导入 MyBatis-Plus 的 BaseMapper 接口，提供基本的 CRUD 操作
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
// 导入 Java 集合框架中的 List 接口
import java.util.List;

/**
 * NoticeMapper 接口，继承自 BaseMapper<Notice>，用于操作 Notice 实体类相关的数据库操作。
 * 提供了一些自定义的数据库查询和操作方法。
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    /**
     * 查询所有符合条件的公告信息
     * @param notice 包含查询条件的 Notice 对象
     * @return 符合条件的公告信息列表
     */
    List<Notice> selectAll(Notice notice);

    /**
     * 根据公告 ID 查询公告信息
     * @param id 公告的 ID
     * @return 对应 ID 的公告信息，如果未找到则返回 null
     */
    Notice selectById(Integer id);

    /**
     * 根据公告 ID 删除公告信息
     * @param id 公告的 ID
     * @return 删除操作影响的行数，删除成功返回大于 0 的值，失败返回 0
     */
    int deleteById(Integer id);
}
