package org.ffpy.myspringboot.sms.core.ui.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 短信分组Response对象
 *
 * @author wenlongsheng
 */
@ApiModel
@Data
@AllArgsConstructor
public class GroupResponse {

    @ApiModelProperty(value = "分组", position = 1, example = "login")
    private String name;

    @ApiModelProperty(value = "描述", position = 2, example = "登录")
    private String desc;
}
