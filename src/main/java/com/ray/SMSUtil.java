package com.ray;


import com.ray.config.Constant;
import com.ray.entity.PhoneSms;
import org.smslib.AGateway;
import org.smslib.AGateway.Protocols;
import org.smslib.IInboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Message.MessageTypes;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SMSUtil {

    private   Service SRV = Service.getInstance();

    private  Map<String,String> COM_PHONE_MAP = new HashMap<String,String>();

    private  Map<String,String> PHONE_COM_MAP = new HashMap<String,String>();

    public class InboundNotification implements IInboundMessageNotification {
        public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg) {
            if (msg.getMemIndex() > 1) {
                PhoneSms phoneSms = new PhoneSms();
                phoneSms.setContent(msg.getText());
                phoneSms.setPhone(//Constant.
                        COM_PHONE_MAP.get(msg.getGatewayId()));
                phoneSms.setReceiveTime(msg.getDate());
                phoneSms.setSendPhone(msg.getOriginator());
                if (NowHttpUtil.sendMsg(phoneSms)) {
                    try {
                        //Constant.
                                SRV.deleteMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(msg);
        }
    }

    public void doIt(List<String> comList) throws Exception {
        // ---------------创建串口设备，如果有多个，就创建多个--------------
        // 1、连接网关的id
        // 2、com口名称，如COM1或/dev/ttyS1（根据实际情况修改）
        // 3、串口波特率，如9600（根据实际情况修改）
        // 4、开发商0
        // 5、型号
        InboundNotification inboundNotification = new InboundNotification();
        List<SerialModemGateway> gatewayList = new ArrayList<SerialModemGateway>();
        for (String com : comList) {
            SerialModemGateway gateway = new SerialModemGateway(com, com, 9600, "null", "null");
            gateway.setProtocol(Protocols.PDU);
            // 设置true，表示该网关可以接收短信,根据需求修改
            gateway.setInbound(true);
            // 设置true，表示该网关可以发送短信,根据需求修改
            gateway.setOutbound(true);
            // sim卡锁，一般默认为0000或1234
            gateway.setSimPin("1234");
            gatewayList.add(gateway);
        }
        try {

            for (SerialModemGateway gateway : gatewayList) {
                //Constant.
                        SRV.addGateway(gateway);
            }
            //Constant.
                    SRV.setInboundMessageNotification(inboundNotification);
            // 将网关添加到短信猫服务中
            // 启动服务
            //Constant.
                    SRV.startService();
            System.out.println("---------------------------服务启动完毕----------------------------");
            // 设置加密
            //srv.getKeyManager().registerKey("短信中心号码", new AESKey(new SecretKeySpec("0011223344556677".getBytes(), "AES")));

            List<InboundMessage> msgList = new ArrayList<InboundMessage>();
            // 读取短信
            //Constant.
                    SRV.readMessages(msgList, MessageClasses.ALL);
            for (InboundMessage msg : msgList) {
                if (msg.getMemIndex() == 1) {
                    //Constant.
                            COM_PHONE_MAP.put(msg.getGatewayId(), msg.getText());
                    //Constant.
                            PHONE_COM_MAP.put(msg.getText(), msg.getGatewayId());
                }
            }
            System.out.println("Now Sleeping - Hit <enter> to stop service.");
            System.in.read();
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Constant.
                    SRV.stopService();
            for (SerialModemGateway gateway : gatewayList) {
                //Constant.
                        SRV.removeGateway(gateway);
            }
        }
    }

    public void deleteAllMsg(List<String> comList) throws Exception {
        // ---------------创建串口设备，如果有多个，就创建多个--------------
        // 1、连接网关的id
        // 2、com口名称，如COM1或/dev/ttyS1（根据实际情况修改）
        // 3、串口波特率，如9600（根据实际情况修改）
        // 4、开发商0
        // 5、型号
        List<SerialModemGateway> gatewayList = new ArrayList<SerialModemGateway>();
        for (String com : comList) {
            SerialModemGateway gateway = new SerialModemGateway(com, com, 9600, "null", "null");
            gateway.setProtocol(Protocols.PDU);
            // 设置true，表示该网关可以接收短信,根据需求修改
            gateway.setInbound(true);
            // 设置true，表示该网关可以发送短信,根据需求修改
            gateway.setOutbound(true);
            // sim卡锁，一般默认为0000或1234
            gateway.setSimPin("1234");
            gatewayList.add(gateway);
        }
        try {

            for (SerialModemGateway gateway : gatewayList) {
                //Constant.
                        SRV.addGateway(gateway);
            }
            // 将网关添加到短信猫服务中
            // 启动服务
            //Constant.
                    SRV.startService();
            System.out.println("---------------------------服务启动完毕----------------------------");
            // 设置加密
            //srv.getKeyManager().registerKey("短信中心号码", new AESKey(new SecretKeySpec("0011223344556677".getBytes(), "AES")));

            List<InboundMessage> msgList = new ArrayList<InboundMessage>();
            // 读取短信
            //Constant.
                    SRV.readMessages(msgList, MessageClasses.ALL);
            for (InboundMessage msg : msgList) {
                //Constant.
                        SRV.deleteMessage(msg);
            }
            System.out.println("Now Sleeping - Hit <enter> to stop service.");
            System.in.read();
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Constant.
                    SRV.stopService();
            for (SerialModemGateway gateway : gatewayList) {
                //Constant.
                        SRV.removeGateway(gateway);
            }
        }
    }

    public static void main(String args[]) throws Exception {
        SMSUtil app = new SMSUtil();
        List<String> list = new ArrayList<String>();
//        list.add("COM4");
//        list.add("COM5");
        list.add("COM6");
        list.add("COM7");
        app.doIt(list);
    }
}