package com.wuyue.pojo;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="co_seller")
@Getter
@Setter
public class CoSeller extends BasePojo implements Serializable{
    
	private String phone;
	private String name;
	private String password;
	private String email;
	private Date created;
	private Date lastLoginTime;
	private Long companyId;

}