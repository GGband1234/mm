package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.po.Purchases;

@Mapper
public interface PurchasesMapper extends BaseMapper<Purchases> {

}