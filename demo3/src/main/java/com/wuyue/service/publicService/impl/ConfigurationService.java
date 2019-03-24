package com.wuyue.service.publicService.impl;

import com.wuyue.pojo.config.Configuration;
import com.wuyue.service.CrudService;
import org.springframework.stereotype.Service;

/**
 * 说明： Configuration的领域服务
 */
@Service
public class ConfigurationService extends CrudService<Configuration> {


    public String doGetValueByCode(String code) {
        Configuration loadOne = new Configuration();
        loadOne.setCode(code);
        loadOne = loadOne(loadOne);
        if(loadOne != null){
            return loadOne.getValue();
        }else{
            return null;
        }
    }

}
