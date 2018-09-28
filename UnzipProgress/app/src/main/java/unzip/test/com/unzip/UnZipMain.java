package unzip.test.com.unzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;



public class UnZipMain {

    String zipFileString;
    String outPathString;
    ZipListener listener;
    private static final int BUFF_SIZE = 1024 * 1024 * 10; // 10M Byte
    private ZipInputStream inZip;
    private FileOutputStream out;
    private Disposable disposable;

    public UnZipMain(String zipFileString, String outPathString, ZipListener listener) {
        this.zipFileString = zipFileString;
        this.outPathString = outPathString;
        this.listener = listener;
    }
    public void disZip() throws IOException {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            inZip.close();
            out.close();
        }
    }

    public void  unzip() {
     disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                try {
                    // listener.zipStart();
                    e.onNext(-1);
                    long sumLength = 0;
                    // 获取解压之后文件的大小,用来计算解压的进度
                    long ziplength = getZipTrueSize(zipFileString);
                  //  L.e("====文件的大小==" + ziplength);
                    FileInputStream inputStream = new FileInputStream(zipFileString);
                    inZip = new ZipInputStream(inputStream);

                    ZipEntry zipEntry;
                    String szName = "";
                    while ((zipEntry = inZip.getNextEntry()) != null) {
                        szName = zipEntry.getName();
                        if (zipEntry.isDirectory()) {
                            szName = szName.substring(0, szName.length() - 1);
                            File folder = new File(outPathString + File.separator + szName);
                            folder.mkdirs();
                        } else {
                            File file = new File(outPathString + File.separator + szName);
                            file.createNewFile();
                            out = new FileOutputStream(file);
                            int len;
                            byte[] buffer = new byte[BUFF_SIZE];
                            while ((len = inZip.read(buffer)) != -1) {
                              //  L.e("代码执行中");
                                sumLength += len;
                                int progress = (int) ((sumLength * 100) / ziplength);
                                // updateProgress(progress, listener);
                                if (progress > lastProgress) {
                                    lastProgress = progress;
                                    // listener.zipProgress(progress);

                                    e.onNext(progress);
                                }

                                out.write(buffer, 0, len);
                                out.flush();
                            }
                            out.close();
                        }
                    }
                    // listener.zipSuccess();
                    e.onNext(-2);
                    inZip.close();
                } catch (Exception ex) {
                    // listener.zipFail();
                    e.onNext(-3);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        switch (integer) {
                            case -1:
                                listener.zipStart();
                                break;
                            case -2:
                                listener.zipSuccess();
                                break;
                            case -3:
                                listener.zipFail();
                                break;
                            default:
                                if (integer > 0)
                                    listener.zipProgress(integer);

                        }
                    }
                });


    }


    int lastProgress = 0;

    private void updateProgress(int progress, ZipListener listener2) {
        /** 因为会频繁的刷新,这里我只是进度>1%的时候才去显示 */
        if (progress > lastProgress) {
            lastProgress = progress;
            listener2.zipProgress(progress);

        }
    }

    /**
     * 获取压缩包解压后的内存大小
     *
     * @param filePath
     *            文件路径
     * @return 返回内存long类型的值
     */
    public long getZipTrueSize(String filePath) {
        long size = 0;
        ZipFile f;
        try {
            f = new ZipFile(filePath);
            Enumeration<? extends ZipEntry> en = f.entries();
            while (en.hasMoreElements()) {
                size += en.nextElement().getSize();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

}