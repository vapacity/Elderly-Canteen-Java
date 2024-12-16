package com.javaee.elderlycanteen.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;  // 使用 Gson 库来将 Map 转换为 JSON 字符串

public class PythonScriptUtils {

    // 调用Python脚本的函数
    public static String runPythonScript(Map<Integer, String> menuItems, String orderText) {
        try {
            // 构造Python脚本的命令
            ProcessBuilder processBuilder = new ProcessBuilder("python","E:\\Projects\\JavaEE\\Elderly-Canteen-Java\\src\\main\\java\\com\\javaee\\elderlycanteen\\script\\order_processing.py");

            // 转换菜单数据为JSON字符串
            Gson gson = new Gson();
            String menuJsonString = gson.toJson(menuItems);

            // 设置环境变量和传递参数
            // 如果你有特定的 Python 环境路径，可以设置 PYTHONPATH
            processBuilder.environment().put("PYTHONPATH", "E:\\Projects\\JavaEE\\Elderly-Canteen-Java\\env\\venv");

            // 启动Python脚本
            Process process = processBuilder.start();

            // 向Python脚本传递输入（菜单和点餐文本）
            OutputStream os = process.getOutputStream();
            String input = menuJsonString + " " + orderText; // 传递菜单 JSON 和点餐文本
            os.write(input.getBytes());
            os.flush();
            os.close();

            // 获取Python脚本的输出结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 等待脚本执行完成
            process.waitFor();

            // 返回Python脚本的输出
            return output.toString();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 测试调用方法
    public static void main(String[] args) {
        // 创建菜单数据
        Map<Integer, String> menuItems = new HashMap<>();
        menuItems.put(1, "burger");
        menuItems.put(2, "pizza");
        menuItems.put(3, "pasta");
        menuItems.put(4, "salad");
        menuItems.put(5, "sushi");
        menuItems.put(6, "fries");

        // 点餐文本
        String orderText = "I would like two burgers and one pizza";

        // 调用Python脚本
        String result = runPythonScript(menuItems, orderText);

        // 打印返回结果
        System.out.println("Result from Python script:");
        System.out.println(result);
    }
}
