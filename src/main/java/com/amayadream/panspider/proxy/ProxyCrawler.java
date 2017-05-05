package com.amayadream.panspider.proxy;

import com.amayadream.panspider.common.util.Constants;
import com.amayadream.panspider.common.util.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ExecutorService;

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
            String res = HttpClientUtils.getRequest(Constants.PROXY_URL_XICI);
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
            try {
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
