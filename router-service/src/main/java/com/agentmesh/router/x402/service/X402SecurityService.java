package com.agentmesh.router.x402.service;

import com.agentmesh.router.x402.dto.X402Challenge;
import com.agentmesh.router.x402.dto.X402PaymentProof;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class X402SecurityService {

    private static final Logger log = LoggerFactory.getLogger(X402SecurityService.class);

    private final Set<String> processedTransactions = ConcurrentHashMap.newKeySet();
    private final Map<String, X402Challenge> activeChallenges = new ConcurrentHashMap<>();

    public void storeChallenge(X402Challenge challenge) {
        if (challenge != null && challenge.getChallengeId() != null) {
            activeChallenges.put(challenge.getChallengeId(), challenge);
        }
    }

    public X402Challenge getChallenge(String challengeId) {
        if (challengeId == null) return null;
        X402Challenge challenge = activeChallenges.get(challengeId);
        if (challenge != null && isChallengeExpired(challenge)) {
            activeChallenges.remove(challengeId);
            return null;
        }
        return challenge;
    }

    public boolean isReplayAttempt(String transactionId) {
        if (transactionId == null || transactionId.isBlank()) return true;
        return processedTransactions.contains(transactionId);
    }

    public void registerProcessedTransaction(String transactionId) {
        if (transactionId != null && !transactionId.isBlank()) {
            processedTransactions.add(transactionId);
            log.info("Registered processed Algorand transaction ID to prevent replay: {}", transactionId);
        }
    }

    public boolean isChallengeExpired(X402Challenge challenge) {
        if (challenge == null || challenge.getExpiresAt() == null) return true;
        return System.currentTimeMillis() > challenge.getExpiresAt();
    }
}
