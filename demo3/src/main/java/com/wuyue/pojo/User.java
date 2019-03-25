package com.wuyue.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuyue.Util.DateUtil;
import com.wuyue.annotation.SqlWhere;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="tb_user")
@ApiModel(value="用户表")
@Getter
@Setter
@ToString
public class User extends BasePojo implements Serializable {
    @ApiModelProperty(value="昵称")
    @SqlWhere(value = SqlWhere.SqlWhereValue.LIKE,proprtityName = "username")
    private String username;
    @ApiModelProperty(value="密码")
    private String password;
    @ApiModelProperty(value="手机号码")
    private String phone;
    @ApiModelProperty(value="邮箱")
    private String email;
    @ApiModelProperty(value="创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private String sourceType;
    private String name;
    private String headPic;
    private String qq;
    private Long accountBalance;
    private String sex;
    private Integer userLevel;
    private Integer points;
    private Integer experienceValue;
    private Date birthday;
    private String addressId;
    private String token;
    private String lastLoginIp;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format=DateUtil.DEFAULT_TIME_FORMAT)
    private Date createTime;
    private Integer creatorId;

    private Integer delStatus;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date lastLoginDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format= DateUtil.DEFAULT_TIME_FORMAT)
    private Date lastUpdateDate;
    @ApiModelProperty(value="最后操作人id")
    private String operatorId;
    @ApiModelProperty(value="最后操作人姓名")
    private String operator;

}