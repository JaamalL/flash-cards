package com.techpassport.server.common.utils.nullable;

public interface INullMapper
{
    <T, R> R mapOrElse(T value, R ifNull, R ifNotNull);
}
