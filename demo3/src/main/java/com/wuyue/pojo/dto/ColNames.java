package com.wuyue.pojo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;

@Getter
@Setter
public class ColNames {
    @Transient
    private String[] colnames;
}
