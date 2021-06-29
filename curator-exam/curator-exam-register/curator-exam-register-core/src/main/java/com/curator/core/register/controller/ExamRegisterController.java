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

    /**
     * 打印准考证
     *
     * @param examRegisterInfoId 考试报名信息id
     * @param response
     * @throws Exception
     */
    @GetMapping("/pdf")
    @Log(controllerName = "ExamRegisterController", remark = "打印准考证")
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

        Paragraph p1 = new Paragraph("现场考试考生须知",  new Font(bfChinese, 13, Font.BOLD));
        p1.setAlignment(1);
        Paragraph p2 = new Paragraph("        1.考生应认真核实准考证所列各项信息是否准确，如有问题，应在考试之前向考点确认;\r\n"
                + "        2.考生须持准考证和报名时使用的身份证件进入考场;\r\n"
                + "        3.考试当天，考生应依照准考证表明时间准时到达考场。考试正式开始后，迟到考生禁止入场;\r\n"
                + "        4.严禁将纸张、文字资料、通讯设备和存储器等带至座位。已带入考场的要切断电源，按照监考人员的要求放在指定地点。凡将上述设备带至座位者，一律按严重违规处理，取消其考试资格;\r\n"
                + "        5.考场内必须保持安静，严禁交头接耳，不得窥视他人的答题。若有作弊行为，将取消其考试资格;\r\n"
                + "        6.考试期间不得提前离场。考试结束后经监考人员许可后，方可离开考场;", textFont);

        PdfPCell cell = new PdfPCell();
        cell.addElement(p1);
        cell.addElement(p2);
        cell.setColspan(5);

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(250);
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
