package com.mxs.flowable.controller;


import com.mxs.flowable.mapper.FlowableMapper;
import com.mxs.domain.dto.R;
import com.mxs.flowable.service.CommonFlowService;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@Slf4j
@RestController
@RequestMapping("common")
public class CommonFlowController {
    @Autowired
    private RepositoryService repositoryService;


    @Autowired
    private FlowableMapper flowableMapper;

    @Autowired
    private CommonFlowService commonFlowService;

    @GetMapping("clear")
    public R clearProcessInstancesPhysically() {
        flowableMapper.clearProcessInstancesPhysically();
        return R.success();
    }


    @PostMapping("/addFlow")
    public R addFlow(@RequestBody Map<String, Object> params) {
        commonFlowService.saveFlow(params);
        return R.success();
    }


    /**
     * 导入 BPMN 流程
     *
     * @param file
     * @return
     */
    @PostMapping("/process/import")
    public R<String> importBpmn(@RequestParam("file") MultipartFile file) {
        try {
            repositoryService.createDeployment().addInputStream(file.getOriginalFilename(), file.getInputStream()).deploy();
            return R.success("流程导入成功");
        } catch (Exception e) {
            return R.failed("流程导入失败");
        }
    }

    /**
     * 查询流程列表
     *
     * @return
     */

    @GetMapping("/process/list")
    public R<List<Map<String, String>>> getProcessList() {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
        List<Map<String, String>> respList = new ArrayList<>();
        for (ProcessDefinition processDefinition : processDefinitions) {
            Map<String, String> map = new HashMap<>();
            map.put("id", processDefinition.getId());
            map.put("name", processDefinition.getName());
            map.put("key", processDefinition.getKey());
            respList.add(map);
        }
        return R.success(respList);
    }


    //删除流程
    @GetMapping("/process/delete")
    public R<Void> deleteProcessDefinitionsByKey(String processDefinitionKey) {
        // 查询所有和指定 processDefinitionKey 相关的流程定义
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
        // 遍历流程定义并删除相关的部署
        for (ProcessDefinition processDefinition : processDefinitions) {
            String deploymentId = processDefinition.getDeploymentId();
            repositoryService.deleteDeployment(deploymentId, true); // 第二个参数表示是否级联删除
        }
        return R.success();
    }


}
