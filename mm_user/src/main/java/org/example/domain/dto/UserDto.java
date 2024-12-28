package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Schema(description = "修改用户信息Dto")
@Data
public class UserDto {
    @Schema(description = "用户民")
    private String username;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "手机号码")
    private String phoneNumber;
}
