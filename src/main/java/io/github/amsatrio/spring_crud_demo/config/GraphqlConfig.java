package io.github.amsatrio.spring_crud_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;


import graphql.scalars.ExtendedScalars;
import io.github.amsatrio.spring_crud_demo.util.GraphQLScalarUtil;

@Configuration
public class GraphqlConfig {
    @Bean
    RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
        .scalar(GraphQLScalarUtil.DATE_TIME)
        .scalar(ExtendedScalars.Object)
        .scalar(ExtendedScalars.GraphQLLong);
    }
}
