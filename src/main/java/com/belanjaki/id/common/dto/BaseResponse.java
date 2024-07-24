package com.belanjaki.id.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class BaseResponse<T> {

    private T data;
    private Meta meta;

    public BaseResponse() {
    }

    public Object getCustomizeResponse(String key) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("meta", meta);
        result.put("data", Map.of(key, data));
        return result;
    }

}
