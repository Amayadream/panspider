package com.amayadream.panspider.crawler.proxy;

import com.amayadream.panspider.common.util.Constants;
import com.amayadream.panspider.common.util.Requests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 代理ip抓取类, 负责抓取代理ip
 * @author :  Amayadream
 * @date :  2017.05.04 22:36
 */
public class ProxyCrawler implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ProxyCrawler.class);

    private ExecutorService validatorService;
    private Jedis jedis;

    public ProxyCrawler(Jedis jedis, ExecutorService validatorService) {
        this.jedis = jedis;
        this.validatorService = validatorService;
    }

    @Override
    public void run() {
        while (true) {
            //1.西刺
            logger.info("开始进行XiCi代理获取");
            String res = Requests.getRequest(Constants.PROXY_URL_XICI);
            if (res != null) {
                String[] proxyIps = res.split("\r\n");
                if (proxyIps.length > 0) {
                    for (String proxyIp : proxyIps) {
                        String[] ipAndHost = proxyIp.split(":");
                        ProxyValidator validator = new ProxyValidator(jedis, ipAndHost[0], Integer.valueOf(ipAndHost[1]));
                        validatorService.execute(validator);
                        logger.info("正在验证{}:{}", ipAndHost[0], ipAndHost[1]);
                    }
                }
            }
            //2.66ip
            logger.info("开始进行66ip代理获取");
            res = Requests.getRequest(Constants.PROXY_URL_66IP);
            if (res != null) {
                Pattern p = Pattern.compile("((\\d+\\.){3}\\d+):(\\d+)");
                Matcher m = p.matcher(res);
                while(m.find()) {
                    ProxyValidator validator = new ProxyValidator(jedis, m.group(1), Integer.valueOf(m.group(3)));
                    validatorService.execute(validator);
                    logger.info("正在验证{}:{}", m.group(1), Integer.valueOf(m.group(3)));
                }
            }
            //3.休眠五分钟
            try {
                logger.info("获取代理结束, 线程暂时休眠");
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
