package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.po.Projects;
import org.example.mapper.ProjectsMapper;
import org.example.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectsMapper, Projects> implements ProjectService {
    @Autowired
    ProjectsMapper projectsMapper;
    @Override
    public Projects findByProjectName(String projectName) {
        return projectsMapper.findByProjectName(projectName);
    }

    @Override
    public void addProject(Projects projects) {
        projectsMapper.addProject(projects);
    }
}
