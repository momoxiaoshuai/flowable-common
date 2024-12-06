package com.mxs.flowable.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mxs.domain.dto.R;
import com.mxs.domain.entity.WorkFlowFormField;
import com.mxs.domain.entity.WorkFlowOrder;
import com.mxs.flowable.service.CommonFlowService;
import com.mxs.flowable.service.WorkFlowFormFieldService;
import com.mxs.flowable.service.WorkFlowOrderService;
import com.mxs.flowable.utils.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.task.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("order")
public class WorkFlowOrderController {

    @Autowired
    private WorkFlowOrderService workFlowOrderService;
    @Autowired
    private WorkFlowFormFieldService workFlowFormFieldService;
    @Autowired
    private CommonFlowService commonFlowService;


    /**
     * 根据类型查看审批
     *
     * @param orderType
     * @return
     */
    @GetMapping("/selectByType")
    public R<List<WorkFlowOrder>> selectByOrderType(String orderType) {
        List<WorkFlowOrder> workFlowOrderList = workFlowOrderService.selectByOrderType(orderType);
        //获取流程表单字段
        switchVo(orderType, workFlowOrderList);
        return R.success(workFlowOrderList);
    }


    /**
     * 获取待处理订单
     *
     * @param handler 处理人
     * @return
     */
    @GetMapping("/myDeals")
    public R<List<WorkFlowOrder>> myDeals(String handler, String orderType) {
        List<WorkFlowOrder> workFlowOrderList = workFlowOrderService.selectOrdersByHandler(handler, orderType);
        switchVo(orderType, workFlowOrderList);
        return R.success(workFlowOrderList);
    }


    /**
     * @param applicant 申请人
     * @return
     */
    @GetMapping("/myApply")
    public R<List<WorkFlowOrder>> myApply(String applicant) {
        List<WorkFlowOrder> workFlowOrderList = workFlowOrderService.selectOrdersByApplicant(applicant);
        return R.success(workFlowOrderList);
    }


    /**
     * 封装响应数据
     *
     * @param orderType
     * @param workFlowOrderList
     */
    private void switchVo(String orderType, List<WorkFlowOrder> workFlowOrderList) {
        Map<String, Object> fieldInfo = getFieldInfo(orderType);
        workFlowOrderList.forEach(workFlowOrder -> {
            workFlowOrder.setFieldInfo(fieldInfo);
            Map<String, Object> fieldValue = MapUtils.stringToMap(workFlowOrder.getFormData());
            workFlowOrder.setFieldValue(fieldValue);
        });
    }

    private Map<String, Object> getFieldInfo(String orderType) {
        Map<String, Object> fieldInfo = new HashMap<>();
        List<WorkFlowFormField> workFlowFormFieldList = workFlowFormFieldService.selectFieldCNameByOrderType(orderType);
        for (WorkFlowFormField workFlowFormField : workFlowFormFieldList) {
            fieldInfo.put(workFlowFormField.getFieldName(), workFlowFormField.getFieldCname());
        }
        return fieldInfo;
    }


    @GetMapping("/myHandled")
    public R<List<WorkFlowOrder>> myHandled(String handler) {
        List<WorkFlowOrder> workFlowOrderList = workFlowOrderService.selectMyHandled(handler);
        return R.success(workFlowOrderList);
    }

    @GetMapping("/getById")
    public R<WorkFlowOrder> selectByOrderId(String orderId) {
        WorkFlowOrder workFlowOrder = workFlowOrderService.selectOrderByOrderId(orderId);
        return R.success(workFlowOrder);
    }

    /**
     * 工单进程
     *
     * @param orderId
     * @return
     */
    @GetMapping("/orderProgress")
    public R<List<Comment>> listTaskComments(String orderId) {
        List<Comment> commentList = commonFlowService.listTaskComments(orderId);
        return R.success(commentList);
    }


    /**
     * 参数
     *
     * @param params userId      申请人
     *               orderType   订单类型
     *               formJson 表单信息
     */
    @PostMapping("/add")
    public R<WorkFlowOrder> addWorkOrder(@RequestBody HashMap<String, Object> params) {
        String userId = MapUtils.getStringValue(params, "userId");
        Assert.notNull(userId, "用户不能为空");
        String orderType = MapUtils.getStringValue(params, "orderType");
        Assert.notNull(orderType, "申请类型不能为空");
        String formJsonStr = JSON.toJSONString(params.get("formJson"));
        Assert.notNull(formJsonStr, "表单不能为空");
        WorkFlowOrder workFlowOrder = workFlowOrderService.addWorkOrder(userId, orderType, formJsonStr);
        return R.success(workFlowOrder);
    }

    /**
     * @param params orderId 工单Id
     *               agree   是否通过审核 1:通过 -1:不通过 -999:撤销
     *               comment 审批意见
     * @return
     */
    @PostMapping("approval")
    public R approval(@RequestBody HashMap<String, Object> params) {
        String businessKey = MapUtils.getStringValue(params, "orderId");
        String comment = MapUtils.getStringValue(params, "comment");
        String agree = MapUtils.getStringValue(params, "agree");
        commonFlowService.examAndApprove(businessKey, agree, comment);
        return R.success();
    }

    @PostMapping("modify")
    public R modifyWorkOrder(@RequestBody HashMap<String, Object> params) {
        String orderId = MapUtils.getStringValue(params, "orderId");
        String formJsonStr = JSON.toJSONString(params.get("formJson"));
        WorkFlowOrder workFlowOrder = workFlowOrderService.selectOrderByOrderId(orderId);
        Assert.notNull(workFlowOrder, "未找到工单, 工单号：" + orderId);
        workFlowOrder.setOrderId(orderId);
        workFlowOrder.setFormData(formJsonStr);
        workFlowOrderService.modificationWorkOrder(workFlowOrder);
        return R.success();
    }


}
