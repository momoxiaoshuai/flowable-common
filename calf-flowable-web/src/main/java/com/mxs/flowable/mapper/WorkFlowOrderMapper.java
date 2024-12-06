package com.mxs.flowable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mxs.domain.entity.WorkFlowOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WorkFlowOrderMapper extends BaseMapper<WorkFlowOrder> {

    @Select("SELECT" +
            "            WO.*" +
            "        FROM " +
            "            WORK_FLOW_ORDER WO" +
            "        LEFT JOIN " +
            "            ACT_HI_PROCINST P ON P.BUSINESS_KEY_ = WO.ORDER_ID" +
            "        LEFT JOIN " +
            "            ACT_HI_COMMENT C ON C.PROC_INST_ID_ = P.PROC_INST_ID_" +
            "        WHERE " +
            "            C.USER_ID_ = #{handler} " +
            "            AND WO.HANDLER != #{handler}" +
            "        GROUP BY" +
            "            WO.ORDER_ID," +
            "            WO.ORDER_TYPE," +
            "            WO.APPLICANT," +
            "            WO.APPLICATION_TIME," +
            "            WO.ORDER_STATUS," +
            "            WO.REASON," +
            "            WO.HANDLER" +
            "        ORDER BY " +
            "            WO.APPLICATION_TIME DESC")
    List<WorkFlowOrder> selectMyHandled(@Param("handler") String handler);
}
