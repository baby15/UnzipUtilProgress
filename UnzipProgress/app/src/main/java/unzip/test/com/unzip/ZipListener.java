package unzip.test.com.unzip;


public interface ZipListener {
    /** 开始解压 */
    void zipStart();

    /** 解压成功 */
    void zipSuccess();

    /** 解压进度 */
    void zipProgress(int progress);

    /** 解压失败 */
    void zipFail();
}
