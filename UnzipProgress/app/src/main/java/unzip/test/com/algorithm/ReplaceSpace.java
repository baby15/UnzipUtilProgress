package unzip.test.com.algorithm;

public class ReplaceSpace {
    public static void main(String[] args) {
        System.out.println(replaceSpace(new StringBuffer("i love you  ")));
    }
    private static String replaceSpace(String ss) {

        return ss.toString().replace(" ","%20");
    }


    public static String replaceSpace(StringBuffer str) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<str.length();i++){
            char c = str.charAt(i);
            if(c == ' '){
                sb.append("%20");
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
