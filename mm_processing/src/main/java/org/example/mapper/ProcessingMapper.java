package org.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.po.Processing;
import org.springframework.transaction.annotation.Transactional;

@Mapper

public interface ProcessingMapper extends BaseMapper<Processing> {

}