package com.icicles.wmms.utils;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class TestJNI {

    /**
     * DLL动态库调用方法
     * @Description: 读取调用CDecl方式导出的DLL动态库方法
     * @author: LinWenLi
     * @date: 2018年7月18日 上午10:49:02
     */
    public interface CLibrary extends Library {
        // DLL文件默认路径为项目根目录，若DLL文件存放在项目外，请使用绝对路径。（此处：(Platform.isWindows()?"msvcrt":"c")指本地动态库msvcrt.dll）
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary("TSC_SDK",CLibrary.class);

        // 声明将要调用的DLL中的方法,可以是多个方法(此处示例调用本地动态库msvcrt.dll中的printf()方法)
        int OpenPort(String format,int a);
        Boolean ClosePort();
        //int POS_Open(String format,int a);

        //Boolean POS_Close();
    }

    public static void main(String[] args) {
        CLibrary.INSTANCE.ClosePort();

        /*ActiveXComponent xl = new ActiveXComponent("Excel.Application");
        Dispatch xlo = (Dispatch)(xl.getObject());
        try {
            System.out.println("version="+xl.getProperty("Version"));
            System.out.println("version="+Dispatch.get(xlo, "Version"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            xl.invoke("Quit", new Variant[] {});
        }*/

    }
}
