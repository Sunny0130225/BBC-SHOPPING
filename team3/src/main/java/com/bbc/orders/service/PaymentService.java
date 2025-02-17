package com.bbc.orders.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bbc.orders.dto.PaymentDTO;
import com.bbc.orders.entity.Orders;
import com.bbc.orders.repository.OrderRepository;

@Service
public class PaymentService {

	//沒用到ㄉ東東
	@Autowired
    private OrderRepository orderRepository;

    @Value("${ecpay.merchantId}")
    private String MERCHANT_ID;

    @Value("${ecpay.hashKey}")
    private String HASH_KEY;

    @Value("${ecpay.hashIv}")
    private String HASH_IV;

    @Value("${ecpay.paymentURL}")
    private String PAYMENT_URL;

    @Value("${ngrok.baseURL}")
    private String NGROK_BASEURL;
    
 // ✅ 處理訂單支付請求
    public String createPayment(PaymentDTO paymentDTO) {
        Map<String, String> params = new HashMap<>();

        // 獲取訂單
        Orders dbOrder = orderRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + paymentDTO.getOrderId()));

        if (dbOrder.getStatus().equals("未付款")) {
            String RETURN_URL = NGROK_BASEURL + "/orders/paymentResult";

            String merchantTradeNo = paymentDTO.getOrderId() + "t" + System.currentTimeMillis(); // 訂單ID+時間戳
            
            params.put("MerchantID", MERCHANT_ID);
            params.put("MerchantTradeNo", merchantTradeNo);
            params.put("MerchantTradeDate", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
            params.put("PaymentType", "aio");
            params.put("TotalAmount", paymentDTO.getFinalAmount().toString());
            params.put("TradeDesc", "信用卡支付");
            params.put("ItemName", paymentDTO.getProductName());
            params.put("ChoosePayment", "Credit");
            params.put("ReturnURL", RETURN_URL);
            params.put("ClientBackURL", "http://localhost:5174/customer/member/myOrder");
            params.put("CheckMacValue", generateCheckMacValue(params));

            // 生成表單
            StringBuilder form = new StringBuilder();
            form.append("<form id='ecpay-form' action='").append(PAYMENT_URL).append("' method='post'>");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                form.append("<input type='hidden' name='").append(entry.getKey()).append("' value='").append(entry.getValue()).append("'>");
            }
            form.append("<button type='submit'>前往付款</button>");
            form.append("</form>");
            form.append("<script>document.getElementById('ecpay-form').submit();</script>");

            return form.toString();
        } else {
            return "訂單狀態無法進行付款";
        }
    }

    // ✅ 處理支付回調
    public void processPaymentReturn(Map<String, String> responseParams) {
        String receivedCheckMacValue = responseParams.get("CheckMacValue");
        responseParams.remove("CheckMacValue");
        String myCheckMacValue = generateCheckMacValue(responseParams);

        if (!myCheckMacValue.equalsIgnoreCase(receivedCheckMacValue)) {
            throw new RuntimeException("CheckMacValue 驗證失敗");
        }

        String orderId = responseParams.get("MerchantTradeNo").split("t")[0];
        String rtnCode = responseParams.get("RtnCode");

        if ("1".equals(rtnCode)) {
            Orders dbOrder = orderRepository.findById(Integer.parseInt(orderId))
                    .orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + orderId));
            dbOrder.setStatus("已付款");
            orderRepository.save(dbOrder);
        } else {
            throw new RuntimeException("支付失敗");
        }
    }

    // ✅ 產生 CheckMacValue
    private String generateCheckMacValue(Map<String, String> params) {
        String sortedParams = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        String raw = "HashKey=" + HASH_KEY + "&" + sortedParams + "&HashIV=" + HASH_IV;
        String encoded = URLEncoder.encode(raw, StandardCharsets.UTF_8).toLowerCase();
        return encryptMD5(encoded).toUpperCase();
    }

    // ✅ MD5 加密
    private String encryptMD5(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5 加密失敗", e);
        }
	
}
    }
