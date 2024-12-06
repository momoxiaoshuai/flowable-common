package com.mxs.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("WORK_FLOW_ORDER")
public class WorkFlowOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    //"工单号")
    private String orderId;

    //"流程定义key")
    private String orderType;

    //"工单所属主体的ID")
    private Integer subjectId;

    //"工单所属主体的类型")
    private String subjectType;

    //"申请人(用户ID)")
    private String applicant;

    //"申请时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date applicationTime;

    //"工单摘要/审批摘要")
    private String orderSummary;

    //"工单状态")
    private String orderStatus;

    //"提工单原因")
    private String reason;

    //"工单可变字段数据,json文本存储")
    private String formData;

    //"当前流程节点名称")
    private String handler;

    //"备注")
    private String remark;

    //"创建人员")
    private String createUser;

    //"创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //"更新人员")
    private String updateUser;

    //"更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private Map<String, Object> fieldInfo;
    @TableField(exist = false)
    private Map<String, Object> fieldValue;


}
