// stores/remark.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useRemarkstore = defineStore('remark', () => {
  const remark = ref('')
  const setMark = () => {
    if (remark.value === '') remark.value = '无'
  }
  const clear = () => {
    remark.value = ''
  }
  return {
    remark,
    setMark,
    clear
  }
})
