package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.domain.dto.SuppliersDto;
import org.example.domain.po.Suppliers;
import org.example.result.Result;
import org.example.service.SuppliersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/suppliers")
@Tag(name = "供应商管理")
public class SuppliersController {
    @Autowired
    private SuppliersService suppliersService;



    @Operation(summary = "添加供应商")
    @PostMapping
    public Result addSupplier(@RequestBody SuppliersDto supplierDto){
        suppliersService.saveSupplier(supplierDto);
        return Result.success();
    }


    @Operation(summary = "删除供应商")
    @DeleteMapping("/{supplierId}")
    public Result delSupplier(@PathVariable("supplierId") Integer supplierId){
        suppliersService.removeById(supplierId);
        return Result.success();
    }

    @Operation(summary = "根据供应商名查询供应商")
    @GetMapping(value = {"/{supplierName}","/"})
    public Result<List<Suppliers>> searchBySupplierName(@PathVariable(value = "supplierName",required = false) String supplierName){
        if (supplierName == null)
            supplierName="";
        List<Suppliers> supplier = suppliersService.listByName(supplierName);
        return Result.success(supplier);
    }

    @Operation(summary = "根据id修改询供应商")
    @PutMapping ()
    public Result updateSupplier(@RequestBody Suppliers suppliers){
        suppliers.setUpdateTime(LocalDateTime.now());
        suppliersService.updateById(suppliers);
        return Result.success();
    }

}
