
import java.util.HashMap;
import org.json.JSONArray;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author river.liao
 */
public class TestClass {

    public static void main(String args[]){
        String userHost = "'river'@'host'";
        String userName = userHost.substring(1, userHost.indexOf("@")-1);
        System.out.println(userName);

        String host = userHost.substring(userHost.indexOf("@")+2, userHost.length()-1);
        System.out.println(host);


        HashMap<String, JSONArray> schema=new HashMap<String, JSONArray>();
        schema.put("test",new JSONArray());

        JSONArray record = schema.get("test");
        System.out.println("test4");
        record.put("River");

        System.out.println("test5"+schema.get("test").toString());

        String db = "river\\_liao";
        System.out.println(db);
        System.out.println(db.replace('\\', ' ').replaceAll(" ", ""));
    }

}
