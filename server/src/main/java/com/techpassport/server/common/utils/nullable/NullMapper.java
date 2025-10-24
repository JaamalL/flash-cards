package com.techpassport.server.common.utils.nullable;

import org.springframework.stereotype.Service;

@Service
public class NullMapper implements INullMapper
{
    @Override
    public <T, R> R mapOrElse(T value, R ifNull, R ifNotNull) {
        return value == null ? ifNull : ifNotNull;
    }
}
