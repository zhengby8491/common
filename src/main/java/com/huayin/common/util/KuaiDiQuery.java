package com.huayin.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <pre>
 * 快递URL：http://api.ickd.cn/?id=[]&secret=[]&com=[]&nu=[]&type=[]&encode=[]&ord=[]&lang=[]
 * </pre>
 * @author think
 * @version 1.0, 2016年3月1日
 */
public class KuaiDiQuery
{
	public static final String URL="http://api.ickd.cn/";
	public static final String ID="201359";
	public static final String SECRET="434ab02a0573d3afa1117a36aa6775aa";
	public static final String ENCODE="UTF-8";
	
	
	public static void main(String[] args)
	{
		System.out.println(KuaiDiQuery.query("yunda","1202028468175"));
	}

	/**
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * @param company 公司
	 * @param orderNo 订单号
	 * @param resturnType 返回类型  html | json（默认） | text | xml
	 * @return
示例：
{"status":"3","message":"","errCode":"0","data":[{"time":"2013-02-23 17:10","context":"辽宁省大连市中山区四部公司 的收件员 王光 已收件"},{"time":"2013-02-24 17:59","context":"辽宁省大连市公司 已收入"},{"time":"2013-02-24 18:11","context":"辽宁省大连市中山区四部公司 已收件"},{"time":"2013-02-26 07:33","context":"吉林省长春市景阳公司 的派件员 张金达 派件中 派件员电话15948736487"},{"time":"2013-02-26 16:47","context":"客户 同事收发家人 已签收 派件员 张金达"}],"html":"","mailNo":"7151900624","expTextName":"圆通快递","expSpellName":"yuantong","update":"1362656241","cache":"186488","ord":"ASC"}
字段说明
字段	类型	说明
status	int	查询结果状态，0|1|2|3|4，0表示查询失败，1正常，2派送中，3已签收，4退回,5其他问题
errCode	int	错误代码，0无错误，1单号不存在，2验证码错误，3链接查询服务器失败，4程序内部错误，5程序执行错误，6快递单号格式错误，7快递公司错误，10未知错误
message	string	错误消息
data	array	进度
html	string	其他HTML，该字段不一定存在
mailNo	string	快递单号
expSpellName	string	快递公司英文代码
expTextName	string	快递公司中文名
update	int	最后更新时间（unix 时间戳）
cache	int	缓存时间，当前时间与 update 之间的差值，单位为：秒
ord	string	排序，ASC | DESC
	 */
	public static String query(String company,String orderNo)
	{
		//id=[]&secret=[]&com=[]&nu=[]&type=[]&encode=[]&ord=[]&lang=[]
		Map<String,String> params=new HashMap<String,String>();
		params.put("id", ID);
		params.put("secret", SECRET);
		params.put("com", company);
		params.put("nu", orderNo);
		params.put("encode", ENCODE);
		return HttpRequestProxy.doGet(URL,params, ENCODE);
	}
	
