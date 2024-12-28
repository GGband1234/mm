package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分页查询Dto")
public class PageQueryDto {
    @Schema(description = "项目经理")
    private String name;
    @Schema(description = "页码")
    private Integer page;
    @Schema(description = "每页显示记录数")
    private Integer pagesize;

}
