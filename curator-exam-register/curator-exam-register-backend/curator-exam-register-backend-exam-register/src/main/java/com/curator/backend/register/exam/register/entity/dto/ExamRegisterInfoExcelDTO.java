package com.curator.backend.register.exam.register.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考试报名信息 excel对象
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamRegisterInfoExcelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 考试类别名称
     */
    @ExcelProperty("考试类别名称")
    @ColumnWidth(40)
    private String examCategoryName;

    /**
     * 考试科目名称
     */
    @ExcelProperty("考试科目名称")
    @ColumnWidth(40)
    private String examSubjectName;

    /**
     * 考点名称
     */
    @ExcelProperty("考点名称")
    @ColumnWidth(40)
    private String examSiteName;

    /**
     * 考生账户名称
     */
    @ExcelProperty("考生账户名称")
    @ColumnWidth(20)
    private String accountName;

    /**
     * 准考证编号
     */
    @ExcelProperty("准考证编号")
    @ColumnWidth(20)
    private String admissionNumber;

    /**
     * 考试状态(0:未开始考试,1:已结束考试,2:缺考)
     */
    @ExcelProperty("考试状态")
    @ColumnWidth(30)
    private String examStatusDesc;

    /**
     * 考生成绩
     */
    @ExcelProperty("考生成绩")
    @ColumnWidth(10)
    private String score;

    /**
     * 是否及格
     */
    @ExcelProperty("是否及格")
    @ColumnWidth(10)
    private String passedDesc;

}
