<template>
  <div>
    <h2>Welcome</h2>
    <editor v-model="detail" :isClear="isClear" @change="change"></editor>
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
      detail: ''
    }
  },
  methods: {
    change (val) {
      console.log(val)
    }
  }
}
</script>

<style >

</style>
