package com.luoqing.baishi.esdao;

import com.luoqing.baishi.model.dto.question.QuestionEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface QuestionEsDao extends ElasticsearchRepository<QuestionEsDTO, Long> {

    /**
     * 根据用户 id 查询
     * @param userId
     * @return
     */
    List<QuestionEsDTO> findByUserId(Long userId);
}