	/**
	 *   $expresses=array (

    'ems'=>'EMS快递'

    'shentong'=>'申通快递'

    'shunfeng'=>'顺丰快递'

    'yuantong'=>'圆通快递'

    'yunda'=>'韵达快递'

    'huitong'=>'汇通快递'

    'tiantian'=>'天天快递'

    'zhongtong'=>'中通快递'

    'zhaijisong'=>'宅急送快递'

    'pingyou'=>'中国邮政'

    'quanfeng'=>'全峰快递'

    'guotong'=>'国通快递'

    'jingdong'=>'京东快递'

    'sure'=>'速尔快递'

    'kuaijie'=>'快捷快递'

    'ririshun'=>'日日顺物流'

    'zhongtie'=>'中铁快运'

    'yousu'=>'优速快递'

    'longbang'=>'龙邦快递'

    'debang'=>'德邦物流'

    'rufeng'=>'如风达快递'

    'lianhaotong'=>'联昊通快递'

    'fedex'=>'国际Fedex'

    'fedexcn'=>'Fedex国内'

    'dhl'=>'DHL快递'

    'xinfeng'=>'信丰快递'

    'eyoubao'=>'E邮宝'

    'zhongxinda'=>'忠信达快递'

    'changtong'=>'长通物流'

    'usps'=>'USPS快递'

    'huaqi'=>'华企快递'

    'zhima'=>'芝麻开门快递'

    'gnxb'=>'邮政小包'

    'nell'=>'尼尔快递'

    'zengyi'=>'增益快递'

    'yuxin'=>'宇鑫物流'

    'xingchengzhaipei'=>'星程宅配快递'

    'anneng'=>'安能物流'

    'dada'=>'大达物流'

    'tongzhishu'=>'高考录取通知书'

    'aol'=>'AOL快递'

    'dongjun'=>'成都东骏快递'

    'suning'=>'苏宁快递'

    'quanyi'=>'全一快递'

    'huayu'=>'华宇物流'

    'quanritong'=>'全日通快递'

    'fengcheng'=>'丰程物流'

    'minhang'=>'民航快递'

    'zhongyou'=>'中邮物流'

    'wanjia'=>'万家物流'

    'jiaji'=>'佳吉快运'

    'wanxiang'=>'万象物流'

    'beihai'=>'贝海国际快递'

    'junfeng'=>'墨尔本骏丰快递'

    'junda'=>'骏达快递'

    'quanxintong'=>'全信通快递'

    'ups'=>'UPS快递'

    'tnt'=>'TNT快递'

    'yibang'=>'一邦快递'

    'shenghui'=>'盛辉物流'

    'yafeng'=>'亚风快递'

    'dsu'=>'D速快递'

    'datian'=>'大田物流'

    'jiayi'=>'佳怡物流'

    'jiayunmei'=>'加运美快递'

    'quanchen'=>'全晨快递'

    'ocs'=>'OCS快递'

    'shengfeng'=>'盛丰物流'

    'xinbang'=>'新邦物流'

    'chengguang'=>'程光快递'

    'fengda'=>'丰达快递'

    'feihang'=>'原飞航物流'

    'jinyue'=>'晋越快递'

    'yuefeng'=>'越丰快递'

    'anjie'=>'安捷快递'

    'aae'=>'AAE快递'

    'yuntong'=>'运通中港快递'

    'dpex'=>'DPEX快递'

    'yuancheng'=>'远成物流'

    'gdyz'=>'广东邮政物流'

    'aramex'=>'Aramex国际快递'

    'intmail'=>'国际邮政快递'

    'ytfh'=>'北京一统飞鸿快递'

    'lejiedi'=>'乐捷递快递'

    'santai'=>'三态速递'

    'chuanzhi'=>'传志快递'

    'gongsuda'=>'共速达物流|快递'

    'ees'=>'百福东方物流'

    'scs'=>'伟邦(SCS)快递'

    'pinganda'=>'平安达'

    'yad'=>'源安达快递'

    'disifang'=>'递四方速递'

    'yinjie'=>'顺捷丰达快递'

    'jldt'=>'嘉里大通物流'

    'coe'=>'东方快递'

    'chuanxi'=>'传喜快递'

    'feibao'=>'飞豹快递'

    'jingguang'=>'京广快递'

    'feiyuan'=>'飞远物流'

    'cszx'=>'城市之星'

    'rpx'=>'RPX保时达'

    'citylink'=>'CityLinkExpress'

    'chengshi100'=>'城市100'

    'lijisong'=>'成都立即送快递'

    'balunzhi'=>'巴伦支'

    'dayang'=>'大洋物流快递'

    'diantong'=>'店通快递'

    'fanyu'=>'凡宇快递'

    'haosheng'=>'昊盛物流'

    'hebeijianhua'=>'河北建华快递'

    'jixianda'=>'急先达物流'

    'suijia'=>'穗佳物流'

    'shengan'=>'圣安物流'

    'saiaodi'=>'赛澳递'

    'haihong'=>'山东海红快递'

    'weitepai'=>'微特派'

    'chengji'=>'城际快递'

    'fardar'=>'Fardar'

    'henglu'=>'恒路物流'

    'hwhq'=>'海外环球快递'

    'jinda'=>'金大物流'

    'kuayue'=>'跨越快递'

    'kcs'=>'顺鑫(KCS)快递'

    'mingliang'=>'明亮物流'

    'minbang'=>'民邦快递'

    'minsheng'=>'闽盛快递'

    'xiyoute'=>'希优特快递'

    'xianglong'=>'祥龙运通快递'

    'yishunhang'=>'亿顺航快递'

    'benteng'=>'成都奔腾国际快递'

    'zhongtian'=>'济南中天万运'

    'zhengzhoujianhua'=>'郑州建华快递'

    'feite'=>'飞特物流'

    'huahan'=>'华翰物流'

    'baotongda'=>'宝通达'

    'chukouyi'=>'出口易物流'

    'yumeijie'=>'誉美捷快递'

    'kuanrong'=>'宽容物流'

    'nanbei'=>'南北快递'

    'wanbo'=>'万博快递'

    'suchengzhaipei'=>'速呈宅配'

    'shengbang'=>'晟邦物流'

    'baiqian'=>'百千诚国际物流'

    'gaotie'=>'高铁快递'

    'guanda'=>'冠达快递'

    'haolaiyun'=>'好来运快递'

    'hutong'=>'户通物流'

    'huahang'=>'华航快递'

    'huangmajia'=>'黄马甲快递'

    'ucs'=>'合众速递'

    'jiete'=>'捷特快递'

    'jiuyi'=>'久易快递'

    'kuaiyouda'=>'快优达速递'

    'lanhu'=>'蓝弧快递'

    'menduimen'=>'门对门快递'

    'peixing'=>'陪行物流'

    'riyu'=>'日昱物流'

    'lindao'=>'上海林道货运'

    'shiyun'=>'世运快递'

    'aoshuo'=>'奥硕物流'

    'nsf'=>'新顺丰（NSF）快递'

    'dajin'=>'大金物流'

    'coscon'=>'中国远洋运输(COSCON)'

    'yuhong'=>'宇宏物流'

    'jiayu'=>'佳宇物流'

    'gangkuai'=>'港快速递'

    'kuaitao'=>'快淘速递'

    'sutong'=>'速通物流'

    'anxun'=>'安迅物流'

    'hkpost'=>'香港邮政'

    'jppost'=>'日本邮政'

    'singpost'=>'新加坡邮政'

    'ztwl'=>'中铁物流'

    'ppbyb'=>'贝邮宝'

    'yanwen'=>'燕文物流'

    'feiyang'=>'飞洋快递'

    'zuochuan'=>'佐川急便'

    'hengyu'=>'恒宇运通'

    'mengsu'=>'蒙速快递'

    'wuhuan'=>'五环速递'

    'simai'=>'思迈快递'

    'jiahuier'=>'佳惠尔快递'

    'ande'=>'安得物流'

    'rongqing'=>'荣庆物流'

    'dashun'=>'大顺物流'

    'fangfangda'=>'方方达物流'

    'huiwen'=>'汇文快递'

    'sujie'=>'速捷快递'

    'dhlde'=>'德国DHL快递'

    'baiteng'=>'百腾物流'

    'dcs'=>'DCS快递'

    'dpd'=>'DPD快递'

    'tengxunda'=>'腾迅达物流'

    'pinjun'=>'品骏快递'

    'bse'=>'蓝天快递'

    'ueq'=>'UEQ快递'

    'xru'=>'XRU快递'

    'ycky'=>'远成快运'

    'anpost'=>'爱尔兰邮政'

    'koreapost'=>'韩国邮政'

    'posten'=>'挪威邮政'

    'auspost'=>'澳大利亚邮政'

    'posteit'=>'意大利邮政'

    'nzpost'=>'新西兰邮政'

    'bpost'=>'比利时邮政'

    'depost'=>'德国邮政'

    'correos'=>'西班牙邮政'

    'postnl'=>'荷兰邮政'

    'thailand'=>'泰国邮政'

    'nengda'=>'能达快递'

    'ruifeng'=>'瑞丰速递'

    'suteng'=>'速腾快递'

    'zongxing'=>'纵行物流'

    'jingshi'=>'京世物流'

    'huacheng'=>'华诚物流'

          );
	 */
}
