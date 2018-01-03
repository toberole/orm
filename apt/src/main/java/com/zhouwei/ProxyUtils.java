package com.zhouwei;

/**
 * Created by zhouwei on 2018/1/2.
 * <p>
 * 动态生成字节码
 * java里面的Proxy本质上是运行期动态的生成字节码
 */
public class ProxyUtils {

    /**
     * 将根据类信息 动态生成的二进制字节码保存到硬盘中 默认的是clazz目录下
     *
     * @param clazz     需要生成动态代理类的类
     * @param proxyName 为动态生成的代理类的名称
     */
//    public static void generateClassFile(Class clazz, String proxyName) {
//        try {
//            byte[] classFile = ProxyGenerator.generateProxyClass(proxyName, clazz.getInterfaces());
//            String paths = clazz.getResource(".").getPath();
//            System.out.println(paths);
//
//            FileOutputStream fout = new FileOutputStream(paths + proxyName + ".class");
//            fout.write(classFile);
//            fout.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
