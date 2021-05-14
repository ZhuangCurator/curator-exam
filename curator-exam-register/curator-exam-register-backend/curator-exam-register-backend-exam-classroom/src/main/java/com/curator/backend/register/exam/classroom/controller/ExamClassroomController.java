package com.curator.backend.register.exam.classroom.controller;

import com.curator.backend.register.exam.classroom.entity.dto.ExamClassroomDTO;
import com.curator.backend.register.exam.classroom.entity.vo.info.ExamClassroomInfo;
import com.curator.backend.register.exam.classroom.entity.vo.search.ExamClassroomSearch;
import com.curator.backend.register.exam.classroom.service.ExamClassroomService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 教室 前端控制器
 *
 * @author Jun
 * @since 2021-05-13
 */
@RestController
@RequestMapping("/examClassroom")
public class ExamClassroomController {

    @Autowired
    private ExamClassroomService examClassroomService;

    /**
     * 教室分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "ExamClassroomController", remark = "教室分页查询")
    ResultResponse<PageResult<ExamClassroomDTO>> pageWithExamClassroom(ExamClassroomSearch search) {
        return examClassroomService.pageWithExamClassroom(search);
    }

    /**
     * 教室列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    @Log(controllerName = "ExamClassroomController", remark = "教室列表查询")
    ResultResponse<List<ExamClassroomDTO>> listWithExamClassroom(ExamClassroomSearch search) {
        return examClassroomService.listWithExamClassroom(search);
    }

    /**
     * 查询教室
     *
     * @param id 教室ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{id}")
    @Log(controllerName = "ExamClassroomController", remark = "查询教室")
    ResultResponse<ExamClassroomDTO> getExamClassroom(@PathVariable("id") String id) {
        return examClassroomService.getExamClassroom(id);
    }

    /**
     * 添加教室
     *
     * @param info 教室信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "ExamClassroomController", remark = "添加教室")
    ResultResponse<ExamClassroomDTO> saveExamClassroom(@RequestBody ExamClassroomInfo info) {
        return examClassroomService.saveExamClassroom(info);
    }

    /**
     * 编辑教室
     *
     * @param info 教室信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "ExamClassroomController", remark = "编辑教室")
    ResultResponse<ExamClassroomDTO> putExamClassroom(@RequestBody ExamClassroomInfo info) {
        return examClassroomService.putExamClassroom(info);
    }

    /**
     * 删除教室
     *
     * @param id 教室ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{id}")
    @Log(controllerName = "ExamClassroomController", remark = "删除教室")
    ResultResponse<String> removeExamClassroom(@PathVariable("id") String id) {
        return examClassroomService.removeExamClassroom(id);
    }

}
