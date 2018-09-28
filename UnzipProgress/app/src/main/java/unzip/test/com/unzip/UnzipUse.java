package unzip.test.com.unzip;

import android.text.TextUtils;

import java.io.IOException;

import unzip.test.com.utils.L;
import unzip.test.com.utils.SDCardUtils;
import unzip.test.com.utils.T;

/**
 * Created by Administrator on 2018/9/28.
 */

public class UnzipUse {
    private UnZipMain zipFail;

    /**
     *
     * @param filePath  需要解压的文件路径
     * @param unzipPath  解压路径
     */
    private void unzip(String filePath,String unzipPath) {
        L.e("解压文件路径:" + filePath);
        if (TextUtils.isEmpty(SDCardUtils.getStoragePath(false))) {
            T.toast("SD卡不可用");

            return;
        }

        zipFail = ZipProgressUtil.UnZipFile(filePath, unzipPath, new ZipListener() {
            @Override
            public void zipStart() {
                L.e("zipStart");

            }

            @Override
            public void zipSuccess() {
                L.e("zipSuccess:" + Thread.currentThread().getName());
                //解压成功



            }

            @Override
            public void zipProgress(int progress) {
                L.e("progress" + progress);

            }

            @Override
            public void zipFail() {
                L.e("zipFail");
                T.toast("文件不可用");

            }
        });

    }

    private void destroy() {
        if (zipFail != null) {
            try {
                zipFail.disZip();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
