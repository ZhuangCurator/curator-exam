<template>
  <div>
    <h2>Welcome</h2>
  </div>
</template>

<script>
export default {
  name: 'Welcome',
  data () {
    return {
      province: '',
      city: '',
      area: ''
    }
  },
  methods: {
    onChangeProvince (data) {
      console.log(data)
    },
    onChangeCity (data) {
      console.log(data)
    },
    onSelectArea (data) {
      console.log(data)
    }
  }
}
</script>

<style scoped>

</style>
