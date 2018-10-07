package org.b3log.xiaov.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.commons.lang.StringUtils;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;

public class Memo {
    private static final Logger LOGGER = Logger.getLogger(Memo.class);

    public static String searchURL(String keyword) {
        String ret = XiaoVs.getString("bot.follow.keywordAnswer");
        try {
            ret = StringUtils.replace(ret, "{keyword}",
                                      URLEncoder.encode(keyword, "UTF-8"));
        } catch (final UnsupportedEncodingException e) {
            LOGGER.log(Level.ERROR, "Search key encoding failed", e);
        }

        return ret;
    }

    private static String kaiHei(String content, String userName) {
        // TODO 开黑
        // 1. 获取联系人姓名
        // 2. 保存数据库, 筛选出一定时间内的消息
        // 3. 格式化输出
        return "开黑功能开发中~";
    }

    public static String write(String keyword, String content, String userName) {
        switch (keyword) {
            case "开黑":
                return kaiHei(content, userName);
            default:
                String[] contents = content.split(keyword, 2);
                return  searchURL(contents[contents.length - 1]);
        }
    }
}
