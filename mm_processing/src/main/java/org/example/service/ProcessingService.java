package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.seata.spring.annotation.GlobalTransactional;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.dto.ProcessingDto;
import org.example.domain.po.Processing;
import org.example.result.PageResult;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProcessingService extends IService<Processing> {


    void saveProcessing(ProcessingDto processingDto);


    PageResult pageQuery(PageQueryDto pageQueryDto);
}
