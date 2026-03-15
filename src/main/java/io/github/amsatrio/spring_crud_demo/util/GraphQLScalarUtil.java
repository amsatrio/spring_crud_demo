package io.github.amsatrio.spring_crud_demo.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

public class GraphQLScalarUtil {
    public static final GraphQLScalarType DATE_TIME = GraphQLScalarType.newScalar()
            .name("DateTime")
            .description("Custom scalar for DateTime")
            .coercing(new Coercing<LocalDateTime, String>() {
                @Override
                public String serialize(Object dataFetcherResult) {
                    LocalDateTime localDateTime = null;
                    if(dataFetcherResult instanceof Timestamp){
                        localDateTime = ((Timestamp)dataFetcherResult).toLocalDateTime();
                        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }

                    return "";
                }

                @Override
                public LocalDateTime parseValue(Object input) {
                    return LocalDateTime.parse((String) input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }

                @Override
                public LocalDateTime parseLiteral(Object input) {
                    return LocalDateTime.parse(((StringValue) input).getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
            })
            .build();
}
