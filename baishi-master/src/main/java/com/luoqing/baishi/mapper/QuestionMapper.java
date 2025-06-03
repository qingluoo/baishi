package com.luoqing.baishi.mapper;

import com.luoqing.baishi.model.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
* @author qingluo
* @description 针对表【question(题目)】的数据库操作Mapper
* @createDate 2025-05-25 15:57:25
* @Entity com.luoqing.baishi.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 查询题目列表，包括被删除的题目
     * @param minUpdateTime 最早更新时间
     * @return 题目列表
     */
    @Select("SELECT * FROM question WHERE updateTime >= #{minUpdateTime}")
    List<Question> listQuestionWithDelete(Date minUpdateTime);
}




