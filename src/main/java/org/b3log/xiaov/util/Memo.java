package org.b3log.xiaov.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.xiaov.service.DataBaseService;

public class Memo {
    private static final Logger LOGGER = Logger.getLogger(Memo.class);

    private static String searchURL(String keyword) {
        String ret = XiaoVs.getString("bot.follow.keywordAnswer");
        try {
            ret = StringUtils.replace(ret, "{keyword}",
                    URLEncoder.encode(keyword, "UTF-8"));
        } catch (final UnsupportedEncodingException e) {
            LOGGER.log(Level.ERROR, "Search key encoding failed", e);
        }

        return ret;
    }

    private static String kaiHei(String userName, String content) {
        // TODO 获取联系人姓名 维护联系人列表 难度较大
        try {
            DataBaseService.insertKaiHeiInfo(userName, content);
            return "添加开黑信息成功~ \n" + formatKaiheiList(DataBaseService.findKaiHeiList());
        } catch (SQLException e) {
            e.printStackTrace();
            return "添加/查询 开黑信息异常, 请联系管理员~";
        }
    }

    private static String formatKaiheiList(Map<String, String> kaiHeiList) {
        String header = "最近一段时间内的开黑记录有: \n ------- \n";
        StringBuilder bodyBuilder = new StringBuilder();
        for (String content : kaiHeiList.values()) {
            bodyBuilder.append(content);
            bodyBuilder.append("\n");
        }
        String body = bodyBuilder.toString();

        if (StringUtils.isBlank(body)) {
            return "最近暂无开黑信息哦~\n\n" + kaiHeiHelp();
        } else {
            return header + body + " ------- ";
        }
    }

    private static String kaiHeiHelp() {
        return "开黑助手使用方法\n" +
                "--------\n" +
                "小 黑[空格]开黑 = 显示该帮助信息\n" +
                "小 黑[空格]开黑[空格]想发布的开黑信息 = 发布开黑信息\n" +
                "小 黑[空格]开黑列表 = 展示已发布的开黑列表\n" +
                "--------\n" +
                "p.s. :\n" +
                "太久的消息将被自动清除\n" +
                "一个人只能发一条，后面覆盖前面";
    }

    public static String write(String keyword, String userName, String content) {
        String[] contents = content.split(keyword, 2);
        String contentTail = contents[contents.length - 1];

        switch (keyword) {
            case "开黑":
                if (StringUtils.isBlank(contentTail)) {
                    return kaiHeiHelp();
                }
                return kaiHei(userName, contentTail);
            case "开黑列表":
                try {
                    return formatKaiheiList(DataBaseService.findKaiHeiList());
                } catch (SQLException e) {
                    e.printStackTrace();
                    return "查询开黑信息异常, 请联系管理员~";
                }
            default:
                return searchURL(contentTail);
        }
    }
}
