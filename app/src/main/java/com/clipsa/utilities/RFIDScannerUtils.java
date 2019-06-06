package com.clipsa.utilities;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.zyapi.CommonApi;

import com.android.hdhe.uhf.reader.Conversion;

import timber.log.Timber;

public class RFIDScannerUtils {

    Context context;
    static CommonApi commonApi;
    /* RFID reader*/
    private static int mComFd =-1;
    private boolean isOpen = true;
    private final int MAX_RECEIVED_BUFFER_SIZE = 512;
    private byte [] receivedBytes;
    private final static int SHOW_RECEIVED_DATA = 1;
    private String strRead;

    Handler handler;

    showMessageListener showMessageListener;

    public RFIDScannerUtils(Context c){
        this.context = c;

        if(c instanceof showMessageListener)
        showMessageListener = (showMessageListener) c;
    }


    public RFIDScannerUtils(Context c, Handler h){
        this.context = c;
        this.handler = h;


        init();

        if(c instanceof showMessageListener)
            showMessageListener = (showMessageListener) c;
    }


    public CommonApi getCommonApi() {
        return commonApi;
    }

    public CommonApi init() {
        try {
            /* RFID */
            commonApi = new CommonApi();
            commonApi.setGpioDir(79, 0);
            commonApi.getGpioIn(79);
            int mBaudrate = 9600;
            String serialPortPath = "/dev/ttyMT2";
            mComFd = commonApi.openCom(serialPortPath, mBaudrate, 8, 'N', 1);

            new Handler().postDelayed(() -> {
                commonApi.setGpioDir(79, 1);
                commonApi.setGpioOut(79, 1);
            }, 500);

            return commonApi;
        } catch (UnsatisfiedLinkError | NoClassDefFoundError e) {
            e.printStackTrace();
            return null;
        }
    }




    public void readData(){
        new Thread(){
            public void run(){
                while(isOpen){
                    int ret;
                    byte[] buf = new byte[MAX_RECEIVED_BUFFER_SIZE +1];
                    AppLogger.e("mComfd = %s", mComFd);
                    ret = commonApi.readComEx(mComFd, buf, MAX_RECEIVED_BUFFER_SIZE, 0, 0);
                    if (ret <= 0) {
                        Timber.e("read failed!!!! ret: %s", ret);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    receivedBytes = new byte[ret];
                    System.arraycopy(buf, 0, receivedBytes, 0, ret);

                    strRead = Conversion.Bytes2HexString(receivedBytes);

                    AppLogger.e("~~~Read["+ret+"]: %s", strRead);
                    if(strRead!=null){
                        Message msg = handler.obtainMessage(SHOW_RECEIVED_DATA);
                        msg.obj=strRead;
                        msg.sendToTarget();
                    }
                }
            }
        }.start();
    }


    public void send(byte[] data){
        if(data == null) return;
        if(mComFd > 0){
            commonApi.writeCom(mComFd, data, data.length);
            Message msg = new Message();
            msg.what = 101;
            handler.sendMessageDelayed(msg, 60000);
        }
    }





    public interface showMessageListener{
        void showRFIDMessage(String message);
    }



    public static  CommonApi hasScanner() {
        try {
            /* RFID */
            commonApi = new CommonApi();
            commonApi.setGpioDir(79, 0);
            commonApi.getGpioIn(79);
            int mBaudrate = 9600;
            String serialPortPath = "/dev/ttyMT2";
            mComFd = commonApi.openCom(serialPortPath, mBaudrate, 8, 'N', 1);

            new Handler().postDelayed(() -> {
                commonApi.setGpioDir(79, 1);
                commonApi.setGpioOut(79, 1);
            }, 500);

            return commonApi;
        } catch (UnsatisfiedLinkError | NoClassDefFoundError ignore) {
            return null;
        }
    }




}
