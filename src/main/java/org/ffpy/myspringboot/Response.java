package org.ffpy.myspringboot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenlongsheng
 * @date 2020/2/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private int code;
    private T data;
    private String message;
}
