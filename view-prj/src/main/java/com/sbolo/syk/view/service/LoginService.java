package com.sbolo.syk.view.service;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RoleEnum;
import com.sbolo.syk.common.constants.UserSettleEnum;
import com.sbolo.syk.common.constants.UserStEnum;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.view.entity.SykOpenUserEntity;
import com.sbolo.syk.view.entity.SykUserEntity;
import com.sbolo.syk.view.enums.OpenTypeEnum;
import com.sbolo.syk.view.exception.OpenUserException;
import com.sbolo.syk.view.mapper.SykOpenUserMapper;
import com.sbolo.syk.view.mapper.SykUserMapper;
import com.sbolo.syk.view.vo.QQAccessTokenInfoVO;
import com.sbolo.syk.view.vo.QQOpenInfoVO;
import com.sbolo.syk.view.vo.QQUserInfoVO;
import com.sbolo.syk.view.vo.SykUserVO;

import okhttp3.Response;

@Service
public class LoginService {
	
	@Autowired
	private SykOpenUserMapper sykOpenUserMapper;
	
	@Autowired
	private SykUserMapper sykUserMapper;
	
	@Transactional
	public SykUserVO qqLogin(String code) throws Exception {
		String appid = ConfigUtil.getPropertyValue("open.qq.appid");
		String appkey = ConfigUtil.getPropertyValue("open.qq.appkey");
		String callbackUrl = ConfigUtil.getPropertyValue("open.qq.callback");
		
		QQAccessTokenInfoVO accessTokenInfo = this.getAccessToken(appid, appkey, code, callbackUrl);
		String accessToken = accessTokenInfo.getAccess_token();
		
		QQOpenInfoVO openInfo = this.getOpenInfo(accessToken);
		String openid = openInfo.getOpenid();
		
		String userPrn = sykOpenUserMapper.selectUserPrn(openid);
		
		SykUserEntity user = null;
		if(StringUtils.isNotBlank(userPrn)) {
			user = sykUserMapper.selectUser(userPrn);
			if(user == null) {
				throw new OpenUserException("系统错误！");
			}
		}else {
			user = this.addQQUserAndGetSykUser(accessTokenInfo, appid, openid);
		}
		
		SykUserVO userVO = VOUtils.po2vo(user, SykUserVO.class);
		return userVO;
	}
	
	private SykUserEntity addQQUserAndGetSykUser(QQAccessTokenInfoVO accessTokenInfo, String appid, String openid) throws Exception {
		String access_token = accessTokenInfo.getAccess_token();
		QQUserInfoVO qqUserInfo = this.getOpenUser(access_token, appid, openid);
		
		String avatarUrl = qqUserInfo.getFigureurl_qq_2();
		if(StringUtils.isBlank(avatarUrl)) {
			avatarUrl = qqUserInfo.getFigureurl_2();
		}
		String nickname = qqUserInfo.getNickname();
		SykUserEntity sykUser = this.addAndGetSykUser(avatarUrl, nickname);
		
		SykOpenUserEntity qqUser = new SykOpenUserEntity();
		qqUser.setAccessToken(access_token);
		qqUser.setAvatarUrl(avatarUrl);
		qqUser.setCreateTime(sykUser.getCreateTime());
		qqUser.setExpiredTime(Long.valueOf(accessTokenInfo.getExpires_in()));
		qqUser.setNickname(qqUserInfo.getNickname());
		qqUser.setOpenId(openid);
		qqUser.setOpenType(OpenTypeEnum.QQ.getCode());
		qqUser.setPrn(StringUtil.getId(null));
		qqUser.setUserPrn(sykUser.getPrn());
		
		sykOpenUserMapper.insertSelective(qqUser);
		return sykUser;
	}
	
	private SykUserEntity addAndGetSykUser(String avatarUrl, String nickname) {
		String defaultPassword = ConfigUtil.getPropertyValue("user.defaultPassword");
		String prn = StringUtil.getId(CommonConstants.user_s);
		SykUserEntity user = new SykUserEntity();
		user.setAvatarUri(avatarUrl);
		user.setCreateTime(new Date());
		user.setNickname(nickname);
		user.setPassword(defaultPassword);
		user.setIsSettle(UserSettleEnum.NOT.getCode());
		user.setPrn(prn);
		user.setSt(UserStEnum.QY.getCode());
		user.setUsername(prn);
		user.setUserPower(RoleEnum.USER1.getDec());
		sykUserMapper.insertSelective(user);
		return user;
	}
	
	private QQAccessTokenInfoVO getAccessToken(String appid, String appkey, String code, String callbackUrl) throws Exception{
		String backUrl = URLEncoder.encode(callbackUrl,"utf-8");
		String url="https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id="+appid+"&client_secret="+appkey+"&code="+code+"&redirect_uri="+backUrl;
		HttpResult<String> result = HttpUtils.httpGet(url, new HttpSendCallback<String>() {

			@Override
			public String onResponse(Response response) throws Exception {
				String string = response.body().string();
				return string;
			}
		});
		
		String values = result.getValue();
		String jsonStr = StringUtil.params2Json(values);
		return JSON.parseObject(jsonStr, QQAccessTokenInfoVO.class);
	}
	
	private QQOpenInfoVO getOpenInfo(String accessToken) throws Exception{
		String url = "https://graph.qq.com/oauth2.0/me?access_token="+accessToken;
		HttpResult<String> result = HttpUtils.httpGet(url, new HttpSendCallback<String>() {

			@Override
			public String onResponse(Response response) throws Exception {
				String string = response.body().string();
				return string;
			}
		});
		String values = result.getValue();
		int start = values.indexOf("{");
		int end = values.lastIndexOf("}")+1;
		values = values.substring(start, end);
		return JSON.parseObject(values, QQOpenInfoVO.class);
	}
	
	private QQUserInfoVO getOpenUser(String accessToken, String appId, String openId) throws Exception{
		String url = "https://graph.qq.com/user/get_user_info?oauth_consumer_key="+appId+"&access_token="+accessToken+"&openid="+openId+"&format=json";
		HttpResult<String> result = HttpUtils.httpGet(url, new HttpSendCallback<String>() {

			@Override
			public String onResponse(Response response) throws Exception {
				String string = response.body().string();
				return string;
			}
		});
		String value = result.getValue();
		QQUserInfoVO qqUserInfo = JSON.parseObject(value, QQUserInfoVO.class);
		
		if(qqUserInfo.getRet() != 0) {
			throw new OpenUserException(qqUserInfo.getMsg());
		}
		
		return qqUserInfo;
	}
}
