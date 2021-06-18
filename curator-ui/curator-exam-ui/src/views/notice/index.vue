<template>
  <div class="notice_container">
    <title-header></title-header>
    <div class="notice_div">
      <h2>考生考试须知</h2>
      <el-card class="content_card">
        <div><h3>1. 考前一定要认真阅读《考试规则》及《国家教育考试违规处理办法》</h3></div>
        <div><h3>2. 考生进入考场门后，监考教师用金属探测仪对每一位考生进行检测，发出声响的考生要接受监考教师的进一步检查,考生要积极配合监考教师的工作</h3></div>
        <div><h3>3. 不能带走和故意毁坏任何一张试卷、答题卡和草稿纸，否则各科考试成绩无效。</h3></div>
        <div><h3>4. 因个人原因，弄脏或轻度损毁答题卡，一般不影响网上阅卷，无需更换答题卡</h3></div>
        <div><h3>5. 考试期间水杯(瓶)不能放在桌子上，以防弄湿答题卡和损坏试卷。矿泉、纯净水瓶不得有外标签遮挡</h3></div>
        <div><h3>6. 考试时间结束,将会自动交卷</h3></div>
        <div><h3>7. 开考前将统一发放考试口令</h3></div>
      </el-card>
      <el-button type="primary" v-if="showStart" @click="showDialog">开始考试</el-button>
      <el-button type="primary" v-else disabled>请仔细阅读考试须知 {{ seconds }}秒</el-button>
    </div>
    <!-- 验证考试口令对话框 -->
    <el-dialog title="验证口令" :visible.sync="dialogVisible" width="400px" @close="handleDialogClose">
      <!-- 对话框主题区域 -->
      <el-form ref="checkFormRef" :model="checkForm" :rules="checkFormRules" label-width="70px">
        <el-form-item label="口令" prop="examPassword">
          <el-input v-model="checkForm.examPassword"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部按钮区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleDialogConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import TitleHeader from '@/components/TitleHeader'
import { handlePaperInit, handleVerifyPassword } from '@/apis/paper'
export default {
  name: 'Notice',
  data () {
    return {
      // 倒计时秒
      seconds: 10,
      // 是否展示开始考试按钮
      showStart: false,
      // 是否展示验证考试口令对话框
      dialogVisible: false,
      // 口令表单
      checkForm: {
        examPassword: undefined
      },
      // 口令表单校验规则
      checkFormRules: {
        examPassword: [
          {
            required: true,
            message: '请输入考试口令',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  components: { TitleHeader },
  methods: {
    // 初始化试卷
    async initPaper () {
      const param = { examRegisterInfoId: this.$store.state.examRegisterInfoId, generationRuleId: this.$store.state.generationRuleId }
      const { data: res } = await handlePaperInit(param)
      if (res.status !== '2000') {
        this.$message.error(res.message)
      } else {
        // 保存试卷ID
        await this.$store.dispatch('saveTestPaperId', res.data)
      }
    },
    // 10秒 倒计时
    countdown () {
      this.timer = setInterval(() => {
        this.seconds--
        if (this.seconds === 0) {
          this.showStart = true
          clearInterval(this.timer)
        }
      }, 1000)
    },
    // 展示口令对话框
    showDialog () {
      this.dialogVisible = true
    },
    // 口令对话框关闭
    handleDialogClose () {
      // 清空字段
      this.$refs.checkFormRef.resetFields()
    },
    // 口令对话框确定
    async handleDialogConfirm () {
      console.log(this.checkForm)
      const param = {
        examRegisterInfoId: this.$store.state.examRegisterInfoId,
        testPaperId: this.$store.state.testPaperId,
        examPassword: this.checkForm.examPassword
      }
      const { data: res } = await handleVerifyPassword(param)
      if (res.status !== '2000') {
        this.$message.error(res.message)
      } else {
        this.dialogVisible = false
        // 保存考试时长
        this.$store.commit('setExamDuration', res.data.examDuration)
        // 则跳转至考试页
        await this.$router.push({
          path: 'paper'
        })
      }
    }
  },
  mounted () {
    // 首先开始初始化试卷
    this.initPaper()
    // 开始倒计时
    this.countdown()
  }
}
</script>

<style lang="less" scoped>
.notice_container, .notice_div {
  height: 100%;
}
.notice_div {
  text-align: center;
  margin: 0 auto;
}
.el-card {
  width: 55%;
  // 圆角边框
  border-radius: 3px;
  position: relative;
  left: 50%;
  transform: translate(-50%);
  text-align: left;
  margin-bottom: 10px;
}

</style>
