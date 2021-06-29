package com.curator.core.register.controller;

import com.curator.api.register.pojo.dto.ExamRegisterInfoDTO;
import com.curator.api.register.pojo.vo.info.ExamRegisterInfoInfo;
import com.curator.api.register.pojo.vo.search.ExamRegisterInfoSearch;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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
        addCell(table, "考生姓名", textFont, 1, 2);
        addCell(table, source.getAccountName(), textFont, 1, 2);

        addCell(table, "性      别", textFont, 1, 2);
        // 计算性别
        int num = Integer.parseInt(source.getIdCard().substring(16, 17));
        if (num % 2 == 0) {
            addCell(table, "女", textFont, 1, 2);
        } else {
            addCell(table, "男", textFont, 1, 2);
        }
        addCell(table, "图片", textFont, 1, 6);

        addCell(table, "身份证号", textFont, 1, 2);
        addCell(table, "340827199501033453", textFont, 3, 2);

        addCell(table, "准考证号", textFont, 1, 2);
        addCell(table, source.getAdmissionNumber(), textFont, 3, 2);

        addCell(table, "考试信息", new Font(bfChinese, 12, Font.BOLD), 5, 2);

        addCell(table, "考试类别", textFont, 1, 2);
        addCell(table, source.getExamCategoryName(), textFont, 4, 2);

        addCell(table, "考试科目", textFont, 1, 2);
        addCell(table, source.getExamSubjectName(), textFont, 4, 2);

        addCell(table, "考试时间", textFont, 1, 2);
        addCell(table, Help.localDateTimeToTxt(source.getExamStartTime()) + " 至 " + Help.localDateTimeToTxt(source.getExamEndTime()), textFont, 4, 2);
        addCell(table, "考点名称", textFont, 1, 2);
        addCell(table, source.getExamSiteName(), textFont, 4, 2);
        addCell(table, "教室名称", textFont, 1, 2);
        addCell(table, source.getExamClassroomName(), textFont, 2, 2);

        addCell(table, "座 位 号", textFont, 1, 2);
        addCell(table, String.valueOf(source.getSeatNumber()), textFont, 1, 2);

        addCell(table, "考试地址", textFont, 1, 3, 60);
        addCell(table, source.getExamSiteAddress() + "\r\n" + source.getExamClassroomAddress(), textFont, 4, 3, 60);

        Paragraph p1 = new Paragraph("疫情防控期间现场考试考生须知",  new Font(bfChinese, 13, Font.BOLD));
        p1.setAlignment(1);
        Paragraph p2 = new Paragraph("        1.从考试日前14天开始，须自测体温，按照“一日一测，异常情况随时报”的疫情报告制度，及时将异常情况报告所在单位或社区防疫部门;\r\n"
                + "        2.不隐瞒或谎报旅居史、接触史、健康状况等疫情防控重点信息，自觉配合工作人员进行防疫检测、询问、排查、送诊等;\r\n"
                + "        3.考生须自备签字笔、消毒纸巾和一次性医用外科口罩，进入考点后须全程佩戴口罩。接受身份识别验证等特殊情况时自觉摘除口罩;\r\n"
                + "        4.考生沿指定路线前往等候区，不得进入与考试无关的场所，严禁接触正常上课的学校老师和学生;\r\n"
                + "        5.完成考试并确认已提交考卷后，按地标指示或现场考务人员的指引迅速离开考场和考点;\r\n"
                + "        6.考生在考试期间，应自觉服从考务工作人员的指挥，相互配合，互相理解，共同做好疫情防控特殊时期的考试工作;", textFont);

        PdfPCell cell = new PdfPCell();
        cell.addElement(p1);
        cell.addElement(p2);
        cell.setColspan(5);

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(300);
        table.addCell(cell);
        document.add(table);

        document.close();
        response.setContentType("application/pdf");
        String fileName = URLEncoder.encode("准考证", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".pdf");
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
