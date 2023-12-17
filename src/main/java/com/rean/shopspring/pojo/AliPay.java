package com.rean.shopspring.pojo;

import lombok.Data;

/**
 * @Author
 * @Date Created in  2023/5/5 15:26
 * @DESCRIPTION:
 * @Version V1.0
 */
@Data
public class AliPay {
    private String orderId;
    private String redirect;
    private double totalAmount;
    private String subject;
    private String email;
    private String alipayTraceNo;
}
