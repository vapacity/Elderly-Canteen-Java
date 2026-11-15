// stores/menu.js
import { defineStore } from 'pinia'

export const useMenuStore = defineStore('menu', {
  state: () => ({
    items: []
  }),
  getters: {
    totalNum: (state) => {
      return state.items.reduce((total, item) => total + item.quantity, 0)
    }
  },
  actions: {
    addItem(item) {
      const existingItem = this.items.find((i) => i.dishName === item.dishName)
      if (existingItem) {
        existingItem.quantity += 1
      } else {
        this.items.push({ ...item, quantity: item.quantity || 1 })
      }
      console.log(this.items)
    },
    clear() {
      this.items = []
    },
    minusItem(item) {
      const existingIndex = this.items.findIndex((i) => i.dishName === item.dishName)
      if (existingIndex !== -1) {
        if (this.items[existingIndex].quantity > 1) {
          this.items[existingIndex].quantity -= 1
        } else {
          this.items.splice(existingIndex, 1)
        }
      } else {
        console.log(`Item with name "${item.dishName}" not found.`)
      }
    },
    getItemCount(name) {
      const item = this.items.find((item) => item.dishName === name)
      return item ? item.quantity : 0
    }
  },
  persist: true // 配置持久化
})
