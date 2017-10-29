package easy.framework.database;


/**
 * 分页
 * @author limengyu
 * @create 2017/10/29
 */
public class Page{

       private int totalCount;
       private int pageSize;
       private int currentPage;
        
       public boolean hasNext(){
          return false;
       }
       
       public int nextPage(){
           return 1;
       }
       
       public int getCurrentPage(){
            return currentPage;
       }
}
