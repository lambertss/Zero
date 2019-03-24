package com.wuyue.annotation;

import com.wuyue.Util.ClassUtil;
import com.wuyue.pojo.BasePojo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * 说明：动态根据注解生成sql
 */
@Component
public class SqlCriteriaFactory<T extends BasePojo> {

    public Example.Criteria GeneraCriteria(Example.Criteria criteria, T t) {

        //查询条件
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            String key = field.getName();
            String value = (String)ClassUtil.getFieldValByName(t, key);

            SqlWhere sqlWhere = field.getAnnotation(SqlWhere.class);
            if(value!=null&&sqlWhere!=null){
                String sqlWhereValue = sqlWhere.value().getValue();
                    if(sqlWhereValue.equals(SqlWhere.SqlWhereValue.LIKE.getValue())){
                        value = "'%"+value+"%'";
                    }else if(sqlWhereValue.equals(SqlWhere.SqlWhereValue.IN.getValue())){
                        key = sqlWhere.proprtityName();
                        value = value.replace("[","(");
                        value = value.replace("]",")");
                    }else if(sqlWhereValue.equals(SqlWhere.SqlWhereValue.LTDATE.getValue())){
                        key = "DATE_FORMAT(" + sqlWhere.proprtityName() + ", '%Y-%m-%d')";
                        value = "DATE_FORMAT('"+value+"','%Y-%m-%d')";
                    }else if(sqlWhereValue.equals(SqlWhere.SqlWhereValue.GTDATE.getValue())){
                        key = "DATE_FORMAT(" + sqlWhere.proprtityName() + ", '%Y-%m-%d')";
                        value = "DATE_FORMAT('"+value+"','%Y-%m-%d')";
                    } else{
                        value = "'"+value+"'";
                    }
                    if(key.contains(sqlWhere.value().name())){
                        key = key.replace(sqlWhere.value().name(),"");
                    }

                    criteria.andCondition(key + sqlWhereValue + value);

                    continue;
                //针对createGT等范围查询需要得到对应的字段，如create
            }

            criteria.andEqualTo(key, value);

        }

        return criteria;




    }

}
