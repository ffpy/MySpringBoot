package org.ffpy.myspringboot.sms.core.ui.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@ApiModel
@Data
public class CountryCodeResponse {

    @ApiModelProperty(value = "国家名称", position = 1, example = "中国")
    private String country;

    @ApiModelProperty(value = "区号", position = 2, example = "86")
    private String code;

    public CountryCodeResponse(String country, String code) {
        if (!code.matches("\\d+")) {
            throw new IllegalArgumentException("区号必须为纯数字");
        }
        if (StringUtils.isEmpty(country)) {
            throw new IllegalArgumentException("国家名称不能为空");
        }

        this.country = country;
        this.code = code;
    }
}
