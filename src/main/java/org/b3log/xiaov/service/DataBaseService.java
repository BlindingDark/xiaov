package org.b3log.xiaov.service;

import org.h2.tools.DeleteDbFiles;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DataBaseService {
    static {
        // delete the database named 'test' in the user home directory
        DeleteDbFiles.execute("~", "h2database-xiaov", true);

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static DataBaseService ourInstance = new DataBaseService();

    public static Connection getConn() {
        return ourInstance.conn;
    }

    private Connection conn;

    private DataBaseService()  {
        try {
            conn = DriverManager.getConnection("jdbc:h2:~/h2database-xiaov");
            // TODO 可配置的, 放在 sql 初始化文件中
            Statement stat = conn.createStatement();
            stat.execute("create table kai_hei(user_id VARCHAR(255) primary key, content VARCHAR(255), updated_at BIGINT)");
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO 单独的 kaihei service
    private final static long timeShift = 3600 * 1000;
    public static Map<String, String> findKaiHeiList() throws SQLException {
        long outDateTime = System.currentTimeMillis() - timeShift;
        Connection conn = DataBaseService.getConn();
        PreparedStatement pst = conn.prepareStatement("select * from kai_hei where updated_at > ?");
        pst.setLong(1, outDateTime);

        ResultSet rs = pst.executeQuery();

        Map<String, String> result = new HashMap<>();
        while (rs.next()) {
            result.put(rs.getString("user_id"), rs.getString("content"));
        }

        return result;
    }

    public static void insertKaiHeiInfo(String userId, String content) throws SQLException {
        long nowTime = System.currentTimeMillis();
        Connection conn = DataBaseService.getConn();
        PreparedStatement pst = conn.prepareStatement("merge into kai_hei values(?, ?, ?)");
        pst.setString(1, userId);
        pst.setString(2, content);
        pst.setLong(3, nowTime);
        pst.execute();
    }
}
