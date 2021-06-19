<template>
  <div class="title">
    <h3 class="header_title">馆长全国综合考试系统</h3>
    <el-divider></el-divider>
  </div>
</template>

<script>

export default {
  name: 'TitleHeader',
  props: {
    dividerWidth: {
      type: Number,
      default: 70
    }
  }
}
</script>

<style lang="less" scoped>
// // :style="{width: dividerWidth + '%'}
.header_title {
  font-size: 30px;
  color: #000;
  margin: 40px auto;
  text-align: center;
  font-weight: 700;
  letter-spacing: 17px;
}
.el-divider {
  background-color: #1be;
  width: 70%;
  height: 4px;
  margin: 40px auto;
}
</style>
