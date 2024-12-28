package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.dto.ProcessingDto;
import org.example.result.PageResult;
import org.example.result.Result;
import org.example.service.ProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "加工管理")
@RequestMapping("/processing")
public class ProcessingController {
    @Autowired
    ProcessingService processingService;

    @PostMapping
    @Operation(summary = "新增加工信息")
    public Result addProcessing(@RequestBody ProcessingDto processingDto){
        processingService.saveProcessing(processingDto);
        return Result.success();
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询加工信息")
    public Result<PageResult> pageQuery(@RequestBody PageQueryDto pageQueryDto){
        PageResult pageResult =  processingService.pageQuery(pageQueryDto);
        return Result.success(pageResult);
    }
}
