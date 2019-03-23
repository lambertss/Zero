package com.wuyue.factory;

import com.wuyue.Util.SpringBeanTools;
import com.wuyue.Util.StringUtil;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public class MapperFactory {

    public Mapper getMapperFromContainer(String domainName) {
        String s = StringUtil.lowerFirstChar(domainName);
        return (Mapper) SpringBeanTools.getBeanByName(s+"Mapper");
    }


}
