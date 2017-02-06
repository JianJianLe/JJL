package com.xy.jjl.utils;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpUtil {
	
	//112.74.38.240
	//public final static String BaseUrl="http://112.74.38.240:8080/JJLserver";
	public final static String BaseUrl="http://192.168.0.112:8080/JJLserver";
	
	public final static String Login=BaseUrl 
			+ "/loginServlet";//登陆
	
/*`
	//正式环境 115.28.168.64
	//public final static String BaseUrl = "http://115.28.168.64:8080/wcXphServer";
	//public final static String BaseUrl = "http://10.0.0.8:8080/wcXphServer";
	public final static String Register = BaseUrl + "/registerServlet";// 注册
	public final static String Login = BaseUrl + "/loginServlet";// 登录
	public final static String QQLogin = BaseUrl + "/QQLoginServlet";// qq登录
	public final static String Apply = BaseUrl + "/applyServlet";// 报名咨询
	public final static String ApplyQueryPrivance = BaseUrl
			+ "/applyQueryProvinceServlet"; // 查询全国省份
	public final static String ApplyQueryCity = BaseUrl
			+ "/applyQueryCityServlet";// 查询省份 城市
	public final static String ApplyQueryCityMessage = BaseUrl
			+ "/applyQueryCityMessageServlet";// 查询城市对应的部落首领信息
	public final static String ChangePassword = BaseUrl
			+ "/changPasswordServlet";// 修改密码
	public final static String SelectCardMember = BaseUrl
			+ "/selectCardMemberServlet";// 查询是否为卡族部落会员
	public final static String SelectCardMemberInfo = BaseUrl
			+ "/selectCardMemberInfoServlet";// 查询卡族部落会员信息
	public final static String UpToCardMember = BaseUrl
			+ "/upToCardMemberServlet";// 升级为卡族部落会员
	public final static String TakeMoneyBusiness = BaseUrl
			+ "/takeMoneyBusinessServlet";// 提额商学院
	public final static String Types = BaseUrl + "/typeServlet";//
	public final static String TakeMoneySkill = BaseUrl
			+ "/takeMoneySkillServlet";// 信用卡提额技巧培训服务
	public final static String CardTribe = BaseUrl + "/cardTribeServlet";
	public final static String TakeMoneyServer = BaseUrl
			+ "/takeMoneyServerServlet";// 其它提额服务
	public final static String TakeMoneyBook = BaseUrl
			+ "/takeMoneyBookServlet";// 提额宝典
	// 查看模板信息
	public final static String SelectMoban = BaseUrl + "/selectMobanServlet";
	// 修改模板笔数
	public final static String ChangeMobanBiShu = BaseUrl
			+ "/changMobanBishuServlet";
	// 添加信用卡信息
	public final static String AddCreditCardBill = BaseUrl
			+ "/addCreditCardBillServlet";
	// 根据userid查询银行卡的账单信息
	public final static String GetCreditCardBillMeessage = BaseUrl
			+ "/getCreditCardBillMessageServlet";
	// 查询用户的信用卡
	public final static String GetCreditCard = BaseUrl
			+ "/getCreditCardServlet";

	// 生成 D账单信息
	public final static String GetBillDMessageServlet = BaseUrl
			+ "/getBillDhuanMessageServlet";
	// 显示 D账单信息
	public final static String ShowBillDMessage = BaseUrl
			+ "/showBillMessageServlet";
	// 显示 Y账单信息
	public final static String ShowBillYMessage = BaseUrl
			+ "/showBillYMessageServlet";
	// 生成 Y账单信息
	public final static String GetBillYMessageServlet = BaseUrl
			+ "/getBillYangkaMessageServlet";
	// 更新 D账单 的还款状态
	public final static String UpdateDhuanStatus = BaseUrl
			+ "/updateDhuanStateServlet";
	// 更新 Y账单的还款状态
	public final static String UpdateYStatus = BaseUrl
			+ "/updateYangStateServlet";

	// 提交瞬时贷 信息
	public final static String ImmediatelyLoanMessageCommit = BaseUrl
			+ "/AddSSDServlet";
	// 提交公司贷 信息
	public final static String CorporationLoanMessageCommit = BaseUrl
			+ "/AddGSDServlet";

	// 检查服务器上 最新apk 的版本号
	public final static String CheckServerCode = BaseUrl
			+ "/checkVersionServlet";
	// 下载新版本apk
	public final static String UploadVersion = BaseUrl
			+ "/UploadVersionServlet";
	// 删除卡信息
	public final static String DeleteCardMessage = BaseUrl
			+ "/DelctCreditCardServlet";
	// 根据邮箱获取账单
	public final static String LoadEmailBill = BaseUrl + "/LoadEmailServlet";
	// 根据用户id 获取用户邮箱列表
	public final static String ShowUserEmail = BaseUrl
			+ "/ShowUserEmailServlet";
	// 根据用户id 获取用户信用卡信息列表
	public final static String ShowUserCreditCardMessage = BaseUrl
			+ "/ShowCreditCradServlet";
	// 根据用户id和邮箱地址删除邮箱
	public final static String DeleteUserEmail = BaseUrl
			+ "/DeleteEmailServlet";
	// 根据信用卡id 删除信用卡账单
	public final static String DeleteCreditCardtBill = BaseUrl
			+ "/DeleteCreditCardServlet";
	// 根据信用卡id 获取具体信用卡账单
	public final static String QueryBillsMessage = BaseUrl
			+ "/QueryBillsTradeServlet";
	// 查询光荣榜信息
	public final static String QueryHonorRollMessage = BaseUrl
			+ "/QueryGRBServlet";
	// 查询广告墙信息
	public final static String QueryAdvertisementMessage = BaseUrl
			+ "/QueryGGQServlet";
	// 修改验证码 状态
	public final static String UpdateSecurityCode = BaseUrl
			+ "/UpdateKeyTypeServlet";
	// 查询是否已经激活
	public final static String QueryIsActivation = BaseUrl
			+ "/QueryActivationIsUserIdServlet";
	// 查询推荐人姓名
	public final static String QueryReferrer = BaseUrl
			+ "/QueryReferrerServlet";
	// 绑定QQ
	public final static String BindingQQ = BaseUrl + "/BindingQQServlet";
	// 更新账单 还款状态 （设为已还 未还）
	public final static String UpdateRepaymentState = BaseUrl
			+ "/UpdateRepaymentStateServlet";
	// 查询卡族贷信息
	public final static String KazudaiSelect = BaseUrl
			+ "/KazudaiSelectServlet";
	// 添加卡族贷个人信息
	public final static String KazudaiAddPersonal = BaseUrl
			+ "/KazudaiAddPersonalServlet";
	// 添加卡族贷公司信息
	public final static String KazudaiAddCompany = BaseUrl
			+ "/KazudaiAddCompanyServlet";
	// 添加卡族贷资金需求
	public final static String KazudaiFundDemand = BaseUrl
			+ "/KazudaiFundDemandServlet";
	// 添加卡族贷住房资料
	public final static String KazudaiAddHousing = BaseUrl
			+ "/KazudaiAddHousingServlet";
	// 查询卡族贷住房资料
	public final static String KazudaiSelectHouse = BaseUrl
			+ "/KazudaiSelectHouseServlet";
	// 删除卡族贷住房资料
	public final static String KazudaiDelHouse = BaseUrl
			+ "/KazudaiDelHouseServlet";
	// 添加卡族贷汽车资料
	public final static String KazudaiAddCar = BaseUrl
			+ "/KazudaiAddCarServlet";
	// 查询卡族贷汽车资料
	public final static String KazudaiSelectCar = BaseUrl
			+ "/KazudaiSelectCarServlet";
	// 查询所得佣金
	public final static String ObtainBroerage = BaseUrl
			+ "/ObtainBroerageServlet";
	// 查询子级用户信息
	public final static String ObtainChildMember = BaseUrl
			+ "/ObtainChildMemberServlet";
	// 查询银行名称
	public final static String QueryBankName = BaseUrl
			+ "/QueryBankNameServlet";
	// 添加佣金提现信息
	public final static String BroerageDrawingsAdd = BaseUrl
			+ "/BroerageDrawingsAddServlet";
	// 查询佣金提现信息
	public final static String BroerageDrawingsSelect = BaseUrl
			+ "/BroerageDrawingsSelectServlet";
	// 得到推荐人id
	public final static String GetReferrerMessage = BaseUrl
			+ "/GetReferrerMessageServlet";
	// 查询排行榜
	public final static String Ranklist = BaseUrl
			+ "/RanklistServlet";
	// 查询银行提额信息
	public final static String QueryBankDrawing = BaseUrl
			+ "/QueryBankDrawingServlet";
	// 添加微社区信息
	public final static String AddWSQ = BaseUrl
			+ "/AddWSQServlet";
	// 查询微社区信息
	public final static String QueryWSQ = BaseUrl
			+ "/QueryWSQServlet";
*/
	
	// 自己要维护sessionid
	private static String sessionId;

	public static String httpGet(String url) throws Exception {

		HttpGet httpGet = new HttpGet(url);
		// 建立客户端
		DefaultHttpClient client = HttpClientSingle.getHttpClient();
		if (sessionId != null) {
			httpGet.setHeader("Cookie", "JSESSIONID=" + sessionId);
		}
		HttpResponse response = client.execute(httpGet);
		// 判断返回是否正确
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 服务器返回的数据
			String result = EntityUtils.toString(response.getEntity(), "UTF-8")
					.trim();
			// 获得session服务器的id
			CookieStore cookieStore = client.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();
			for (int i = 0; i < cookies.size(); i++) {
				Cookie cookie = cookies.get(i);
				if (cookie.getName().equals("JSESSIONID")) {
					sessionId = cookie.getValue();
				}
			}
			return result;
		}
		return null;
	}

	public static String httpPost(String url, List<NameValuePair> nameValuePairs)
			throws Exception {
		HttpPost post = new HttpPost(url);
		HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
		post.setEntity(entity);

		// 建立客户端
		DefaultHttpClient client = HttpClientSingle.getHttpClient();
		if (sessionId != null) {
			post.setHeader("Cookie", "JSESSIONID=" + sessionId);
		}
		HttpResponse response = client.execute(post);
		// 判断返回是否正确
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 服务器返回的数据
			String result = EntityUtils.toString(response.getEntity(), "UTF-8")
					.trim();

			// 获得session服务器的id
			CookieStore cookieStore = client.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();
			for (int i = 0; i < cookies.size(); i++) {
				Cookie cookie = cookies.get(i);
				if (cookie.getName().equals("JSESSIONID")) {
					sessionId = cookie.getValue();
				}
			}
			return result;
		}
		return null;
	}

	public static Bitmap down(String urladdress) {
		try {
			URL url = new URL(urladdress);
			URLConnection connection = url.openConnection();
			BufferedInputStream bis = new BufferedInputStream(
					connection.getInputStream());
			Bitmap bitmap = BitmapFactory.decodeStream(bis);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
