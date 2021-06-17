<template>
  <div class="editor">
    <div id="e" >
    </div>
  </div>
</template>

<script>
import E from 'wangeditor'
export default {
  name: 'editor',
  props: {
    content: {
      type: String,
      default: ''
    },
    // 业务中我们经常会有添加操作和编辑操作，添加操作时，我们需清除上一操作留下的缓存
    isClear: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    isClear (val) {
      if (val) {
        this.editor.txt.clear()
      }
    },
    content (value) {
      if (value !== this.editor.txt.html()) {
        this.editor.txt.html(this.content)
      }
    }
  },
  data () {
    return {
      editor: null
    }
  },
  methods: {
    initE () {
      this.editor = new E('#e')
      // 单位 ms
      this.editor.config.onchangeTimeout = 1000
      this.editor.config.uploadFileName = 'image'
      // 你的服务器地址
      this.editor.config.uploadImgServer = 'http://localhost:9010/examAuthCore/uploadImage'
      // 图片上传钩子函数
      this.editor.config.uploadImgHooks = {
        before: function (xhr, editor, files) {
          // 图片上传之前触发
          // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，files 是选择的图片文件

          // 如果返回的结果是 {prevent: true, msg: 'xxxx'} 则表示用户放弃上传
          // return {
          //     prevent: true,
          //     msg: '放弃上传'
          // }
        },
        success: function (xhr, editor, result) {
          // 图片上传并返回结果，图片插入成功之后触发
          // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
        },
        fail: function (xhr, editor, result) {
          // 图片上传并返回结果，但图片插入错误时触发
          // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
        },
        error: function (xhr, editor) {
          // 图片上传出错时触发
          // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象
        },
        timeout: function (xhr, editor) {
          // 图片上传超时时触发
          // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象
        },
        customInsert: function (insertImg, result, editor) {
          const url = result.data.url
          insertImg(url)
        }
      }
      // 将内容同步到父组件中
      this.editor.config.onchange = (html) => {
        this.$emit('change', html)
      }
      // 设置菜单
      this.editor.config.menus = [
        // 标题
        'head',
        // 粗体
        'bold',
        'fontSize', // 字号
        'fontName', // 字体
        'italic', // 斜体
        'underline', // 下划线
        'strikeThrough', // 删除线
        'foreColor', // 文字颜色
        'backColor', // 背景颜色
        'link', // 插入链接
        'list', // 列表
        'justify', // 对齐方式
        'quote', // 引用
        'emoticon', // 表情
        'image', // 插入图片
        'table', // 表格
        'code', // 插入代码
        'undo', // 撤销
        'redo' // 重复
      ]
      // 创建编辑器
      this.editor.create()
      this.editor.txt.html(this.content)
    }
  },
  mounted () {
    this.initE()
  }
}
</script>

<style scoped>

</style>
