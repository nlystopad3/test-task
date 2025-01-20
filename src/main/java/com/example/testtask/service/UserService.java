package com.example.testtask.service;

import com.example.testtask.config.DatabaseProperties;
import com.example.testtask.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class UserService {

    private final Map<String, JdbcTemplate> jdbcTemplates;
    private final DatabaseProperties properties;

    public List<UserDTO> aggregateUsers(String username, String name, String surname) {
        return properties.getDataSources()
                .stream()
                .flatMap(dataSourceConfig ->
                {
                    StringBuilder sql = new StringBuilder("SELECT " +
                            dataSourceConfig.getMapping().get("id") + " AS id, " +
                            dataSourceConfig.getMapping().get("username") + " AS username," +
                            dataSourceConfig.getMapping().get("name") + " AS name, " +
                            dataSourceConfig.getMapping().get("surname") + " AS surname " +
                            "FROM " + dataSourceConfig.getTable() + " WHERE 1=1"
                    );

                    List<Object> params = new ArrayList<>();

                    if (username != null && !username.isEmpty()) {
                        sql.append(" AND ").append(dataSourceConfig.getMapping().get("username")).append(" = ?");
                        params.add(username);
                    }
                    if (name != null && !name.isEmpty()) {
                        sql.append(" AND ").append(dataSourceConfig.getMapping().get("name")).append(" = ?");
                        params.add(name);
                    }
                    if (surname != null && !surname.isEmpty()) {
                        sql.append(" AND ").append(dataSourceConfig.getMapping().get("surname")).append(" = ?");
                        params.add(surname);
                    }
                    System.out.println(dataSourceConfig.getUrl());
                    Stream<UserDTO> stream = jdbcTemplates.get(dataSourceConfig.getName()).query(sql.toString(), params.toArray(), (rs, rowNum) -> new UserDTO(
                            rs.getString("id"),
                            rs.getString("username"),
                            rs.getString("name"),
                            rs.getString("surname")
                    )).stream();
                    System.out.println(stream);
                    return stream;
                })
                .collect(Collectors.toList());
    }

}
