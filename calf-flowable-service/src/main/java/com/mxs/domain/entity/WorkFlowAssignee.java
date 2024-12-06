package com.mxs.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mxs.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("WORK_FLOW_ASSIGNEE")
public class WorkFlowAssignee implements Serializable {

    private static final long serialVersionUID = -7835007305589191964L;

    /*** 自增ID */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*** 工单号. */
    //@ApiModelProperty("工单号.")
    private String orderId;

    /*** 工单任务代理人的版本号，为防止影响到已有流程而设计. */

    private Integer assigneeVersion;

    /*** 工单任务代理人的用户ID. */
    private String assignee;

    /*** 工单任务代理人的顺序，从1开始，用于表示此工单任务代理人为第几个审批人/处理人. */
    private Integer assigneeOrder;

    /*** 任务(节点)名. */
    private String taskName;

    /*** 流程执行到此任务(节点)时的工单状态. */
    private String orderStatus;

    /*** 此工单任务代理人是否可以取消工单. */
    private Boolean canCancelOrder;

    /*** 创建人. */
    private String createUser;

    /*** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /*** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /*** 更新人. */
    private String updateUser;

    /*** 备注. */
    private String remark;


    @Override
    public String toString() {
        return JsonUtil.toJSONString(this);
    }
}