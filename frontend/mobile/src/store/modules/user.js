import { defineStore } from 'pinia'
import { ref } from 'vue'
export const useUserStore = defineStore(
  'user',
  () => {
    const addr = ref('')
    const setDefault = ref(false)
    const note = ref('')
    const deliver_or_dining = ref(true)
    const clear = () => {
      addr.value = ''
    }
    return {
      addr,
      setDefault,
      note,
      clear,
      deliver_or_dining
    }
  },
  { persist: true }
)
