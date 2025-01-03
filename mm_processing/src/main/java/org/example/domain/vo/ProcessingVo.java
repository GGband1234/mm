package org.example.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class ProcessingVo {

    private Integer processingId;
    private String materialName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column processing.need_material_names
     *
     * @mbggenerated Mon Dec 02 20:38:24 CST 2024
     */
    private String needMaterialNames;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column processing.material_ratio
     *
     * @mbggenerated Mon Dec 02 20:38:24 CST 2024
     */
    private String materialRatio;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column processing.warehouse_id
     *
     * @mbggenerated Mon Dec 02 20:38:24 CST 2024
     */
    private String warehouseName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column processing.project_id
     *
     * @mbggenerated Mon Dec 02 20:38:24 CST 2024
     */
    private String projectName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column processing.quantity
     *
     * @mbggenerated Mon Dec 02 20:38:24 CST 2024
     */
    private BigInteger quantity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column processing.unit
     *
     * @mbggenerated Mon Dec 02 20:38:24 CST 2024
     */
    private String unit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
