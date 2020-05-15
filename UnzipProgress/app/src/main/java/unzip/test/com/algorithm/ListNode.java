package unzip.test.com.algorithm;

public class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
    //添加节点
    public void add(int newdata){
        ListNode newNode = new ListNode(newdata);
     if(this.next==null){
            this.next = newNode;
      }else{
             this.next.add(newdata);
          }
     }
}
