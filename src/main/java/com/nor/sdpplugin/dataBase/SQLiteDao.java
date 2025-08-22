package com.nor.sdpplugin.dataBase;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
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
            Class.forName("org.sqlite.JDBC");
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

    public ArrayList<HashMap<String, String>> findMobileNumber(String mobileNo) throws Exception {
        try {
            ArrayList<HashMap<String, String>> list = new ArrayList();
            if (connection == null)
                connection = getConnection(url);
            if (connection == null)
                throw new SQLException("connection is null");

//            Statement statement = connection.createStatement();
            String query = "select * from requestFromSDP where mobileNo = ? and customerReaction is null";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, mobileNo);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
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

    public ArrayList<HashMap<String, String>> findRequestId(int requestId) throws Exception {
        try {
            ArrayList<HashMap<String, String>> list = new ArrayList();
            if (connection == null)
                connection = getConnection(url);
            if (connection == null)
                throw new SQLException("connection is null");

//            Statement statement = connection.createStatement();
            String query = "select * from requestFromSDP where reqID_SDP = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, requestId);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            int columnsCount = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                HashMap<String, String> tmpStr = new HashMap();
                for (int i = 1; i <= columnsCount; i++)
                    tmpStr.put(resultSet.getMetaData().getColumnName(i).toUpperCase(), resultSet.getString(i));
                list.add(tmpStr);
            }
            return list;
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    public boolean templateExistInDB(String str) throws Exception {
        try {
            ArrayList<HashMap<String, String>> list = new ArrayList();
            if (connection == null)
                connection = getConnection(url);
            if (connection == null)
                throw new SQLException("connection is null");

//            Statement statement = connection.createStatement();
            String query = "select name from templates";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                return resultSet.getString(1).equals(str);
            }
            return false;
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    public void updateCalledFromTelsi(int id, int reaction) throws Exception {
        try {
            ArrayList<HashMap<String, String>> list = new ArrayList();
            if (connection == null)
                connection = getConnection(url);
            if (connection == null)
                throw new SQLException("connection is null");

//            Statement statement = connection.createStatement();
            String query = "update requestFromSDP set customerReaction = ? where id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, reaction);
            statement.setInt(2, id);
            statement.execute();
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    public void update_callCustomer(int id) throws Exception {
        try {
            ArrayList<HashMap<String, String>> list = new ArrayList();
            if (connection == null)
                connection = getConnection(url);
            if (connection == null)
                throw new SQLException("connection is null");

//            Statement statement = connection.createStatement();
            String query = "update requestFromSDP set callCustomer = ? where id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, 1);
            statement.setInt(2, id);
            statement.execute();
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    public void updateTemplateChanged(int reqID_SDP, String requesterMobile, int templateChanged) throws Exception {
        try {
            ArrayList<HashMap<String, String>> list = new ArrayList();
            if (connection == null)
                connection = getConnection(url);
            if (connection == null)
                throw new SQLException("connection is null");
            log.info("update requestFromSDP set templateChanged = " + templateChanged + " , mobileNo = " + requesterMobile + " where reqID_SDP = " + reqID_SDP);
            String query = "update requestFromSDP set templateChanged = ? , mobileNo = ? where reqID_SDP = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, templateChanged);
            statement.setString(2, requesterMobile);
            statement.setInt(3, reqID_SDP);
            statement.execute();
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
            log.error(e.toString());
            return e.toString();
        }
    }

    public void insertIntoRequestsFromSDP(int reqID, String MobileNo, String json, int templateChanged) throws Exception {
//        try {
        if (connection == null)
            connection = getConnection(url);
        if (connection == null)
            throw new SQLException("connection is null");
//            Statement statement = connection.createStatement();
        log.info("insert into requestFromSDP(reqID_SDP,mobileNo, inoutJSON,templateChanged)values(" + reqID + "," + MobileNo + ", ? ," + templateChanged + ")");
        PreparedStatement statement = connection.prepareStatement("insert into requestFromSDP(reqID_SDP,mobileNo, inoutJSON,templateChanged)values(?,?,?,?)");
        statement.setInt(1, reqID);
        statement.setString(2, MobileNo);
        statement.setString(3, json);
        statement.setInt(4, templateChanged);
        statement.execute();
//        } catch (Exception e) {
//            log.error(e.toString());
//        }
    }

    public void insertIntoRequestsFromTelsi(String phone, int reaction) throws Exception {
        if (connection == null)
            connection = getConnection(url);
        if (connection == null)
            throw new SQLException("connection is null");
//            Statement statement = connection.createStatement();
        PreparedStatement statement = connection.prepareStatement("insert into requestFromTelsi(phone, reaction)values(?,?)");
        statement.setString(1, phone);
        statement.setInt(2, reaction);
        statement.execute();
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
