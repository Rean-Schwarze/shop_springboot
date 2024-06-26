package com.rean.shopspring.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.rean.shopspring.config.AliPayConfig;
import com.rean.shopspring.mapper.OrderMapper;
import com.rean.shopspring.mapper.UserMapper;
import com.rean.shopspring.pojo.AliPay;
import com.rean.shopspring.pojo.Result;
import com.rean.shopspring.service.EmailService;
import com.rean.shopspring.service.OrderService;
import com.rean.shopspring.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.springframework.mail.javamail.JavaMailSender;

@RestController
public class AliPayController {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderService orderService;

//    生成支付页面
    @CrossOrigin
    @GetMapping("/pay/aliPay") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public String pay(AliPay aliPay) {
        AlipayTradePagePayResponse response; // 创建交易页面
        try {
            //  发起API调用
            int orderState=orderMapper.getOrderStateById(Integer.parseInt(aliPay.getOrderId()));
            if(orderState==1){
                aliPay.setSubject("Test Pay");
                aliPay.setTotalAmount(orderMapper.getPayMoneyById(Integer.parseInt(aliPay.getOrderId())));
                response = Factory.Payment.Page()
                        .pay(aliPay.getSubject(), aliPay.getOrderId(), String.valueOf(aliPay.getTotalAmount()),
                                aliPay.getRedirect());
//                String email= aliPay.getEmail();
//                emailService.sendTextMailMessage(email,"感谢购买","感谢您的购买，以下是订单信息：\n订单号："+aliPay.getOrderId()+"\n订单总额："+ aliPay.getTotalAmount());
            }
            else{
                throw new Exception("订单已支付！");
            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return response.getBody();
    }

//    支付宝回调结果处理
    @CrossOrigin
    @RequestMapping("/paycallback")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
                // System.out.println(name + " = " + request.getParameter(name));
            }

            String tradeNo = params.get("out_trade_no");
            String gmtPayment = params.get("gmt_payment");
            String alipayTradeNo = params.get("trade_no");

            // 支付宝验签
            if (Factory.Payment.Common().verifyNotify(params)) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

                // 更新订单状态（可能会有死锁相关问题，后面再改吧！）
                orderService.updateOrderState(Integer.parseInt(tradeNo),2);
            }
        }
        return "success";
    }
}
