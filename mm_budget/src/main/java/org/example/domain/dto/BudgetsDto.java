package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "预算管理新增类")
@Data
public class BudgetsDto {
    @Schema(description = "预算id")
    private Integer budgetId;
    @Schema(description = "项目名称")
    private String projectName;
    @Schema(description = "预算金额")
    private BigDecimal budgetAmount;
    @Schema(description = "项目经理")
    private String projectManager;
    @Schema(description = "项目开始日期")
    private Date startDate;
    @Schema(description = "项目结束日期")
    private Date endDate;

}
