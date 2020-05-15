package unzip.test.com.algorithm;

public class TwoTree {
    public static void main(String[] args) {
        int[] pre = {1,2,4,7,3,5,6,8};
        int[] mid = {4,7,2,1,5,3,8,6};
        TreeNode treeNode = reConstructBinaryTree(pre,mid);
        System.out.println("前序");
        preOrderRe(treeNode);
        System.out.println("中序");
        midOrderRe(treeNode);
        System.out.println("后序");
        postOrderRe(treeNode);

    }

    /**
     * 输入某二叉树的前序遍历和中序遍历的结果，
     * 请重建出该二叉树。假设输入的前序遍历和中序遍历的结果中都不含重复的数字。
     * 例如输入前序遍历序列{1,2,4,7,3,5,6,8}和中序遍历序列{4,7,2,1,5,3,8,6}，则重建二叉树并返回。
     * @param
     */
    public static TreeNode reConstructBinaryTree(int [] pre,int [] in) {
        TreeNode root=reConstructBinaryTree(pre,0,pre.length-1,in,0,in.length-1);
        return root;
    }
    //前序遍历{1,2,4,7,3,5,6,8}和中序遍历序列{4,7,2,1,5,3,8,6}
    private static TreeNode reConstructBinaryTree(int [] pre,int startPre,int endPre,int [] in,int startIn,int endIn) {

        if(startPre>endPre||startIn>endIn)
            return null;
        TreeNode root=new TreeNode(pre[startPre]);

        for(int i=startIn;i<=endIn;i++)
            if(in[i]==pre[startPre]){
                root.left=reConstructBinaryTree(pre,startPre+1,startPre+i-startIn,in,startIn,i-1);
                root.right=reConstructBinaryTree(pre,i-startIn+startPre+1,endPre,in,i+1,endIn);
                break;
            }

        return root;
    }


    public static void preOrderRe(TreeNode biTree)
    {//递归实现
        if (biTree == null) {
            return;
        } else {
            System.out.println(biTree.value);
            preOrderRe(biTree.left);

            preOrderRe(biTree.right);
        }
    }



public static void midOrderRe(TreeNode biTree) {
    if (biTree == null) {
        return;
    } else {
        midOrderRe(biTree.left);
        System.out.println(biTree.value);
        midOrderRe(biTree.right);
    }

}

    public static void postOrderRe(TreeNode biTree) {
        if (biTree == null) {
            return;
        } else {
            postOrderRe(biTree.left);
            postOrderRe(biTree.right);
            System.out.println(biTree.value);

        }

    }

  static   class TreeNode//节点结构
    {
        int value;
        TreeNode left;
        TreeNode right;

        TreeNode(int value)
        {
            this.value = value;
        }
    }
}
