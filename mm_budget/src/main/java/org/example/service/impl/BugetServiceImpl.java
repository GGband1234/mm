package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.client.UserClient;
import org.example.domain.dto.BudgetsDto;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.po.Budgets;

import org.example.domain.po.Projects;
import org.example.domain.po.User;
import org.example.domain.vo.BudgetsVo;
import org.example.mapper.BudgetsMapper;
import org.example.result.PageResult;
import org.example.service.BudgetService;
import org.example.service.ProjectService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BugetServiceImpl extends ServiceImpl<BudgetsMapper,Budgets> implements BudgetService {

    @Autowired
    UserClient userClient;

    @Autowired
    BudgetsMapper budgetsMapper;

    @Autowired
    ProjectService projectService;
    @Override
    public void addBudget(BudgetsDto budgetsDto) {
        Projects byProjectName = projectService.findByProjectName(budgetsDto.getProjectName());
        if (byProjectName == null){
            addBudgetfunction(budgetsDto);
        }else {
            Budgets budget = getBudgetByProjectId(byProjectName.getProjectId());
            if (budget != null && !Objects.equals(budget.getBudgetId(), budgetsDto.getBudgetId()))
                throw new RuntimeException("预算信息已存在");
            addBudgetfunction(budgetsDto);
        }

    }
    private void addBudgetfunction(BudgetsDto budgetsDto){
        User user = userClient.findByUserName(budgetsDto.getProjectManager());
        if(user == null)
            throw new RuntimeException("项目经理不存在");
        Projects projects = new Projects();
        Budgets budgets= new Budgets();
        BeanUtils.copyProperties(budgetsDto,projects);
        projects.setProjectManager(user.getUserId());
        projectService.save(projects);
        BeanUtils.copyProperties(budgetsDto,budgets);
        budgets.setProjectId(projects.getProjectId());
        budgets.setCreateTime(LocalDateTime.now());
        budgetsMapper.insert(budgets);
    }
    @Override
    public PageResult pageQuery(PageQueryDto pageQueryDto) {
//        List<Integer> ids = new ArrayList<>();
//        List<Integer> ids1 = new ArrayList<>();
//        List<BudgetsVo> budgetsVos = new ArrayList<>();
//        List<User> users = userClient.listByUserName(pageQueryDto.getName());
//        if (users == null || users.isEmpty())
//            throw new RuntimeException("没有匹配信息");
//        for (User user : users) {
//            ids.add(user.getUserId());
//        }
//        Page<Projects> page1 = Page.of(pageQueryDto.getPage(), pageQueryDto.getPagesize());
//        Page<Projects> page2 = projectService.lambdaQuery().in(Projects::getProjectManager, ids).page(page1);
//
//        List<Projects> records = page2.getRecords();
//        for (Projects project : records) {
//            BudgetsVo budgetsVo = new BudgetsVo();
//            BeanUtils.copyProperties(project,budgetsVo);
//            for (User user : users) {
//                if (Objects.equals(user.getUserId(), project.getProjectManager())){
//                    budgetsVo.setUsername(user.getUsername());
//                    break;
//                }
//            }
//            budgetsVos.add(budgetsVo);
//            ids1.add(project.getProjectId());
//        }
//
//        LambdaQueryWrapper<Budgets> userLambdaQueryWrapper3 = new LambdaQueryWrapper<>();
//        userLambdaQueryWrapper3.in(Budgets::getProjectId,ids1);
//        List<Budgets> budgets = budgetsMapper.selectList(userLambdaQueryWrapper3);
//        int i=0;
//        List<BudgetsVo> budgetsVos1 = new ArrayList<>();
//        for (Budgets budget : budgets) {
//            BeanUtils.copyProperties(budget,budgetsVos.get(i));
//            budgetsVos1.add(budgetsVos.get(i));
//            i++;
//        }
//        PageResult pageResult = new PageResult();
//        pageResult.setTotal(page2.getTotal());
//        pageResult.setRecords(budgetsVos1);
//        return pageResult;
        List<User> users = null;
        try {
            users = userClient.listByUserName(pageQueryDto.getName());
        } catch (Exception e) {
            throw new RuntimeException("没有匹配信息" );
        }
        Page<Budgets> pages = Page.of(pageQueryDto.getPage(), pageQueryDto.getPagesize());
        page(pages);
        List<Budgets> records = pages.getRecords();
        List<Integer> projectIds = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        List<BudgetsVo> budgetsVos = new ArrayList<>();
        for (Budgets record : records) {
            projectIds.add(record.getProjectId());
        }
        for (User user : users) {
            userIds.add(user.getUserId());
        }
        List<Projects> projects = projectService.lambdaQuery()
                .in(Projects::getProjectId, projectIds)
                .in(Projects::getProjectManager, userIds)
                .list();
        int i =0;
        for (Projects project : projects) {
            BudgetsVo budgetsVo = new BudgetsVo();
            budgetsVo.setProjectName(project.getProjectName());
            for (User user : users) {
                if (project.getProjectManager().equals(user.getUserId())){
                    budgetsVo.setUsername(user.getUsername());
                    break;
                }
            }
            for (Budgets record : records) {
                if (record.getProjectId().equals(project.getProjectId())){
                    BeanUtils.copyProperties(record,budgetsVo);
                    break;
                }
            }
            budgetsVos.add(budgetsVo);
        }

        return new PageResult((long) projectIds.size(),budgetsVos);


    }



    @Override
    public void removeByProjectId(Integer projectId) {
        budgetsMapper.delectByProjectId(projectId);
    }

    @Override
    public BudgetsVo getBudgetInfoById(Integer budgetId) {
        BudgetsVo budgetsVo = new BudgetsVo();
        Budgets budget = budgetsMapper.selectById(budgetId);
        Projects project = projectService.getById(budget.getProjectId());
        User user = userClient.findByUserId(project.getProjectManager());
        BeanUtils.copyProperties(budget,budgetsVo);
        BeanUtils.copyProperties(project,budgetsVo);
        BeanUtils.copyProperties(user,budgetsVo);
        return budgetsVo;
    }

    @Override
    public void updateBudgetInfo(BudgetsDto budgetsDto) {
        User user = userClient.findByUserName(budgetsDto.getProjectManager());
        if (user == null)
            throw new RuntimeException("项目经理不存在");
        Projects project = projectService.findByProjectName(budgetsDto.getProjectName());
        if(project == null)
            throw new RuntimeException("项目信息不存在");
        Budgets budget = getBudgetByProjectId(project.getProjectId());
        if (budget != null && !Objects.equals(budget.getBudgetId(), budgetsDto.getBudgetId()))
            throw new RuntimeException("预算信息已存在");
        Budgets budget1 = new Budgets();
        BeanUtils.copyProperties(budgetsDto,budget1);
        budget1.setProjectId(project.getProjectId());
        projectService.lambdaUpdate().set(Projects::getProjectManager,user.getUserId())
                        .eq(Projects::getProjectId,project.getProjectId())
                                .update();
        updateById(budget1);
    }

    public Budgets getBudgetByProjectId(Integer projectId) {
        LambdaQueryWrapper<Budgets> budgetsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        budgetsLambdaQueryWrapper.eq(Budgets::getProjectId,projectId);
        return budgetsMapper.selectOne(budgetsLambdaQueryWrapper);
    }
}
