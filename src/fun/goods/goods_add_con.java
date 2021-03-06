package fun.goods;

import base.jdbc.Config;
import base.jdbc.JDBCBuild;
import bean.GoodsBean;
import fun.ConnectResponse;

import java.sql.ResultSet;

public class goods_add_con {
    private ConnectResponse response;
    public ConnectResponse DoTask(GoodsBean bean){
        //mysql数据库连接
        JDBCBuild jdbcBuild = new JDBCBuild();

        if(!jdbcBuild.buildConnect(Config.DRIVER,Config.DBUrl+"?serverTimezone=GMT", Config.name,Config.password)){
            //如果链接不成功
            return getConnectResponse(jdbcBuild,500,"连接数据库失败！");
        }else{
            try{
                ResultSet result =jdbcBuild.executeQuery(String.format("select Tid from type where Tname='%s'",bean.getTname()));
                result.next();
                String Tid=result.getString("Tid");
                if(jdbcBuild.executeUpdate(
                  String.format("insert into goodsinfo values ('%s','%s','%s','%s')",bean.getGid(),bean.getGname(),Tid,bean.getGproducer())
                )==-1){
                    return getConnectResponse(jdbcBuild,500,"插入数据时出错！");
                }else {
                    return getConnectResponse(jdbcBuild,200,"插入成功！");
                }
            }catch (Exception e){
                return getConnectResponse(jdbcBuild,500,"发生未知错误！");
            }
        }
    }


    private ConnectResponse getConnectResponse(JDBCBuild jdbcBuild, int code, String message) {
        response=new ConnectResponse();
        jdbcBuild.close();
        response.setStatus(code);
        response.setMessage(message);
        return response;
    }
}
