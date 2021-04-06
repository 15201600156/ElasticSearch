
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HashMapTest {

    @Test
    public  void  test()
    {
        Map<String,String> map=new HashMap<>();
        map.put("张三","张三");
        map.put("里斯","里斯");


        System.out.println(map.get("张三"));
        System.out.println(map.get("里斯"));
    }
}
