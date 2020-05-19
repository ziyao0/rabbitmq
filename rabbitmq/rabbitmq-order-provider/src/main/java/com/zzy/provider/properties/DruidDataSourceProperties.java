package com.zzy.provider.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/19 10:23
 * @desc
 */
@Data
@ConfigurationProperties("spring.datasource.druid")
public class DruidDataSourceProperties {
    private String username;

    private String password;

    private String jdbcUrl;

    private String driverClassName;

    private Integer initialSize;

    private Integer maxActive;

    private Integer minIdle;

    private long maxWait;

    private boolean poolPreparedStatements;

    private String filters;
}
