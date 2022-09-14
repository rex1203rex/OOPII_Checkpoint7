import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class A1083337_checkpoint7_QueryDB {
    // //Description : the driver description of mysql
    // private static final String driver = "com.mysql.cj.jdbc.Driver";
    // //Description : the protocol description of mysql
    // private static final String protocol = "jdbc:mysql://140.127.220.220:3306/";
    // Description : the obstacle set queried from database.
    private static ArrayList<Integer[]> data = new ArrayList<Integer[]>();
    // Description : the filename of obstacle image queried from database.
    private static HashMap<Integer, String> typeChar = new HashMap<Integer, String>();
    // Description : the primary key of map in database.
    private static String mapID = "0";

    public static void queryData(ArrayList<Integer[]> data, HashMap<Integer, String> typeChar) {
        // TODO(Past): Querying the barrier location from the server, and set it into
        // Arraylist data.
        // TODO(Past): Querying the bar_type and the corresponding file_name from the
        // server, and set it into HashMap.
        /********************************************************************************************
         * START OF YOUR CODE
         ********************************************************************************************/
        String dbName = "//140.127.220.226:5432/oopiickp";
        String protocol = "jdbc:postgresql:";   
        String driver = "org.postgresql.Driver";
        String obsQuery = "SELECT x_coordinate, y_coordinate, obstacle_type FROM obstacle_info WHERE map_id="+mapID+";"; 
        String typeQuery = "SELECT sty.obstacle_type, sty.filename FROM obstacle_style sty, obstacle_info info WHERE info.map_id="+mapID+" and info.obstacle_type=sty.obstacle_type;";
        Integer[] obs = new Integer[3];
        ArrayList<Integer[]> obsTemp = new ArrayList<Integer[]>();

        try{
            Class.forName(driver).newInstance();
        }catch(Exception err){
            System.err.println("Unable to load the embedded driver.");
            System.exit(0);
        }

        Connection conn = null;

        try{
            conn = DriverManager.getConnection(protocol + dbName ,"fallckp","2021OOPIIpwd");
            Statement s = conn.createStatement();
            /*******************************
             * Query the obstacle x,y,type
             *******************************/
            ResultSet rs = s.executeQuery(obsQuery);
            while (rs.next()){
                if(rs.getString("obstacle_type")!=null && rs.getString("y_coordinate")!=null && rs.getString("x_coordinate")!=null){
                    obs = new Integer[3];
                    obs[1] = rs.getInt("x_coordinate");
                    obs[0] = rs.getInt("y_coordinate");
                    obs[2] = rs.getInt("obstacle_type");
                    data.add(obs);
                }
            }

            
           
            /*******************************
             * Query the obstacle_type and display
             *******************************/
            rs = s.executeQuery(typeQuery);
            while (rs.next()){
                if(rs.getString("obstacle_type")!=null && rs.getString("filename")!=null){
                    typeChar.put(rs.getInt("obstacle_type"), rs.getString("filename"));
                }
            }
            
            
            conn.close();

        }catch(SQLException e){
            System.err.println(e.getMessage());
            System.err.println("SQL error.");

            System.exit(0);

        } 
        /********************************************************************************************
         * END OF YOUR CODE
         ********************************************************************************************/

    }

    public ArrayList getObstacle() {
        return this.data;
    }

    public void setObstacle(ArrayList<Integer[]> data) {
        this.data = data;
    }

    public String getMapID() {
        return this.mapID;
    }

    public void setMapID(String mapID) {
        this.mapID = mapID;
    }

    public HashMap getObstacleImg() {
        return this.typeChar;
    }

    public void setObstacleImg(HashMap<Integer, String> typeChar) {
        this.typeChar = typeChar;
    }
}
