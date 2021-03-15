package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.res.DicResDTO;
import com.zte.msg.alarmcenter.entity.Dic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/21 10:29
 */
@Mapper
public interface DicMapper extends BaseMapper<Dic> {

    /**
     * 分页查询字典类型
     *
     * @param page
     * @param name
     * @param type
     * @param isEnable
     * @return
     */
    @Select({
            "<script>",
            "SELECT " +
                    " id, " +
                    " `name`, " +
                    " type, " +
                    " is_enable, " +
                    " description, " +
                    " updated_at  " +
                    "FROM " +
                    " `dic`",
            "<where>",
            "<if test=\"name != '' and name != null\">",
            " AND `name` LIKE CONCAT('%',#{name}, '%') ",
            "</if>",
            "<if test = \"type != '' and type != null\">",
            " AND type LIKE CONCAT('%',#{type}, '%') ",
            "</if>",
            "<if test = \"is_enable != null\">",
            " AND is_enable = #{is_enable}",
            "</if>",
            "</where>",
            " ORDER BY updated_at DESC",
            "</script>"
    })
    Page<DicResDTO> selectDicByPage(Page<DicResDTO> page,
                                    @Param("name") String name,
                                    @Param("type") String type,
                                    @Param("is_enable") Integer isEnable);


}
