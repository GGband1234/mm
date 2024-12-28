package org.example.client;

import io.swagger.v3.oas.annotations.Operation;
import org.example.config.DefaultFeignConfig;
import org.example.domain.po.Projects;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;


@FeignClient(value = "mm-budget",configuration = DefaultFeignConfig.class)

public interface BudgetClient {
    @GetMapping("/project/findById")
    @Operation(summary = "根据id查询项目信息")
    public Projects findProjectById(@RequestParam("projectId") Integer projectId);

    @GetMapping("/project/findByName")
    @Operation(summary = "根据仓库名查询项目信息")
    public Projects findProjectByName(@RequestParam("projectName") String projectName);

}
