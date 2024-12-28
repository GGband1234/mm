package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.dto.BudgetsDto;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.po.Budgets;
import org.example.domain.vo.BudgetsVo;
import org.example.result.PageResult;
import org.springframework.transaction.annotation.Transactional;

public interface BudgetService extends IService<Budgets> {
    @Transactional
     void addBudget(BudgetsDto budgetsDto) ;


    PageResult pageQuery(PageQueryDto pageQueryDto);

    void removeByProjectId(Integer projectId);

    BudgetsVo getBudgetInfoById(Integer budgetId);

    void updateBudgetInfo(BudgetsDto budgetsDto);
    Budgets getBudgetByProjectId(Integer projectId);
}
