package unzip.test.com.algorithm;

public class TwoArray {


    public static boolean Find(int [][] array,int target) {

        for(int i=0;i<array.length;i++){
            int low=0;
            int high=array[i].length-1;
            while(low<=high){
                int mid=(low+high)/2;
                if(target>array[i][mid])
                    low=mid+1;
                else if(target<array[i][mid])
                    high=mid-1;
                else
                    return true;
            }
        }
        return false;

    }

    public static boolean Find(int target,int [][] array) {
        int row=0;
        int col=array[0].length-1;
        while(row<=array.length-1&&col>=0){
            if(target==array[row][col])
                return true;
            else if(target>array[row][col])
                row++;
            else
                col--;
        }
        return false;

    }
    public static boolean find(int target, int [][] array) {

        for(int i = 0;i < array.length;i++) {
            for(int j = 0;j < array[i].length;j++) {
                if (target == array[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void  main(String[] args) {
        int [][] arr = new int[][]{{1,2,3},{2,3,4}};
        int target = 1;
        System.out.println(Find(arr,target));
    }
}
