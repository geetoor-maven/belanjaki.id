package com.belanjaki.id.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {

    private T data;
    private Meta meta;


    public Object getCustomizeResponse(String key) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", Map.of(key, data));
        result.put("meta", meta);
        return result;
    }

}
