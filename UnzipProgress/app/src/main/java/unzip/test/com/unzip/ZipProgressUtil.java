package unzip.test.com.unzip;




public class ZipProgressUtil {

    /***
     * 解压通用方法
     *
     * @param zipFileString
     *            文件路径
     * @param outPathString
     *            解压路径
     * @param listener
     *            加压监听
     */
    public static UnZipMain UnZipFile(final String zipFileString, final String outPathString, final ZipListener listener) {
        UnZipMain zipThread = new UnZipMain(zipFileString, outPathString, listener);
        zipThread.unzip();
        return zipThread;
    }


}
