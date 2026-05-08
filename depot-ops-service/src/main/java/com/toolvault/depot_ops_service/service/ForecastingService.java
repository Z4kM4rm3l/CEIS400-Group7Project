package com.toolvault.depot_ops_service.service;

import com.toolvault.depot_ops_service.domain.CheckoutHistory;
import com.toolvault.depot_ops_service.repository.CheckoutHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForecastingService {

    private final CheckoutHistoryRepository history;

    public Map<String, Long> getCheckoutFrequency() {
        Instant thirtyDaysAgo = Instant.now().minus(30, ChronoUnit.DAYS);

        List<CheckoutHistory> recentCheckouts = history
                .findByActionAndTimestampAfter("CHECK_OUT", thirtyDaysAgo);

        return recentCheckouts.stream()
                .filter(h -> h.getModel() != null)
                .collect(Collectors.groupingBy(
                        CheckoutHistory::getModel,
                        Collectors.counting()
                ));
    }

    public List<String> getLowStockRisk() {
        Instant thirtyDaysAgo = Instant.now().minus(30, ChronoUnit.DAYS);

        List<CheckoutHistory> recentHistory = history
                .findByActionAndTimestampAfter("CHECK_OUT", thirtyDaysAgo);

        Map<String, Long> checkouts = recentHistory.stream()
                .filter(h -> h.getModel() != null)
                .collect(Collectors.groupingBy(
                        CheckoutHistory::getModel,
                        Collectors.counting()
                ));

        List<CheckoutHistory> recentCheckIns = history
                .findByActionAndTimestampAfter("CHECK_IN", thirtyDaysAgo);

        Map<String, Long> checkIns = recentCheckIns.stream()
                .filter(h -> h.getModel() != null)
                .collect(Collectors.groupingBy(
                        CheckoutHistory::getModel,
                        Collectors.counting()
                ));

        return checkouts.entrySet().stream()
                .filter(entry -> {
                    long ins = checkIns.getOrDefault(entry.getKey(), 0L);
                    return entry.getValue() > ins * 2;
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getReorderSuggestions() {
        Map<String, Long> frequency = getCheckoutFrequency();
        List<String> atRisk = getLowStockRisk();

        return frequency.entrySet().stream()
                .filter(entry -> atRisk.contains(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() * 2
                ));
    }

}