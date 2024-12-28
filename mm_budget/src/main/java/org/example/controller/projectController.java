package org.example.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.domain.po.Projects;
import org.example.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "项目管理")
@RequestMapping("/project")
public class projectController {
    @Autowired

    ProjectService projectService;

    @GetMapping("/findById")
    @Operation(summary = "根据id查询项目信息")
    public Projects findProjectById(@RequestParam("projectId") Integer projectId){
        return projectService.getById(projectId);
    }

    @GetMapping("/findByName")
    @Operation(summary = "根据仓库名查询项目信息")
    public Projects findProjectByName(@RequestParam("projectName") String projectName){
        return projectService.findByProjectName(projectName);
    }
}
