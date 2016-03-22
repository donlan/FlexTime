package dong.lan.flextime.utils;

import dong.lan.flextime.Config;

/**
 * Created by 梁桂栋 on 2015/12/12.
 */
public class SortManager {
    public static double IMP=3.5;
    public static double URG =2.5;
    public static double getSortWeight(int imp,int urg,int status)
    {
        return (imp*IMP+URG*urg)* Config.getLevelFactor(status);
    }

    public static void init(double imp,double urg)
    {
        IMP = imp;
        URG = urg;
    }
}
