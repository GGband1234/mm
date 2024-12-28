package org.example.domain.vo;


import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class InventorysVo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column inventorys.inventory_id
     *
     * @mbggenerated Sat Nov 30 15:38:13 CST 2024
     */

    @TableId("inventory_id")
    private Integer inventoryId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column inventorys.material_name
     *
     * @mbggenerated Sat Nov 30 15:38:13 CST 2024
     */
    private String materialName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column inventorys.warehouse_id
     *
     * @mbggenerated Sat Nov 30 15:38:13 CST 2024
     */
    private String warehouseName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column inventorys.project_id
     *
     * @mbggenerated Sat Nov 30 15:38:13 CST 2024
     */
    private String projectName;




    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column inventorys.quantity
     *
     * @mbggenerated Sat Nov 30 15:38:13 CST 2024
     */
    private BigInteger quantity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column inventorys.unit
     *
     * @mbggenerated Sat Nov 30 15:38:13 CST 2024
     */
    private String unit;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column inventorys.min_quantity
     *
     * @mbggenerated Sat Nov 30 15:38:13 CST 2024
     */
    private BigInteger minQuantity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column inventorys.max_quantity
     *
     * @mbggenerated Sat Nov 30 15:38:13 CST 2024
     */
    private BigInteger maxQuantity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column inventorys.create_time
     *
     * @mbggenerated Sat Nov 30 15:38:13 CST 2024
     */

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column inventorys.update_time
     *
     * @mbggenerated Sat Nov 30 15:38:13 CST 2024
     */

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}