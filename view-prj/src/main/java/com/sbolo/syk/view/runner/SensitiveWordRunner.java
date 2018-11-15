package com.sbolo.syk.view.runner;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sbolo.syk.common.tools.FileUtils;
import com.sbolo.syk.common.tools.SensitiveWordUtils;

@Component
@Order(value = 1)   //执行顺序控制
public class SensitiveWordRunner implements ApplicationRunner {
	
	private static final Logger log = LoggerFactory.getLogger(SensitiveWordRunner.class);

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("-------------开始整理违禁词库-------------");
		log.info("==装载[暴恐词库.txt]==");
		InputStream txtStream1 = this.getClass().getResourceAsStream("/sensitive/暴恐词库.txt");
		Set<String> sw1 = FileUtils.txt2Set(txtStream1);
		if(sw1 != null && sw1.size() > 0) {
			SensitiveWordUtils.add(sw1);
		}
		log.info("==装载[反动词库.txt]==");
		InputStream txtStream2 = this.getClass().getResourceAsStream("/sensitive/反动词库.txt");
		Set<String> sw2 = FileUtils.txt2Set(txtStream2);
		if(sw2 != null && sw2.size() > 0) {
			SensitiveWordUtils.add(sw2);
		}
		log.info("==装载[民生词库.txt]==");
		InputStream txtStream3 = this.getClass().getResourceAsStream("/sensitive/民生词库.txt");
		Set<String> sw3 = FileUtils.txt2Set(txtStream3);
		if(sw3 != null && sw3.size() > 0) {
			SensitiveWordUtils.add(sw3);
		}
		log.info("==装载[色情词库.txt]==");
		InputStream txtStream4 = this.getClass().getResourceAsStream("/sensitive/色情词库.txt");
		Set<String> sw4 = FileUtils.txt2Set(txtStream4);
		if(sw4 != null && sw4.size() > 0) {
			SensitiveWordUtils.add(sw4);
		}
		log.info("==装载[政治词库.txt]==");
		InputStream txtStream5 = this.getClass().getResourceAsStream("/sensitive/政治词库.txt");
		Set<String> sw5 = FileUtils.txt2Set(txtStream5);
		if(sw5 != null && sw5.size() > 0) {
			SensitiveWordUtils.add(sw5);
		}
		log.info("==装载[其他词库.txt]==");
		InputStream txtStream6 = this.getClass().getResourceAsStream("/sensitive/其他词库.txt");
		Set<String> sw6 = FileUtils.txt2Set(txtStream6);
		if(sw6 != null && sw6.size() > 0) {
			SensitiveWordUtils.add(sw6);
		}
		log.info("-------------违禁词库整理结束-------------");
	}

}
