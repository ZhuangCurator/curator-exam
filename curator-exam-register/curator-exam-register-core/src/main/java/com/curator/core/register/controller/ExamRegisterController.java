package com.curator.core.register.controller;

import com.curator.api.register.pojo.dto.ExamRegisterInfoDTO;
import com.curator.api.register.pojo.vo.info.ExamRegisterInfoInfo;
import com.curator.api.register.pojo.vo.search.ExamRegisterInfoSearch;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.core.register.service.ExamRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import wiki.xsx.core.pdf.component.table.XEasyPdfCell;
import wiki.xsx.core.pdf.component.table.XEasyPdfRow;
import wiki.xsx.core.pdf.component.text.XEasyPdfTextStyle;
import wiki.xsx.core.pdf.doc.XEasyPdfDocument;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.io.IOException;

/**
 * 考生报名信息 前端控制器
 *
 * @author Jun
 * @date 2021/5/17
 */
@RestController
@RequestMapping("/examRegisterInfo")
public class ExamRegisterController {

    @Autowired
    private ExamRegisterService examRegisterService;

    /**
     * 考生报名
     *
     * @param info 报名信息
     * @return
     */
    @PostMapping("/register")
    @Log(controllerName = "ExamRegisterController", remark = "考生报名")
    ResultResponse<String> accountRegister(@RequestBody ExamRegisterInfoInfo info) {
        return examRegisterService.accountRegister(info);
    }

    /**
     * 个人报名信息
     *
     * @param search 分页条件
     * @return
     */
    @GetMapping("/pageWithRegisterInfo")
    @Log(controllerName = "ExamRegisterController", remark = "个人报名信息")
    ResultResponse<PageResult<ExamRegisterInfoDTO>> pageWithRegisterInfo(ExamRegisterInfoSearch search) {
        return examRegisterService.pageWithRegisterInfo(search);
    }

    @GetMapping("/pdf")
    void pdfAdmissionTicket () throws IOException {
                // 添加页面
        XEasyPdfHandler.Document.build().addPage(
                // 构建页面
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Table.build(
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(200, 70).addContent(
                                                XEasyPdfHandler.Text.build(
                                                        "第一行第一列"
                                                )
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(200, 70).addContent(
                                                XEasyPdfHandler.Text.build(
                                                        "第一行第二列"
                                                )
                                        )
                                ),
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(200, 70).addContent(
                                                XEasyPdfHandler.Text.build(
                                                        "第二行第一列"
                                                )
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(200, 70).addContent(
                                                XEasyPdfHandler.Text.build(
                                                        "第二行第二列"
                                                )
                                        )
                                )
                        ),
                        // 构建文本
                        XEasyPdfHandler.Text.build(
                                "Hello World(这是一个DEMO)"
                        ).setStyle(XEasyPdfTextStyle.CENTER).setFontSize(20F).setMargin(10F)
                        // 构建文本
                        ,XEasyPdfHandler.Text.build(
                                "        这里是正文（这是一个基于PDFBOX开源工具，专注于PDF文件导出功能，" +
                                        "以组件形式进行拼接，简单、方便，上手及其容易，" +
                                        "目前有TEXT(文本)、LINE(分割线)等组件，后续还会补充更多组件，满足各种需求）。"
                        ).setStyle(XEasyPdfTextStyle.LEFT).setFontSize(14F).setMargin(10F)
                        // 构建文本
                        ,XEasyPdfHandler.Text.build(
                                "-- by xsx"
                        ).setStyle(XEasyPdfTextStyle.RIGHT).setFontSize(12F).setMarginTop(10F).setMarginRight(10F)
                        // 构建文本
                        ,XEasyPdfHandler.Text.build(
                                "2020.03.12"
                        ).setStyle(XEasyPdfTextStyle.RIGHT).setFontSize(12F).setMarginTop(10F).setMarginRight(10F)
                        // 构建实线分割线
                        ,XEasyPdfHandler.SplitLine.SolidLine.build().setMarginTop(10F)
                        // 构建虚线分割线
                        ,XEasyPdfHandler.SplitLine.DottedLine.build().setLineLength(10F).setMarginTop(10F).setLineWidth(10F)
                        // 构建实线分割线
                        ,XEasyPdfHandler.SplitLine.SolidLine.build().setMarginTop(10F)
                        // 构建文本
                        ,XEasyPdfHandler.Text.build( "完结").setStyle(XEasyPdfTextStyle.CENTER)
                )
                // 设置字体路径，并保存
        ).setFontPath("static/fonts/1571279005.ttf").save("/home/jun/Desktop/text.pdf").close();
    }
}
