package com.curator.core.register.controller;

import com.curator.api.register.pojo.dto.ExamRegisterInfoDTO;
import com.curator.api.register.pojo.vo.info.ExamRegisterInfoInfo;
import com.curator.api.register.pojo.vo.search.ExamRegisterInfoSearch;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.core.register.service.ExamRegisterService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

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
     * 个人所有报名信息
     *
     * @param search 分页条件
     * @return
     */
    @GetMapping("/pageWithRegisterInfo")
    @Log(controllerName = "ExamRegisterController", remark = "个人所有报名信息")
    ResultResponse<PageResult<ExamRegisterInfoDTO>> pageWithRegisterInfo(ExamRegisterInfoSearch search) {
        return examRegisterService.pageWithRegisterInfo(search);
    }

    /**
     * 预校验准考证信息
     *
     * @param examRegisterInfoId 考试报名信息id
     * @return
     */
    @GetMapping("/preview/admissionTicket")
    @Log(controllerName = "ExamRegisterController", remark = "预校验准考证信息")
    ResultResponse<?> previewAdmissionTicket(String examRegisterInfoId) {
        return examRegisterService.previewAdmissionTicket(examRegisterInfoId);
    }

    @GetMapping("/pdf")
    void pdfAdmissionTicket(String examRegisterInfoId, HttpServletResponse response) throws Exception {

        ExamRegisterInfoDTO source = examRegisterService.getRegisterInfo(examRegisterInfoId).getData();
        ByteArrayOutputStream byteOs = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, byteOs);
        document.open();
        BaseFont bfChinese = BaseFont.createFont("static/fonts/AdobeKaitiStd-Regular.otf", BaseFont.IDENTITY_H,
                BaseFont.NOT_EMBEDDED);
        Font textFont = new Font(bfChinese, 12, Font.NORMAL);
        Paragraph pTitle = new Paragraph("馆长全国综合考试", new Font(bfChinese, 12, Font.NORMAL));
        // 居中
        pTitle.setAlignment(1);
        // 段落下空白
        pTitle.setSpacingAfter(5f);
        Paragraph pAdmission = new Paragraph("准考证", new Font(bfChinese, 14, Font.BOLD));
        // 居中
        pAdmission.setAlignment(1);
        // 段落下空白
        pAdmission.setSpacingAfter(14f);
        document.add(pTitle);
        document.add(pAdmission);

        PdfPTable table = new PdfPTable(5);
        table.setTotalWidth(500);
        // 锁定宽度
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        // 第一行第一列
        addCell(table, "考生姓名", textFont, 1, 2);
        // 第一行第二列
        addCell(table, source.getAccountName(), textFont, 1, 2);
        // 第一行第三列
        addCell(table, "准考证号", textFont, 1, 2);
        // 第一行第四列
        addCell(table, source.getAdmissionNumber(), textFont, 1, 2);
        // 第一行第五列
        addCell(table, "", textFont, 1, 6);

        addCell(table, "性      别", textFont, 1, 2);
        addCell(table, "男", textFont, 1, 2);
        addCell(table, "报考类别", textFont, 1, 2);
        addCell(table, source.getExamCategoryName(), textFont, 1, 2);

        addCell(table, "身份证号", textFont, 1, 2);
        addCell(table, "340827199501033453", textFont, 3, 2);

        addCell(table, "考点考场信息", textFont, 5, 2);

        addCell(table, "考点名称", textFont, 1, 2);
        addCell(table, "望江第二中学", textFont, 2, 2);
        addCell(table, "场次(时间顺序号)", textFont, 1, 2);
        addCell(table, "011", textFont, 1, 2);

        addCell(table, "考试时间", textFont, 1, 2);
        addCell(table, "2021-06-21 09:00:00 至 2021-06-21 10:00:00", textFont, 2, 2);
        addCell(table, "座 位 号", textFont, 1, 2);
        addCell(table, "12", textFont, 5, 2);

        addCell(table, "考试地址", textFont, 1, 3, 60);
        addCell(table, "某某路某某号", textFont, 4, 3, 60);

        Paragraph p1 = new Paragraph("疫情防控期间现场考试考生须知",  new Font(bfChinese, 13, Font.BOLD));
        p1.setAlignment(1);
        Paragraph p2 = new Paragraph("        1.从考试日前14天开始，须自测体温，按照“一日一测，异常情况随时报”的疫情报告制度，及时将异常情况报告所在单位或社区防疫部门;\r\n"
                + "        2.不隐瞒或谎报旅居史、接触史、健康状况等疫情防控重点信息，自觉配合工作人员进行防疫检测、询问、排查、送诊等;\r\n"
                + "        3.考生须自备签字笔、消毒纸巾和一次性医用外科口罩，进入考点后须全程佩戴口罩。接受身份识别验证等特殊情况时自觉摘除口罩;\r\n"
                + "        4.距考试时间30分钟时，考生凭健康码、准考证、带磁身份证经体温测试合格后方可进入考点。若考生所属地市要求入场前上交《健康管理信息采集表》的，请务必携带。\r\n"
                + "        5.考生沿指定路线前往等候区，不得进入与考试无关的场所，严禁接触正常上课的学校老师和学生;\r\n"
                + "        6.到达等候区后，按照地标指示排队，保持左右间隔2米，前后间隔1米;\r\n"
                + "        7.考生通过身份证识别及人脸校验后入场，开考15分钟以后不得进入考场;\r\n"
                + "        8.进入考场时用自备签字笔签字登记；入座后用自备的消毒纸巾对所用电脑键盘、鼠标、桌面进行消毒（注意不要因纸巾过湿造成电器短路）;\r\n"
                + "        9.完成考试并确认已提交考卷后，按地标指示或现场考务人员的指引迅速离开考场和考点;\r\n"
                + "        10.考生在考试期间，应自觉服从考务工作人员的指挥，相互配合，互相理解，共同做好疫情防控特殊时期的考试工作;", textFont);

        PdfPCell cell = new PdfPCell();
        cell.addElement(p1);
        cell.addElement(p2);
        cell.setColspan(5);

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(400);
        table.addCell(cell);
        document.add(table);

        document.close();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(("准考证.pdf").getBytes(), StandardCharsets.ISO_8859_1));
        OutputStream out = response.getOutputStream();
        byteOs.writeTo(out);
        out.flush();
        out.close();
    }

    private void addCell(PdfPTable table, String text,  Font font, int colspan, int rowSpan) {
        addCell(table, text, font, colspan, rowSpan, 30);
    }

    private void addCell(PdfPTable table, String text,  Font font, int colspan, int rowSpan, int fixedHeight) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setColspan(colspan);
        cell.setRowspan(rowSpan);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(fixedHeight);
        table.addCell(cell);
    }
}
