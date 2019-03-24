package com.wuyue.mapper;

import com.wuyue.pojo.Table;
import com.wuyue.pojo.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Map;

@Repository
public interface UserMapper extends Mapper<User> {

    Map[] queryColName(Table table);
}
