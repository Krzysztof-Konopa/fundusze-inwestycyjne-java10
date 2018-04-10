package com.investment.services.impl;

import com.strategy.daos.StrategyDAO;
import com.investment.dtos.InvestmentDTO;
import com.investment.models.Fund;
import com.investment.models.Investment;
import com.investment.models.Result;
import com.investment.services.InvestmentService;
import com.strategy.models.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InvestmentServiceImpl implements InvestmentService {

    private static final int PERCENT_SCALE = 2;
    private static final int CASH_SCALE = 0;

    @Autowired
    private StrategyDAO strategyDAO;

    @Override
    @Transactional
    public Investment compute(InvestmentDTO investmentDTO) {

        var cash = investmentDTO.getCash();
        var remainCash = investmentDTO.getCash();
        var funds = investmentDTO.getFunds();
        var strategyName = investmentDTO.getStrategyName();
        Strategy strategy = strategyDAO.getByName(strategyName);

        List<Result> allResults = new ArrayList<>();

        for(var entry : strategy.getVolumesMap().entrySet()) {
            var fundsPerType = funds.stream()
                    .filter(fund -> fund.getType().equals(entry.getKey()))
                    .collect(Collectors.toList());

            var results = getResultsFromFunds(entry.getValue(), fundsPerType, cash, remainCash);
            remainCash -= sumInvestedCash(results);

            allResults = Stream.of(allResults, results)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }

        allResults.sort(Comparator.comparing(result -> result.getFund().getId()));
        return new Investment(allResults, remainCash);
    }

    private List<Result> getResultsFromFunds(String percent, List<Fund> funds, long cash, long remainCash) {
        var fundPercent = new BigDecimal(percent);
        var overallCash = new BigDecimal(cash);
        var fundCount = funds.size();
        List<Result> results = new ArrayList<>();

        for(Fund fund : funds) {
            var percentPerFund = fundPercent.divide(new BigDecimal(fundCount), PERCENT_SCALE, RoundingMode.FLOOR);
            var cashPerFund = percentPerFund.multiply(overallCash).divide(new BigDecimal("100"), CASH_SCALE, RoundingMode.FLOOR);
            remainCash -= cashPerFund.longValue();

            if(remainCash < 0) {
                cashPerFund = BigDecimal.ZERO;
            }

            results.add(new Result(fund, cashPerFund.toString(), percentPerFund.toString()));

            fundCount--;
            fundPercent = fundPercent.subtract(percentPerFund);
        }
        return results;
    }

    private long sumInvestedCash(List<Result> results) {
        return results.stream()
                .mapToLong(result -> Long.valueOf(result.getCashValue()))
                .sum();
    }
}
