package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.domain.dto.PageQueryDto;
import org.example.result.PageResult;
import org.example.result.Result;
import org.example.service.OutboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "出库管理")
@RequestMapping("/outbound")
public class OutboundController {
    @Autowired
    OutboundService outboundService;

    @PostMapping("/page")
    @Operation(summary = "分页查询出库信息")
    public Result<PageResult> pageQuery(@RequestBody PageQueryDto pageQueryDto){
        PageResult pageResult =  outboundService.pageQuery(pageQueryDto);
        return Result.success(pageResult);
    }


}
