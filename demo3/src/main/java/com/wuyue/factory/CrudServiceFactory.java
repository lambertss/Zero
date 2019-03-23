package com.wuyue.factory;

import com.wuyue.Util.SpringBeanTools;
import com.wuyue.Util.StringUtil;
import com.wuyue.service.CrudService;
import org.springframework.stereotype.Component;

/**
 * 说明： 基础增删改查
 */
@Component
public class CrudServiceFactory {

    public CrudService createService(String domainName) {
        String s = StringUtil.lowerFirstChar(domainName);
        return (CrudService) SpringBeanTools.getBeanByName(s+"Service");
    }
}
