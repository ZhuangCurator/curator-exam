<template>
  <div class="noticeContent_container">
    <!-- 卡片视图区域-->
    <el-card class="box-card">
      <div>
        <div class="content_header">{{ examNotice.examNoticeName }}</div>
        <div class="content_time">发布日期: {{ examNotice.createTime }}</div>
        <el-divider></el-divider>
        <div class="content_main" v-html="examNotice.content"></div>
      </div>
    </el-card>
</div>
</template>

<script>
import {
  handleExamNoticeQuery
} from '@/apis/notice/examNotice'
export default {
  name: 'NoticeContentPage',
  data () {
    return {
      examNoticeId: undefined,
      examNotice: {
        examNoticeName: undefined,
        content: undefined
      }
    }
  },
  methods: {
    // 得到考试公告分页数据
    async getExamNotice () {
      const { data: res } = await handleExamNoticeQuery(this.examNoticeId)
      console.log(res.data)
      if (res.status !== '2000') return this.$message.error(res.message)
      this.examNotice = res.data
    }
  },
  mounted () {
    this.examNoticeId = this.$route.query.id
    this.getExamNotice()
  }
}
</script>

<style  lang="less" scoped>
.content_header {
  width: 100%;
  line-height:50px;
  text-align: center;
  vertical-align:middle;
  font-size: 30px;
  font-weight: bold;
  // 字体颜色
  color: blueviolet;
}
.content_time {
  width: 100%;
  height: 30px;
  color: darkgray;
}
.el-divider--horizontal {
  margin: 0 0 24px 0 !important;
}
</style>
