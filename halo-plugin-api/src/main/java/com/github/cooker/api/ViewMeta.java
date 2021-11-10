package com.github.cooker.api;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * grant
 * 10/11/2021 3:17 下午
 * 描述：
 */
@Data
@Builder
public class ViewMeta {
    String fileType;
    String fileDir;

    Map<String, Object> params;
}
