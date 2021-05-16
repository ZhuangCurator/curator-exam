package com.curator.backend.register.exam.register.controller;

import com.alibaba.excel.EasyExcel;
import com.curator.backend.register.exam.register.entity.dto.ExamRegisterInfoDTO;
import com.curator.backend.register.exam.register.entity.dto.ExamRegisterInfoExcelDTO;
import com.curator.backend.register.exam.register.entity.vo.search.ExamRegisterInfoSearch;
import com.curator.backend.register.exam.register.entity.vo.search.info.ExamRegisterInfoInfo;
import com.curator.backend.register.exam.register.service.ExamRegisterInfoService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 考试报名信息 前端控制器
 *
 * @author Jun
 * @since 2021-05-13
 */
@RestController
@RequestMapping("/examRegisterInfo")
public class ExamRegisterInfoController {

    @Autowired
    private ExamRegisterInfoService examRegisterInfoService;

    /**
     * 考试报名信息分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "ExamRegisterInfoController", remark = "考试报名信息分页查询")
    ResultResponse<PageResult<ExamRegisterInfoDTO>> pageWithExamRegisterInfo(ExamRegisterInfoSearch search) {
        return examRegisterInfoService.pageWithExamRegisterInfo(search);
    }

    /**
     * 考生统一生成考试口令
     *
     * @param info 考试信息
     * @return
     */
    @PutMapping("/generate/examPassword")
    @Log(controllerName = "ExamRegisterInfoController", remark = "考生统一生成考试口令")
    ResultResponse<?> generateExamPassword(@RequestBody ExamRegisterInfoInfo info) {
        return examRegisterInfoService.generateExamPassword(info);
    }

    /**
     * 考生统一分配教室
     *
     * @param info 考试信息
     * @return
     */
    @PutMapping("/assign/classroom")
    @Log(controllerName = "ExamRegisterInfoController", remark = "考生统一分配教室")
    ResultResponse<?> assignClassroom(@RequestBody ExamRegisterInfoInfo info) {
        return examRegisterInfoService.assignClassroom(info);
    }

    /**
     * 考生重考
     *
     * @param info 考试信息
     * @return
     */
    @PutMapping("/reExam")
    @Log(controllerName = "ExamRegisterInfoController", remark = "考生重考")
    ResultResponse<?> reExam(@RequestBody ExamRegisterInfoInfo info) {
        return examRegisterInfoService.reExam(info);
    }

    /**
     * 导出报考人员名单
     *
     * @param search 人员查询信息
     */
    @GetMapping("/export")
    @Log(controllerName = "ExamRegisterInfoController", remark = "导出报考人员名单")
    void exportAccountInfoOfExamSite(ExamRegisterInfoSearch search, HttpServletResponse response) throws Exception {
        List<ExamRegisterInfoExcelDTO> dataList = examRegisterInfoService.listWithExamRegisterInfoExcel(search);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("考点报考人员信息", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ExamRegisterInfoExcelDTO.class).sheet("模板").doWrite(dataList);
    }
}
