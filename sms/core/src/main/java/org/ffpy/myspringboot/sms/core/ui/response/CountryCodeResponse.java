package org.ffpy.myspringboot.sms.core.ui.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 国家区号Response对象
 *
 * @author wenlongsheng
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryCodeResponse {

    @ApiModelProperty(value = "国家名称", example = "中国")
    private String country;

    @ApiModelProperty(value = "区号", example = "86")
    private String code;
}
