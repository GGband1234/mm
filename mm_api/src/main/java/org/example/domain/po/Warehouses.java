package org.example.domain.po;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("warehouses")
public class Warehouses {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mm-warehouses.warehouse_id
     *
     * @mbggenerated Sat Nov 23 14:51:32 CST 2024
     */
    @TableId("warehouse_id")
    private Integer warehouseId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mm-warehouses.warehouse_name
     *
     * @mbggenerated Sat Nov 23 14:51:32 CST 2024
     */
    private String warehouseName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mm-warehouses.location
     *
     * @mbggenerated Sat Nov 23 14:51:32 CST 2024
     */
    private String location;

}