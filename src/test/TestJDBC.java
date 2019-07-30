import com.waston.utils.DataSourceUtils;
import com.waston.utils.JDBCUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.sql.*;

/**
 * @Description:
 * @Author: Waston
 * @Date: 2019/7/27 11:01
 */
public class TestJDBC {
    @Test
    public void test(){
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try {
            //建立连接
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            //SQL注入
            String sql = "select * from jdbc.user";


            //Result封装查询后的结果
            result = statement.executeQuery(sql);
            while (result.next()){
                System.out.print("ID "+result.getObject("ID")+", ");
                System.out.print("USERNAME "+result.getObject("USERNAME")+", ");
                System.out.print("PASSWORD "+result.getObject("PASSWORD")+", ");
                System.out.println();
            }

        }  catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtils.closeResources(connection,statement,result);
        }
    }

    @Test
    public void testQuery(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            //建立连接
            connection = DataSourceUtils.getConnection();
            //SQL注入
            String sql = "SELECT * FROM user WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1,1);
            result = statement.executeQuery();
            while (result.next()){
                int id = result.getInt("id");
                String userName = result.getString("username");
                String password = result.getString("password");
                System.out.println("id为"+id+",用户名为:"+userName+ "，密码为"+password);
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtils.closeResources(connection,statement,result);
        }
    }

    @Test
    public void testInsert(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try{
            connection  = JDBCUtils.getConnection();
            String sql = "INSERT INTO user(username,password) VALUES(?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1,"kk");
            statement.setString(2,DigestUtils.md5Hex("kk"));
            int effect = statement.executeUpdate();
            Assert.assertEquals(1,effect);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
