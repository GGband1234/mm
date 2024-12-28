package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.dto.ProvidedMaterialDto;
import org.example.domain.po.ProvidedMaterial;

import org.example.domain.vo.ProvidedMaterialVo;
import org.example.result.PageResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProvidedMaterialService extends IService<ProvidedMaterial> {
    @Transactional
    void saveOrUpdateProvidedMaterial(ProvidedMaterialDto providedMaterialDto);

    PageResult pageQuery(PageQueryDto pageQueryDto);

    List<ProvidedMaterialVo> logicList();
    @Transactional
    void logicRemoveByIds(List<Integer> providedMaterialIds);
}
