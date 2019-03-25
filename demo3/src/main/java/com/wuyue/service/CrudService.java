package com.wuyue.service;

import com.github.pagehelper.PageHelper;
import com.wuyue.Util.ClassUtil;
import com.wuyue.Util.IdWorker;
import com.wuyue.Util.RedisUtils;
import com.wuyue.annotation.SqlCriteriaFactory;
import com.wuyue.annotation.SqlOrderBy;
import com.wuyue.common.Cons;
import com.wuyue.factory.MapperFactory;
import com.wuyue.pojo.BasePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 说明： 领域服务的基础增删查改
 */
public class CrudService<T extends BasePojo> {

    @Autowired
    private MapperFactory mapperFactory;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private SqlCriteriaFactory sqlCriteriaFactory;

    /**
     * 说明： 依据条件加载一条数据
     */
    private  Integer genId(){
        long l = idWorker.nextId();
        return Integer.valueOf((l+"").substring(12));
    }

    public T create(T t) {
        Mapper<T> mapper = getMapperFromContainer(t);
        if(mapper!=null){
            t.setId(genId());
            int row = mapper.insertSelective(t);
            if (row == 1) {
                return t;
            }
        }
        return null;
    }

    /**
     * 说明： 根据条件增加一批数据
     */
    @Transactional(rollbackFor = Exception.class)
    public int creates(List<T> list) {
        int sum = 0;
        for(T t : list) {
            T t1 = create(t);
            if(t1!=null){
                sum++;
            }
        }
        return sum;
    }

    /**
     * 说明： 依据条件加载一条数据
     */
    public T loadOne(T t) {
        Mapper<T> mapper = getMapperFromContainer(t);
        if(mapper!=null){
            return mapper.selectOne(t);
        }
        return null;
    }

    /**
     * 说明： 加载单条数据
     */
    public T loadById(T t) {
        Mapper<T> mapper = getMapperFromContainer(t);
        if(mapper!=null){
            return mapper.selectByPrimaryKey(t);
        }
        return null;
    }

    /**
     * 说明： 根据主键进行更新
     */
    public T update(T t) {
        Mapper<T> mapper = getMapperFromContainer(t);
        if(mapper!=null){
            int updateRow = mapper.updateByPrimaryKeySelective(t);
            if (updateRow == 1) {
                t = mapper.selectByPrimaryKey(t);
                return t;
            }
        }

        return null;
    }
    public List<T> list(T t){
        return list(t,1,15);
    }

    /**
     * 说明： 加载列表数据前端调用分页
     */
    public List<T> list(T t, Integer page, Integer limit) {
        Mapper<T> mapper = getMapperFromContainer(t);

        //给查询赋值
        Condition condition = new Condition(t.getClass());
        Example.Criteria criteria = condition.createCriteria();
        criteria = sqlCriteriaFactory.GeneraCriteria(criteria,t);
        //排序t
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            SqlOrderBy sqlOrderBy = field.getAnnotation(SqlOrderBy.class);
            if(sqlOrderBy!=null){
                condition.setOrderByClause(sqlOrderBy.value());
            }
        }

        try {
            criteria.andEqualTo(Cons.DelStatus, 0);
        } catch (Exception e) {

        }
        if (page != null && limit != null) {
            if(page==0||limit==0){
                page=1;
                limit=15;
            }
            PageHelper.offsetPage(page * limit, limit);
        }
        return mapper.selectByExample(condition);

    }

    /**
     * 说明： 根据主键设置多条数据删除状态为1
     */
    public int forgeDeleteByIds(T t){
        int deleteSum=0;
        Mapper<T> mapper = getMapperFromContainer(t);
        List<Integer> ids = t.getIds();
        if(ids!=null){
            for (Integer id : ids) {
                t.setId(id);
                Boolean flag = setValue(t, Cons.DelStatus, 1);
                if(flag){
                    int i = mapper.updateByPrimaryKeySelective(t);
                    if(i==1){
                        deleteSum++;
                    }
                }
            }
        }
        return deleteSum;
    }
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(T t)  {
        int deleteSum=0;
        Mapper<T> mapper = getMapperFromContainer(t);
        List<Integer> ids = t.getIds();

        for (Integer id : ids) {
            setValue(t,"Id",id);
            int i = mapper.deleteByPrimaryKey(t);
            if(i==1){
                deleteSum++;
            }
        }
       return deleteSum;
    }


    private static Boolean setValue(Object t,String fieldName,Object o){

        return ClassUtil.setValue(t,fieldName,o);
    }
    private Mapper getMapperFromContainer(T t){
        return mapperFactory.getMapperFromContainer(t.getClass().getSimpleName());
    }


}
