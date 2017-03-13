package com.ryan.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ryan.utils.ImageCode;

@Controller
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/images/imagecode")
	public void imagecode(HttpSession session, HttpServletResponse response) throws Exception {

		OutputStream os = response.getOutputStream();
		Map<String, Object> map = ImageCode.getImageCode(60, 20, os);

		String captchaNum = map.get("strEnsure").toString().toLowerCase();
		Long createTime = new Date().getTime();
		session.setAttribute("captchaNum", captchaNum);
		session.setAttribute("codeTime", createTime);
		log.info("生成验证码：{},时间: {}", captchaNum, createTime);
		try {
			ImageIO.write((BufferedImage) map.get("image"), "JPEG", os);
		} catch (IOException e) {
			log.info("生产验证码图片失败！");
		}
	}

	@RequestMapping(value = "/checkcode")
	@ResponseBody
	public String checkcode(HttpServletRequest request, HttpSession session) throws Exception {
		String checkCode = request.getParameter("checkCode");
		Object captchaNum = session.getAttribute("captchaNum");
		if (captchaNum == null) {
			return "验证码已失效，请重新输入！";
		}

		String captcha = captchaNum.toString();
		Date now = new Date();
		Long codeTime = Long.valueOf(session.getAttribute("codeTime") + "");
		if (StringUtils.isEmpty(checkCode) || captcha == null || !(checkCode.equalsIgnoreCase(captcha))) {
			return "验证码错误！";
		} else if ((now.getTime() - codeTime) / 1000 / 60 > 5) {// 验证码有效时长为5分钟
			return "验证码已失效，请重新输入！";
		} else {
			session.removeAttribute("simpleCaptcha");
			return "1";
		}
	}
}
