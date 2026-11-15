// import { Toast } from 'vant'

import axios from 'axios'
import querystring from 'querystring'

const errorHandle = (status,info)=>{
	switch(status){
		case 406:
			console. log("语义有误");break;
		case 401:
			console. log("服务器认证失败");break;
		case 403:
			console.log("服务器拒绝访问");break;
		case 404:
			console.log("地址错误");break;
		case 500:
			console.log("服务器遇到意外");break;
		case 502:
			console.log("服务器无响应");break;
		default:
			console.log(info);break ;
}

const instance = axios.create({
  // 网络请求的公共配置
  // baseURL: '',
  // 统一超时时间，如有某个接口需要设置不同超时时间，可在调用接口是传入超时时间来覆盖这个超时时间
  timeout: 5000,
  // 请求是否携带cookie
  withCredentials: true
})

// 拦截器是最常用的（在请求或响应被 then 或 catch 处理前拦截它们。）
// 添加请求拦截器（发送请求之前）
instance.interceptors.request.use(
  // 发送请求成功
  config => {
    if (config.methods === 'post') {
      config.data = querystring.stringify(config.data)
    }
    // config:包含着网络请求的所有信息
    return config
  },
  // 发送请求失败
  error => {
    return Promise.reject(error)
  }
)

// 添加响应拦截器（获得响应之前）
instance.interceptors.response.use(
  // 获得响应成功
  response => {
    return response.status === 200
      ? Promise.resolve(response)
      : Promise.reject(response)
    // status=200表示获得数据成功
  },
  // 获得响应失败（服务器返回非 2xx 状态码，网络错误）
  error => {
    // 从 error 对象中提取 response 属性，如果服务器有响应（即使状态码不是 2xx），这个 response 属性就会存在。反之不存在
    const { response } = error

    if (response) {
      // 服务器有响应，但状态码非 2xx
      errorHandle(response.status, response.data)
    } else {
      // 服务器没有响应
      console.error('Network Error')
    }
    return Promise.reject(error)
  }
)

export default instance
