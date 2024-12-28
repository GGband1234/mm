package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.po.Projects;

public interface ProjectService extends IService<Projects> {
    Projects findByProjectName(String projectName);
    void addProject(Projects projects);
}
