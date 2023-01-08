package com.sqi.beanutil.generator

import java.util.function.Function

data class MappingInfo(
    val interfaceName: String,
    val methodInfo: MethodInfo,
    val withSameName: Boolean,
    val mappings: List<Mapping>
)

data class Mapping(
    val sourceFieldName: String,
    val sourceFieldType: Class<*>? = null,
    val targetFieldName: String,
    val targetFieldType: Class<*>? = null,
    val convertProviderClass: Class<Function<*, *>>,
    val formatClass: Class<Function<*, *>>
)

data class MethodInfo(
    val methodName: String,
    val returnType: Class<*>,
    val parameterInfo: List<ParameterInfo>
)

data class ParameterInfo(
    val name: String,
    val type: String
)