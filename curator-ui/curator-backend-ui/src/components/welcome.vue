<template>
  <div>
    <h2>Welcome</h2>
      <!--<el-checkbox-group-->
    <!--    v-model="question.answer"-->
    <!--    :min="0"-->
    <!--    :max="2"-->
    <!--  >-->
    <!--    <el-row v-for="(item, index) in ['A', 'B', 'C', 'D']" :key="item">-->
    <!--      <el-col :span="1">-->
    <!--        <el-checkbox-button-->
    <!--          v-model="question.answer"-->
    <!--          :label="question.content[index]"-->
    <!--          border-->
    <!--        >-->
    <!--          {{ item }}-->
    <!--        </el-checkbox-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="23">-->
    <!--        <el-input-->
    <!--          v-model="question.content[index]"-->
    <!--          placeholder="请输入选项..."-->
    <!--        />-->
    <!--      </el-col>-->
    <!--    </el-row>-->
    <!--  </el-checkbox-group>-->

    <el-checkbox-group
      v-model="checkedCities"
      :min="0"
      :max="3"
     >
      <!--<el-checkbox v-for="city in cities" :label="city" :key="city">{{city}}</el-checkbox>-->
      <el-row v-for="(item, index) in cities" :key="item.code" >
        <el-col :span="1">
          <el-checkbox :label="item.name"   size="medium">{{ item.name }}</el-checkbox>
        </el-col>
        <el-col :span="15">
          <el-input
            v-model="content[index]"
            placeholder="请输入选项..."
          />
        </el-col>
      </el-row>
    </el-checkbox-group>

    <el-input placeholder="请输入内容" v-model="content[index]" v-for="(item, index) in cities" :key="item.code">
      <template slot="prepend">{{ item.code }}</template>
    </el-input>
    {{ checkedCities }}  {{ content }}
  </div>
</template>

<script>

export default {
  name: 'Welcome',
  data () {
    return {
      question: [
        {
          answer: undefined,
          content: []
        }
      ],
      cities: [
        {
          code: 1,
          name: '上海'
        },
        {
          code: 2,
          name: '北京'
        },
        {
          code: 3,
          name: '广州'
        },
        {
          code: 4,
          name: '深圳'
        }
      ],
      checkedCities: [],
      content: []
    }
  }
}
</script>

<style >
.el-row {
  align-items: center;
}
.el-row .el-col {
  height: 100% !important;
}
</style>
