package org.example.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.domain.dto.BudgetsDto;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.vo.BudgetsVo;
import org.example.result.PageResult;
import org.example.result.Result;
import org.example.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budget")
@Tag(name = "预算接口文档")
public class BudgetController {
    @Autowired
    BudgetService budgetService;


    @Operation(summary = "新增项目预算")
    @PostMapping("/addBudget")
    public Result addBudget(@RequestBody BudgetsDto budgetsDto){
        budgetService.addBudget(budgetsDto);
        return Result.success();
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询")
    public Result<PageResult> page(@RequestBody PageQueryDto pageQueryDto){
        PageResult pageResult = budgetService.pageQuery(pageQueryDto);
        return Result.success(pageResult);
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "删除预算信息")
    public Result delect(@PathVariable(value = "projectId") Integer projectId){
        budgetService.removeById(projectId);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "更改预算信息")
    public Result update(@RequestBody BudgetsDto budgetsDto){
        System.out.println(budgetsDto);
        budgetService.updateBudgetInfo(budgetsDto);
        return Result.success();
    }
    @GetMapping("/{budgetId}")
    @Operation(summary = "根据id查询预算信息")
    public Result<BudgetsVo> findByBudgetId(@PathVariable Integer budgetId){
        BudgetsVo budgetsVo = budgetService.getBudgetInfoById(budgetId);
        return Result.success(budgetsVo);
    }
}
