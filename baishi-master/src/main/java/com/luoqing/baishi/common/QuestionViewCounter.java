package com.luoqing.baishi.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 题目访问计数工具类（线程安全）
 */
public class QuestionViewCounter {
    // 私有化构造器，防止实例化
    private QuestionViewCounter() {}

    // 线程安全的计数器存储结构
    private static final ConcurrentHashMap<Long, AtomicLong> COUNTER_MAP = new ConcurrentHashMap<>();

    // 计数器操作锁，确保复合操作的线程安全
    private static final Object COUNTER_LOCK = new Object();

    /**
     * 记录题目访问（自增计数）
     * @param questionId 题目ID
     * @return 增加后的计数值
     */
    public static long increment(Long questionId) {
        // 双重检查锁确保线程安全
        if (!COUNTER_MAP.containsKey(questionId)) {
            synchronized (COUNTER_LOCK) {
                COUNTER_MAP.putIfAbsent(questionId, new AtomicLong(0));
            }
        }
        return COUNTER_MAP.get(questionId).incrementAndGet();
    }

    /**
     * 获取当前计数（不清零）
     * @param questionId 题目ID
     * @return 当前计数值，如果不存在返回0
     */
    public static long getCount(Long questionId) {
        AtomicLong counter = COUNTER_MAP.get(questionId);
        return counter == null ? 0 : counter.get();
    }

    /**
     * 获取并重置计数
     * @param questionId 题目ID
     * @return 重置前的计数值，如果不存在返回0
     */
    public static long getAndReset(Long questionId) {
        AtomicLong counter = COUNTER_MAP.get(questionId);
        if (counter == null) {
            return 0;
        }
        return counter.getAndSet(0);
    }

    /**
     * 获取所有计数快照（不清零）
     * @return 题目ID到计数的映射
     */
    public static Map<Long, Long> getAllCounts() {
        Map<Long, Long> snapshot = new ConcurrentHashMap<>();
        COUNTER_MAP.forEach((id, counter) -> {
            snapshot.put(id, counter.get());
        });
        return snapshot;
    }

    /**
     * 获取并重置所有计数
     * @return 重置前的完整计数快照
     */
    public static Map<Long, Long> getAllAndReset() {
        Map<Long, Long> snapshot = new ConcurrentHashMap<>();
        COUNTER_MAP.forEach((id, counter) -> {
            snapshot.put(id, counter.getAndSet(0));
        });
        return snapshot;
    }

    /**
     * 重置所有计数器
     */
    public static void resetAll() {
        COUNTER_MAP.forEach((id, counter) -> {
            counter.set(0);
        });
    }
}
