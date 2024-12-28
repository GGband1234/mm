package org.example.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "展示用户信息VO")
@Data
public class UserVo {
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "权限")
    private String permission;
    @Schema(description = "手机号码")
    private String phoneNumber;
}
