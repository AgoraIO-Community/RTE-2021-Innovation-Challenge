package my.socket.mysql;

import java.sql.*;

public class DBManager {

    /**
     * 该方法用户连接数据库
     *
     * @return 返回Connection的一个实例
     */
    private Connection getConnection() {

        Connection con = null;

        try {

            Class.forName(StaticVar.DRIVER_NAME);
            con = DriverManager.getConnection(StaticVar.DB_URL,StaticVar.USER_NAME, StaticVar.DB_PASSWD);

        }
         catch (SQLException | ClassNotFoundException e) {
            return null;
        }

        return con;
    }

    /**
     * 用于查询sql语句
     *
     * @param sql
     *            sql语句
     * @return 返回ResultSet集合
     */
    public  ResultSet select(String sql) {
        ResultSet res = null;

        Connection con = getConnection();

        Statement state = null;

        if (!(con == null)) {

            try {
                state = con.createStatement();
                res = state.executeQuery(sql);
            } catch (SQLException e) {
                return null;
            }

        }



        return res;
    }

    /**
     * 向表中插入一个元素，返回插入后的元素的id
     *
     * @param sql
     * @return
     */
    public int insert(String sql) {
        int iId = -1;

        Connection con = getConnection();

        Statement state = null;

        if (con != null) {
            try {
                state = con.createStatement();

                iId = state.executeUpdate(sql,
                        Statement.RETURN_GENERATED_KEYS);

//                if (res != 0) {
//                    ResultSet rs = state.getGeneratedKeys();
//                    if (rs.next()) {
//                        iId = rs.getInt(1);
//                    }
//                }
            } catch (SQLException e) {
                iId = -1;
            }

        }

        if (state != null) {
            try {
                state.close();
            } catch (SQLException e) {
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
            }
        }

        return iId;
    }

    /**
     * 修改表中的某个元素的数值
     *
     * @param sql
     *            sql语句
     * @return 元素是否被成功修改
     */
    public boolean update(String sql) {
        boolean updated = false;

        Connection con = getConnection();
        Statement state = null;

        if (con != null) {
            try {
                state = con.createStatement();

                int res = state.executeUpdate(sql);

                if (res == 0) {
                    updated = false;
                } else {
                    updated = true;
                }
            } catch (SQLException e) {
                updated = false;
            }

        }

        if (state != null) {
            try {
                state.close();
            } catch (SQLException e) {
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
            }
        }

        return updated;
    }

    /**
     * 删除表中的某一个表项
     *
     * @param sql
     *            sql语句
     * @return 返回是否删除成功
     */
    public boolean delete(String sql) {
        boolean deleted = false;

        Connection con = getConnection();

        Statement state = null;

        if (con != null) {
            try {
                state = con.createStatement();

                int res = state.executeUpdate(sql);

                if (res == 0) {
                    deleted = false;
                } else {
                    deleted = true;
                }

            } catch (SQLException e) {
                deleted = false;
            }

        }

        if (state != null) {
            try {
                state.close();
            } catch (SQLException e) {
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
            }
        }

        return deleted;
    }

}
