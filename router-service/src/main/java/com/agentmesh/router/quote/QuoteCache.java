package com.agentmesh.router.quote;

import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class QuoteCache {

    private static final Logger log = LoggerFactory.getLogger(QuoteCache.class);

    private final long ttlMillis;
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public QuoteCache(@Value("${agentmesh.quote.cache.ttl-seconds:300}") long ttlSeconds) {
        this.ttlMillis = ttlSeconds * 1000L;
    }

    public static class CacheEntry {
        private final AgentQuoteResponse quote;
        private final long cachedAt;

        public CacheEntry(AgentQuoteResponse quote) {
            this.quote = quote;
            this.cachedAt = System.currentTimeMillis();
        }

        public AgentQuoteResponse getQuote() { return quote; }
        public long getCachedAt() { return cachedAt; }
        public boolean isExpired(long ttlMillis) {
            return (System.currentTimeMillis() - cachedAt) > ttlMillis;
        }
    }

    public void put(String taskId, String agentId, AgentQuoteResponse quote) {
        if (taskId == null || agentId == null || quote == null) return;
        String key = buildKey(taskId, agentId);
        cache.put(key, new CacheEntry(quote));
        log.debug("Cached quote for key: {}", key);
    }

    public Optional<AgentQuoteResponse> get(String taskId, String agentId) {
        if (taskId == null || agentId == null) return Optional.empty();
        String key = buildKey(taskId, agentId);
        CacheEntry entry = cache.get(key);
        if (entry == null) return Optional.empty();

        if (entry.isExpired(ttlMillis)) {
            cache.remove(key);
            log.debug("Evicted expired quote for key: {}", key);
            return Optional.empty();
        }
        return Optional.of(entry.getQuote());
    }

    public List<AgentQuoteResponse> getQuotesForTask(String taskId) {
        if (taskId == null) return Collections.emptyList();
        List<AgentQuoteResponse> validQuotes = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
            if (entry.getKey().startsWith(taskId + ":")) {
                if (entry.getValue().isExpired(ttlMillis)) {
                    cache.remove(entry.getKey());
                } else {
                    validQuotes.add(entry.getValue().getQuote());
                }
            }
        }
        return validQuotes;
    }

    public void evictExpired() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired(ttlMillis));
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }

    private String buildKey(String taskId, String agentId) {
        return taskId + ":" + agentId;
    }
}
