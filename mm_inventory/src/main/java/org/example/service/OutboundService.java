package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.dto.OutboundDto;
import org.example.domain.dto.PageQueryDto;
import org.example.domain.po.Outbound;
import org.example.result.PageResult;

import java.util.List;

public interface OutboundService extends IService<Outbound> {

    PageResult pageQuery(PageQueryDto pageQueryDto);
}
