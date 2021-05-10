package com.curator.backend.paper.question.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.curator.backend.paper.question.entity.dto.QuestionDTO;
import com.curator.backend.paper.question.entity.vo.info.QuestionExcelInfo;
import com.curator.backend.paper.question.entity.vo.info.QuestionInfo;
import com.curator.backend.paper.question.entity.vo.search.QuestionSearch;
import com.curator.backend.paper.question.listener.FillBlankListener;
import com.curator.backend.paper.question.listener.JudgmentListener;
import com.curator.backend.paper.question.listener.MultipleChoiceListener;
import com.curator.backend.paper.question.listener.SingleChoiceListener;
import com.curator.backend.paper.question.service.QuestionService;
import com.curator.common.annotation.Log;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.RedissonUtil;
import com.curator.common.util.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.ExtensionInfo;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 试题 前端控制器
 *
 * @author Jun
 * @since 2021-05-08
 */
@Slf4j
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * 试题分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "QuestionController", remark = "试题分页查询")
    ResultResponse<PageResult<QuestionDTO>> pageWithQuestion(QuestionSearch search) {
        return questionService.pageWithQuestion(search);
    }

    /**
     * 试题列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    @Log(controllerName = "QuestionController", remark = "试题列表查询")
    ResultResponse<List<QuestionDTO>> listWithQuestion(QuestionSearch search) {
        return questionService.listWithQuestion(search);
    }

    /**
     * 查询试题
     *
     * @param id 试题ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{id}")
    @Log(controllerName = "QuestionController", remark = "查询试题")
    ResultResponse<QuestionDTO> getQuestion(@PathVariable("id") String id) {
        return questionService.getQuestion(id);
    }

    /**
     * 添加试题
     *
     * @param info 试题信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "QuestionController", remark = "添加试题")
    ResultResponse<QuestionDTO> saveQuestion(@RequestBody QuestionInfo info) {
        return questionService.saveQuestion(info);
    }

    /**
     * 编辑试题
     *
     * @param info 试题信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "QuestionController", remark = "编辑试题")
    ResultResponse<QuestionDTO> putQuestion(@RequestBody QuestionInfo info) {
        return questionService.putQuestion(info);
    }

    /**
     * 删除试题
     *
     * @param id 试题ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{id}")
    @Log(controllerName = "QuestionController", remark = "删除试题")
    ResultResponse<String> removeQuestion(@PathVariable("id") String id) {
        return questionService.removeQuestion(id);
    }

    /**
     * 下载试题导入模版
     */
    @GetMapping("/export/template")
    @Log(controllerName = "QuestionController", remark = "下载试题导入模版")
    void exportQuestionTemplate(HttpServletResponse response) {
        try {
            InputStream in = this.getClass().getResourceAsStream("/static/Question.xlsx");
            String excelName = URLEncoder.encode("试题导入模板", "UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + excelName + ".xlsx");
            ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.copy(in, outputStream);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 预校验 试题Excel
     *
     * @param file excel文件
     * @return
     */
    @PostMapping("/preCheck")
    @Log(controllerName = "QuestionController", remark = "预校验 试题Excel")
    ResultResponse<?> preCheckQuestionTemplate(MultipartFile file) {
        ExcelReader excelReader = null;
        String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
        RedissonUtil.deleteObject("QUESTION:IMPORT:OK:" + createAccountId);
        RedissonUtil.deleteObject("QUESTION:IMPORT:WRONG:" + createAccountId);
        try {
            excelReader = EasyExcel.read(file.getInputStream()).build();
            // 单选
            ReadSheet readSheet1 =
                    EasyExcel.readSheet(0).head(QuestionExcelInfo.class).registerReadListener(new SingleChoiceListener(createAccountId)).build();
            // 多选
            ReadSheet readSheet2 =
                    EasyExcel.readSheet(1).head(QuestionExcelInfo.class).registerReadListener(new MultipleChoiceListener(createAccountId)).build();
            // 判断
            ReadSheet readSheet3 =
                    EasyExcel.readSheet(2).head(QuestionExcelInfo.class).registerReadListener(new JudgmentListener(createAccountId)).build();
            // 填空
            ReadSheet readSheet4 =
                    EasyExcel.readSheet(3).head(QuestionExcelInfo.class).registerReadListener(new FillBlankListener(createAccountId)).build();

            excelReader.read(readSheet1, readSheet2, readSheet3, readSheet4);
        } catch (IOException e) {
            log.error("QuestionController#preCheckQuestionTemplate has exception: {}", e.getMessage());
            return ResultResponse.builder().failure("试题批量导入预校验失败").build();
        } finally {
            if (excelReader != null) {
                // 关闭
                excelReader.finish();
            }
        }
        List<QuestionInfo> listOk = RedissonUtil.getCacheList("QUESTION:IMPORT:OK:" + createAccountId);
        List<ExtensionInfo> listWrong = RedissonUtil.getCacheList("QUESTION:IMPORT:WRONG:" + createAccountId);
        return ResultResponse.<String>builder().success("正确数据: " + listOk.size() + "条,错误数据: " + listWrong.size() + "条!").build();
    }

    /**
     * 批量上传 试题
     * @return
     */
    @PostMapping("/batch/upload")
    @Log(controllerName = "QuestionController", remark = "批量上传 试题")
    ResultResponse<?> batchUploadQuestion() {
        return questionService.batchUploadQuestion();
    }

}
