package com.example.administrator.bazipaipan.utils;

/**
 * 全局静态变量工具类
 *
 * 网络请求及地址相关
 */
public class ConstantUtil {
    public static final String IP_PORT = "http://118.194.244.20:9999";//公司映射

    //    public static final String URL = "/OnlineParking-0.0.1/rest/";//子恒映射
    public static final String URL = "/OnlineParking/rest/";
    /**
     * 获取验证码服务
     */
    public static final String URL_VERIFICATION = IP_PORT + URL + "getVeriCode";
    /**
     * 登录服务
     */
    public static final String URL_LOGIN = IP_PORT + URL + "userLogin";
    /**
     * 登录服务
     */
    public static final String URL_ADDREGISTRATIONID = IP_PORT + URL + "addRegistrAtionId";
    /**
     * 更换手机号
     */
    public static final String URL_CHANGEPHONE = IP_PORT + URL + "upUserPhone";

    /**
     * 检测手机号
     */
    public static final String URL_CHECKPHONE = IP_PORT + URL + "hasPhoneNumber";

    /**
     * 更新用户信息
     */
    public static final String URL_UPDATEPERSONDATA = IP_PORT + URL + "updateUser";

    /**
     * 添加车辆信息
     */
    public static final String URL_CARADD = IP_PORT + URL + "insertUserCar";

    /**
     * 删除车辆信息
     */
    public static final String URL_CARDEL = IP_PORT + URL + "deleteUserCarByCarManageId";

    /**
     * 更新车辆信息
     */
    public static final String URL_CARUPDATE = IP_PORT + URL + "upUserCarActiveMark";

    /**
     * 查询车辆是否存在
     */
    public static final String URL_CARSEARCH = IP_PORT + URL + "hasUserCarNo";

    /**
     * 查询所有车辆车辆信息
     */
    public static final String URL_CARSELECT = IP_PORT + URL + "selectUserCar";

    /**
     * 查询钱包余额信息
     * 更新用户资金金额
     */
    public static final String URL_WALLETMONEY = IP_PORT + URL + "upFundAccountManageAmount";

    /**
     * 查询订单信息
     */
    public static final String URL_ORDER = IP_PORT + URL + "selectParkingOrderByUserId";

    /**
     * 消息记录
     */
    public static final String URL_MESSAGE = IP_PORT + URL + "quertMessageByUserIdWhereStatus";


    /**
     * 意见反馈接口
     */
    public static final String URL_RECHARGE = IP_PORT + URL + "creatChargingOrder";


    /**
     * 意见反馈接口
     */
    public static final String URL_SUGGESTION = IP_PORT + URL + "createOpinion";

    /**
     * 检测自动支付设置
     */
    public static final String URL_AUTOPAYMENT_CHECK = IP_PORT + URL + "delData";

    /**
     * 收费标准
     */
    public static final String URL_CHARGINGSTANDARDLIST = IP_PORT + URL + "getChargingStandardsListByParkingInfoId";
    /**
     * 更新自动支付设置
     */
    public static final String URL_AUTOPAYMENT_CHANGE = IP_PORT + URL + "userPayOk";
    /**
     * 现金支付
     */
    public static final String URL_ORDERPAYCASH = IP_PORT + URL + "OrderPayCash";

    /**
     * 余额提醒设置
     */
    public static final String URL_BALANCEALERTCHANGE = IP_PORT + URL + "userPay";

    /**
     * 充值记录
     */
    public static final String URL_RECHARGEHISTORY = IP_PORT + URL + "queryChargingOrderByUserId";

    /**
     * 余额查询
     */
    public static final String URL_WALLETLEFT = IP_PORT + URL + "selectByCustId";

    /**
     * 停车场查询
     */
    public static final String URL_PARKINGINFO = IP_PORT + URL + "getParkInfoByLoLa";

    /**
     * 更新apk的url
     */
    public static String URL_REQUEST_APK_INFO = "http://app.hdsxtech.com:8181/drivers/android/driver/getXjktVersion";
}
