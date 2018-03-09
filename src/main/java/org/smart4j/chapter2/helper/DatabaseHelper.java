package org.smart4j.chapter2.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.util.PropsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/*
* 数据库操作工具类
* */
public final class DatabaseHelper {
    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    private static final QueryRunner QUERY_RUNNER;

    private static final BasicDataSource DATA_SOURCE;

    static {
        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();
        Properties conf = PropsUtil.loadProps("config.properties");
        String driver = conf.getProperty("jdbc.driver");
        String url = conf.getProperty("jdbc.url");
        String username = conf.getProperty("jdbc.username");
        String password = conf.getProperty("jdbc.password");

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);
    }

    /*
    * 获取数据库连接
    * */
    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
                e.printStackTrace();
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /*
    * 查询实体表
    * */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
            return entityList;
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
            throw new RuntimeException(e);
        }
    }

    /*
    * 查询实体
    * */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity = null;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);
            e.printStackTrace();
        }
        return entity;
    }

    /*
    * 执行查询语句
    * */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> result = null;
        try {
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
    * 执行更新语句（包括update、delete、insert）
    * */
    public static int executeUpdate(String sql, Object... params) {
        int rows = 0;
        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOGGER.error("update sql failure", e);
            e.printStackTrace();
        }
        return rows;
    }

    /*
    * 插入实体
    * */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (fieldMap == null) {
            LOGGER.error("can not insert entity : fieldMap is null");
            return false;
        }
        String sql = "insert into " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(","), columns.length(), ")");
        values.replace(values.lastIndexOf(","), values.length(), ")");
        sql += columns + (" values ") + values;
        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql, params) == 1;
    }

    /*
    * 更新实体
    * */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (fieldMap == null) {
            LOGGER.error("can not update entiry : fieldMap is null");
            return false;
        }
        String sql = "update " + getTableName(entityClass) + " set ";
        StringBuilder columns = new StringBuilder();
        for (String filedName : fieldMap.keySet()) {
            columns.append(filedName).append(" = ?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(",")) + " where id = ? ";
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.addAll(fieldMap.values());
        paramsList.add(id);
        Object[] params = paramsList.toArray();
        return executeUpdate(sql, params) == 1;
    }

    /*
    * 删除实体
    * */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
        String sql = " delete from " + getTableName(entityClass) + " where id = ?";
        return executeUpdate(sql, id) == 1;
    }

    /*
    * 执行sql文件
    * */
    public static void executeSqlFile(String filePath) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String sql;
        try {
            while ((sql = reader.readLine()) != null) {
                DatabaseHelper.executeUpdate(sql);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }
}
