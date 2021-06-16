<template>
  <div>
    <h2>Welcome</h2>
    <editor :desc="content"  @change="change"></editor>
  </div>
</template>

<script>
import Editor from '@/components/editor'
export default {
  name: 'Welcome',
  components: { Editor },
  data () {
    return {
      isClear: false,
      content: 'aaa'
    }
  },
  methods: {
    change (val) {
      this.content = val
      console.log(this.content)
    }
  }
}
</script>

<style >

</style>
