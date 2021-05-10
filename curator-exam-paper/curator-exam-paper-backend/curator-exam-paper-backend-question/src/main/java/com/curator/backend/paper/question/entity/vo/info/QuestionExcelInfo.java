package com.curator.backend.paper.question.entity.vo.info;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * excel表格 - 试题信息
 *
 * @author Jun
 * @date 2021-05-08
 */
@Data
public class QuestionExcelInfo implements Serializable {

    @ExcelProperty("试题内容")
    private String questionStem;

    @ExcelProperty("选项A")
    private String optionsA;

    @ExcelProperty("选项B")
    private String optionsB;

    @ExcelProperty("选项C")
    private String optionsC;

    @ExcelProperty("选项D")
    private String optionsD;

    @ExcelProperty("选项E")
    private String optionsE;

    @ExcelProperty("答案")
    private String answer;

    @ExcelProperty("答案1")
    private String answerOne;

    @ExcelProperty("答案2")
    private String answerTwo;

    @ExcelProperty("答案3")
    private String answerThree;

    @ExcelProperty("答案4")
    private String answerFour;

    @ExcelProperty("答案5")
    private String answerFive;

    @ExcelProperty("解析")
    private String analysis;

    @ExcelProperty("分值")
    private Integer point;

    @ExcelProperty("难度等级")
    private String difficulty;

    @ExcelProperty("答案是否有序")
    private String order;

    @ExcelProperty("错误原因")
    private String reason;
}
