package org.superboot.config.datasource;

/**
 * <b> 数据源绑定线程 </b>
 * <p>
 * 功能描述:提供数据源线程池本地绑定服务
 * </p>
 */
public class DynamicDataSourceHolder {

    //使用ThreadLocal把数据源与当前线程绑定
    private static final ThreadLocal<String> dataSources = new ThreadLocal<String>();


    public static void setDataSource(String dataSourceName) {
        dataSources.set(dataSourceName);
    }

    public static String getDataSource() {
        return (String) dataSources.get();
    }

    public static void clearDataSource() {
        dataSources.remove();
    }


}
