package com.luoqing.baishi.job.cycle;

import com.luoqing.baishi.common.QuestionViewCounter;
import com.luoqing.baishi.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSortedSet;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;
import javax.annotation.Resource;
import java.util.Map;

/**
 * 题目计数更新
 *
 */
// todo 取消注释开启任务
@Component
@Slf4j
public class IncSyncCountToRedis {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 60*1000)
    public void run() {
        // 获取并重置所有计数
        Map<Long, Long> snapshot = QuestionViewCounter.getAllAndReset();
        if(snapshot.isEmpty())
        {
            log.info("没有需要更新的计数");
            return;
        }

        // 批量更新到Redis
        String key = RedisConstant.QUESTION_BROWSE_REDIS_KEY;
        RScoredSortedSet<Long> scoredSortedSet = redissonClient.getScoredSortedSet(key);
        try {
            snapshot.forEach(scoredSortedSet::addScore);
        } catch (Exception e) {
            log.error("更新Redis排行失败", e);
        }

    }
}
