package com.example.testtask.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "database-properties")
@Validated
@Getter
@Setter
public class DatabaseProperties {

    private List<DataSourceConfig> dataSources;

    public DatabaseProperties(List<DataSourceConfig> dataSources) {
        this.dataSources = dataSources;
    }

    public static class DataSourceConfig {

        @NotEmpty
        private String name;
        private String strategy;
        @NotEmpty
        private String url;
        @NotEmpty
        private String table;
        @NotEmpty
        private String user;
        @NotEmpty
        private String password;
        private Map<String, String> mapping;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStrategy() {
            return strategy;
        }

        public void setStrategy(String strategy) {
            this.strategy = strategy;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Map<String, String> getMapping() {
            return mapping;
        }

        public void setMapping(Map<String, String> mapping) {
            this.mapping = mapping;
        }
    }
}
