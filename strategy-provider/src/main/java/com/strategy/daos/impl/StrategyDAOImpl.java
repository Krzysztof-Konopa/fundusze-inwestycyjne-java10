package com.strategy.daos.impl;

import com.strategy.daos.StrategyDAO;
import com.strategy.models.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

@Repository
public class StrategyDAOImpl implements StrategyDAO {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Strategy getByName(String name) {
        var strategy = (Strategy) applicationContext.getBean(name);
        StrategyDAO.validateStrategy(strategy);
        return strategy;
    }
}
