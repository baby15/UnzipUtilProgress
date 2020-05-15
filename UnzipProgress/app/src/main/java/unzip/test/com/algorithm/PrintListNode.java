package unzip.test.com.algorithm;

import java.util.ArrayList;

public class PrintListNode {
    public static void main(String[] args) {
        ListNode l1 = new ListNode(-1);
        for (int i = 0;i < 10;i++) {
            l1.add(i);
        }



        ArrayList<Integer> integers = printListFromTailToHead1(l1);
        for (Integer in : integers) {
            System.out.println(String.valueOf(in));
        }


    }


    static ArrayList<Integer> list = new ArrayList();

    /**
     * 递归实现 从尾到头打印链表 -
     * @param listNode
     * @return
     */
    public static ArrayList<Integer> printListFromTailToHead1(ListNode listNode) {
        if(listNode!=null){
            printListFromTailToHead1(listNode.next);
            list.add(listNode.val);
        }
        return list;
    }

    public static ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
        ArrayList<Integer> list = new ArrayList<>();
        ListNode tmp = listNode;
        while(tmp!=null){
            list.add(0,tmp.val);
            tmp = tmp.next;
        }
        return list;
    }
}
