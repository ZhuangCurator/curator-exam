<template>
  <div class="home">
    <el-main>
        <!-- 路由占位符 -->
        <router-view/>
    </el-main>
  </div>
</template>

<script>

export default {
  name: 'Home'
}
</script>

<style lang="less">
.el-main {
  width: 100%;
  height: 100%;
}
</style>
