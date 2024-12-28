package org.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.client.BudgetClient;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.dto.ProvidedMaterialDto;
import org.example.domain.po.Projects;
import org.example.domain.po.ProvidedMaterial;
import org.example.domain.vo.ProvidedMaterialVo;
import org.example.mapper.ProvidedMaterialMapper;
import org.example.result.PageResult;
import org.example.service.ProvidedMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProvidedMaterialServiceImpl extends ServiceImpl<ProvidedMaterialMapper, ProvidedMaterial> implements ProvidedMaterialService {

    @Autowired
    BudgetClient budgetClient;
    @Override
    public void saveOrUpdateProvidedMaterial(ProvidedMaterialDto providedMaterialDto) {
        Projects byProjectName = budgetClient.findProjectByName(providedMaterialDto.getProjectName());
        if (byProjectName == null)
            throw new RuntimeException("项目名不存在");
        ProvidedMaterial providedMaterial = new ProvidedMaterial();
        BeanUtils.copyProperties(providedMaterialDto,providedMaterial);
        providedMaterial.setProvidedMaterialId(null);
        providedMaterial.setProjectId(byProjectName.getProjectId());
        providedMaterial.setType("甲供");
        providedMaterial.setDeleted(0);
        providedMaterial.setCreateTime(LocalDateTime.now());
        providedMaterial.setUpdateTime(LocalDateTime.now());
        saveOrUpdate(providedMaterial);
    }

    @Override
    public PageResult pageQuery(PageQueryDto pageQueryDto) {
        Page<ProvidedMaterial> pages = Page.of(pageQueryDto.getPage(), pageQueryDto.getPagesize());
        lambdaQuery().like(ProvidedMaterial::getMaterialName,pageQueryDto.getName()).page(pages);
        List<ProvidedMaterial> records = pages.getRecords();
        List<ProvidedMaterialVo> providedMaterialVos = getProvidedMaterialVos(records);

        return new PageResult(pages.getTotal(),providedMaterialVos);
    }



    private List<ProvidedMaterialVo> getProvidedMaterialVos(List<ProvidedMaterial> records){
        List<ProvidedMaterialVo> providedMaterialVoList = new ArrayList<>();
        List<Integer> projectIds = new ArrayList<>();
        for (ProvidedMaterial record : records) {
            ProvidedMaterialVo providedMaterialVo = new ProvidedMaterialVo();
            BeanUtils.copyProperties(record,providedMaterialVo);
            providedMaterialVoList.add(providedMaterialVo);
            projectIds.add(record.getProjectId());
        }
        copyProject(providedMaterialVoList, projectIds);
        return providedMaterialVoList;
    }
    private void copyProject(List<ProvidedMaterialVo> providedMaterialVoList
            ,List<Integer> projectIds)
    {
        List<Projects> projects = new ArrayList<>();
        int i = 0;
        for (Integer projectId : projectIds) {
            Projects byId = budgetClient.findProjectById(projectId);
            projects.add(byId);
        }
        i =0;
        for (ProvidedMaterialVo purchasesDto : providedMaterialVoList) {
            purchasesDto.setProjectName(projects.get(i).getProjectName());
            i++;
        }
    }
    @Override
    public List<ProvidedMaterialVo> logicList() {
        List<ProvidedMaterial> providedMaterialList = lambdaQuery().eq(ProvidedMaterial::getDeleted, 0).list();
        List<ProvidedMaterialVo> providedMaterialVoList = new ArrayList<>();
        List<Integer> projectIds = new ArrayList<>();
        for (ProvidedMaterial providedMaterial : providedMaterialList) {
            projectIds.add(providedMaterial.getProjectId());
            ProvidedMaterialVo providedMaterialVo = new ProvidedMaterialVo();
            BeanUtils.copyProperties(providedMaterial,providedMaterialVo);
            providedMaterialVoList.add(providedMaterialVo);
        }
        copyProject(providedMaterialVoList,projectIds);
        return providedMaterialVoList;
    }

    @Override
    public void logicRemoveByIds(List<Integer> providedMaterialIds) {
        lambdaUpdate().set(ProvidedMaterial::getDeleted,1)
                .in(providedMaterialIds != null,ProvidedMaterial::getProvidedMaterialId,providedMaterialIds)
                .update();
    }

}
