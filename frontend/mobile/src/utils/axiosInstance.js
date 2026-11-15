import axios from 'axios'

const axiosInstance = axios.create({
  // 网络请求的公共配置
  // baseURL: 'http://127.0.0.1:4523/m1/4808550-4462943-default',
  baseURL: '',
  timeout: 10000 // 请求超时时间
})

// 请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    // 在发送请求之前做些什么
    // 比如添加token到请求头
    const token = localStorage.getItem('token')
    config.headers.Authorization = `Bearer ${token}`
    return config
  },
  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 响应拦截器
axiosInstance.interceptors.response.use(
  // 获得响应成功
  (response) => response,
  (error) => {
    // 对响应错误做些什么
    return Promise.reject(error)
  }
)

export default axiosInstance
