import { fileURLToPath, URL } from 'node:url'

import vueDevTools from 'vite-plugin-vue-devtools'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import postCssPxToRem from 'postcss-pxtorem'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { VantResolver } from '@vant/auto-import-resolver'
import {
  VueUseComponentsResolver,
  VueUseDirectiveResolver
} from 'unplugin-vue-components/resolvers'

export default defineConfig({
  base: '/mobile/',
  server: { proxy: { '/demo-api': 'http://127.0.0.1:18180' } },
  plugins: [
    vue(),
    vueDevTools(),
    AutoImport({
      imports: ['vue', 'vue-router', 'pinia', '@vueuse/core'],
      resolvers: [VantResolver()]
    }),
    Components({
      dts: true,
      dirs: ['src/components', 'src/views'], // 按需加载的文件夹
      resolvers: [VantResolver(), VueUseComponentsResolver(), VueUseDirectiveResolver()]
    })
  ],
  css: {
    postcss: {
      plugins: [
        postCssPxToRem({
          rootValue: 37.5,
          propList: ['*']
        })
      ]
    }
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
