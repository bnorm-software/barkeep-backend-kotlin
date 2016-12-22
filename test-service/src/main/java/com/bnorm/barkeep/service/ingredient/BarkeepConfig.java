package com.bnorm.barkeep.service.ingredient;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = "com.bnorm.barkeep.repo.ingredient")
public class BarkeepConfig extends AbstractCassandraConfiguration {

    @Override
    public String getKeyspaceName() {
        return "barkeep";
    }

    @Override
    protected String getContactPoints() {
        return "192.168.99.100";
    }
}