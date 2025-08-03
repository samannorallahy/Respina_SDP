package com.nor.sdpplugin.dataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteDao {
    private Connection connection;
    //    private String url = "jdbc:sqlite:db/db.callSDP";
    private String url = "jdbc:sqlite:sdpPlugin.db";

//    public static void main(String[] args) throws Exception {
//        SQLiteDao sqliteDao = new SQLiteDao();
//        ArrayList<HashMap<String, String>> hashMaps = sqliteDao.exequteQuery("select  * from requests");
//        System.out.println(hashMaps);
//    }

    public SQLiteDao() {
        // url = String.format(url, Paths.get("").toAbsolutePath().normalize().toString());// + File.separator + "library.db");
    }

    private Connection getConnection(String connectionUrl) throws SQLException, Exception {
        try {
            Connection connection = DriverManager.getConnection(connectionUrl);
            return connection;
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    public ArrayList<HashMap<String, String>> selectQuery(String query) throws Exception {
        try {
            ArrayList<HashMap<String, String>> list = new ArrayList();
            if (connection == null)
                connection = getConnection(url);
            if (connection == null)
                throw new SQLException("connection is null");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int columnsCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                HashMap<String, String> tmpStr = new HashMap();
                for (int i = 1; i <= columnsCount; i++) {
                    tmpStr.put(resultSet.getMetaData().getColumnName(i).toUpperCase(), resultSet.getString(i));
                }
                list.add(tmpStr);
            }
            return list;
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    public String executeQuery(String query) {
        try {
            if (connection == null)
                connection = getConnection(url);
            if (connection == null)
                throw new SQLException("connection is null");
            Statement statement = connection.createStatement();
            boolean execute = statement.execute(query);
            return String.valueOf(execute);
        } catch (Exception e) {
            return e.toString();
        }
    }

    public void insertLog(String reqID_SDP, String reqID_JIRA, String inoutJSON, String outputJSON) {
        try {
            if (connection == null)
                connection = getConnection(url);
            if (connection == null)
                throw new SQLException("connection is null");

            String sql = "insert into main.request(reqID_SDP,reqID_JIRA,inoutJSON,outputJSON)VALUES(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reqID_SDP);
            statement.setString(2, reqID_JIRA);
            statement.setString(3, inoutJSON);
            statement.setString(4, outputJSON);
            statement.execute();
        } catch (SQLException e) {
            System.out.println(e);
//            System.exit(1);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void insertLogSdpToJira(String reqID_SDP, String reqID_JIRA, String inoutJSON, String outputJSON) {
        try {
            if (connection == null)
                connection = getConnection(url);
            if (connection == null)
                throw new SQLException("connection is null");

            String sql = "insert into main.request(reqID_SDP,reqID_JIRA,inoutJSON,outputJSON)VALUES(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reqID_SDP);
            statement.setString(2, reqID_JIRA);
            statement.setString(3, inoutJSON);
            statement.setString(4, outputJSON);
            statement.execute();
        } catch (SQLException e) {
            System.out.println(e);
//            System.exit(1);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateSdpToJira(String reqID_SDP, String reqID_JIRA) {
        try {
            if (connection == null)
                connection = getConnection(url);
            if (connection == null)
                throw new SQLException("connection is null");
            String sql = "update request set jiraUpdateCount = jiraUpdateCount + 1 where reqID_JIRA = ? and reqID_SDP = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reqID_JIRA);
            statement.setString(2, reqID_SDP);
            statement.execute();
        } catch (SQLException e) {
            System.out.println(e);
//            System.exit(1);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateJiraToSDP(String reqID_SDP, String reqID_JIRA) {
        try {
            if (connection == null)
                connection = getConnection(url);
            if (connection == null)
                throw new SQLException("connection is null");
            String sql = "update request set sdpUpdateCount = sdpUpdateCount + 1 where reqID_JIRA = ? and reqID_SDP = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reqID_JIRA);
            statement.setString(2, reqID_SDP);
            statement.execute();
        } catch (SQLException e) {
            System.out.println(e);
//            System.exit(1);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
