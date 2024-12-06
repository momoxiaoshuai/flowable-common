package com.mxs.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("WORK_FLOW_FORM_FIELD")
public class WorkFlowFormField implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    //"流程定义key")
    private String orderType;

    //"字段名称")
    private String fieldName;

    //"字段中文")
    private String fieldCname;

    //"字段类型")
    private String fieldType;

    //默认值
    private String defaultValue;

    //其他信息内容
    private String otherInfo;

    //"序号")
    private Integer fieldOrder;


}
