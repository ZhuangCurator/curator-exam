package com.curator.core.paper.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.curator.api.paper.enums.TestPaperStatusEnum;
import com.curator.api.paper.provider.TestPaperProvider;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.paper.entity.TestPaper;
import com.curator.core.paper.mapper.TestPaperMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 试卷 RPC接口实现类
 *
 * @author Jun
 * @date 2021/5/15
 */
@DubboService
public class TestPaperProviderImpl implements TestPaperProvider {

    @Autowired
    private TestPaperMapper testPaperMapper;

    @Override
    public ResultResponse<String> reExam(String examRegisterInfoId, Integer paperStatus) {
        // 查看考生是否有正在进行的考试或者考试已结束的试卷
        QueryWrapper<TestPaper> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_register_info_id", examRegisterInfoId)
                .and(wr -> wr.eq("paper_status", TestPaperStatusEnum.PROCESSING.getStatus())
                    .or(w -> w.eq("paper_status",  TestPaperStatusEnum.OVER.getStatus()))
                );
        TestPaper testPaper = testPaperMapper.selectOne(wrapper);
        if(Help.isNotEmpty(testPaper)) {
            testPaper.setPaperStatus(paperStatus);
            testPaperMapper.update(testPaper, new UpdateWrapper<TestPaper>().eq("test_paper_id", testPaper.getTestPaperId()));
        }
        return ResultResponse.<String>builder().success("考生试卷设置重考成功").build();
    }
}
