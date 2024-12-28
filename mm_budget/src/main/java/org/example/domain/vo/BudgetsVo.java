package org.example.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BudgetsVo {


    @Schema(description = "预算id")
    private Integer budgetId;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "预算金额")
    private BigDecimal budgetAmount;

    @Schema(description = "项目经理")
    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "项目开始时间")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "项目结束时间")
    private Date endDate;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    @TableField(value = "create_time")
    private LocalDateTime createTime;
}
