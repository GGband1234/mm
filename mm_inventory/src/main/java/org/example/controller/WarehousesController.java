package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.domain.po.Warehouses;
import org.example.result.Result;
import org.example.service.WarehousesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "仓库管理")
@RequestMapping("/warehouses")
public class WarehousesController {
    @Autowired
    WarehousesService warehousesService;
    @PostMapping("/{warehouseName}/{warehouselocation}")
    @Operation(summary = "新增仓库")
    public Result addWarehouse(@PathVariable("warehouseName") String warehouseName, @PathVariable("warehouselocation") String warehouselocation){
        Warehouses one = warehousesService.lambdaQuery().eq(Warehouses::getWarehouseName, warehouseName).one();
        if (one != null)
            return Result.error("仓库信息已存在");
        Warehouses warehouses = new Warehouses();
        warehouses.setWarehouseName(warehouseName);
        warehouses.setLocation(warehouselocation);
        warehousesService.save(warehouses);
        return Result.success();
    }

    @GetMapping
    @Operation(summary = "查询仓库")
    public Result<List<Warehouses>> queryWarehouse(){
        List<Warehouses> list = warehousesService.list();
        return Result.success(list);
    }
    @PutMapping
    @Operation(summary = "修改仓库")
    public Result updateWarehouse(@RequestBody Warehouses warehouses){
        Warehouses one = warehousesService.lambdaQuery().eq(Warehouses::getWarehouseName, warehouses.getWarehouseName()).one();
        if (one != null)
            return Result.error("仓库信息已存在");
        warehousesService.lambdaUpdate()
                .eq(Warehouses::getWarehouseId,warehouses.getWarehouseId())
                .update(warehouses);
        return Result.success();
    }

    @DeleteMapping("/{warehouseId}")
    @Operation(summary = "删除仓库")
    public Result delWarehouse(@PathVariable("warehouseId") String warehouseId){
        warehousesService.removeById(warehouseId);
        return Result.success();
    }

    @GetMapping("/findById")
    @Operation(summary = "根据id查询仓库信息")
    public Warehouses findWarehouseById(@RequestParam("warehouseId") Integer warehouseId){
        return warehousesService.getById(warehouseId);
    }

    @GetMapping("/findByName")
    @Operation(summary = "根据仓库名查询仓库信息")
    public Warehouses findWarehouseByName(@RequestParam("warehouseId") String warehouseName){
        return warehousesService.lambdaQuery().eq(Warehouses::getWarehouseName,warehouseName)
                .one();
    }
}
