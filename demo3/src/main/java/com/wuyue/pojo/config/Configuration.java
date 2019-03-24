package com.wuyue.pojo.config;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuyue.pojo.BasePojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * 说明： 系统通用配置实体类
 */
@ApiModel(value="配置表")
@Table(name="ts_configuration")
@Getter
@Setter
public class Configuration extends BasePojo{

    /**
     * 登录超时，单位秒
     */
    public static final String LOGIN_EXPIRETIME = "LOGIN_EXPIRETIME";

    /**
     * 验证码超时， 单位秒
     */
    public static final String CODE_EXPIRETIME = "CODE_EXPIRETIME";

    /**
     * 验证码长度
     */
    public static final String CODE_LENGTH = "CODE_LENGTH";

    @ApiModelProperty(value="配置数据编码")
    @Column(name="code")
    private String code;
    /**
     *配置数据值
     **/
    @ApiModelProperty(value="配置数据值")
    @Column(name="value")
    private String value;
    /**
     *备注信息
     **/
    @ApiModelProperty(value="备注信息")
    @Column(name="note")
    private String note;
    /**
     *排序字段
     **/
    @ApiModelProperty(value="排序字段")
    @Column(name="index_order")
    private Integer indexOrder;
    /**
     *删除状态 0正常 1删除
     **/
    @ApiModelProperty(value="删除状态 0正常 1删除")
    @Column(name="del_status")
    private Integer delStatus;
    /**
     *创建人id
     **/
    @ApiModelProperty(value="创建人id")
    @Column(name="creater_id")
    private String createrId;
    /**
     *创建人姓名
     **/
    @ApiModelProperty(value="创建人姓名")
    @Column(name="creater")
    private String creater;
    /**
     *创建时间
     **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")@Column(name="create_date")
    private Date createDate;
    /**
     *最后操作人id
     **/
    @ApiModelProperty(value="最后操作人id")
    @Column(name="operator_id")
    private String operatorId;
    /**
     *最后操作人姓名
     **/
    @ApiModelProperty(value="最后操作人姓名")
    @Column(name="operator")
    private String operator;
    /**
     *最后操作时间
     **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")@Column(name="operate_date")
    private Date operateDate;

}
