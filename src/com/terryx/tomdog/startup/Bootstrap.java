package com.terryx.tomdog.startup;

import com.terryx.tomdog.*;
import com.terryx.tomdog.connector.http.HttpConnector;
import com.terryx.tomdog.core.SimpleContext;
import com.terryx.tomdog.core.SimpleContextMapper;
import com.terryx.tomdog.core.SimpleLoader;
import com.terryx.tomdog.core.SimpleWrapper;
import com.terryx.tomdog.valves.ClientIPLoggerValve;
import com.terryx.tomdog.valves.HeaderLoggerValve;

import java.io.IOException;

/**
 * @author taoranxue on 7/9/17 9:11 PM.
 */
public class Bootstrap {
    public static final String[] strs = new String[]{"        ,----,                                                               ",
            "      ,/   .`| ,----..            ____                 ,----..               ",
            "    ,`   .'  :/   /   \\         ,'  , `.   ,---,      /   /   \\   ,----..    ",
            "  ;    ;     /   .     :     ,-+-,.' _ | .'  .' `\\   /   .     : /   /   \\   ",
            ".'___,/    ,.   /   ;.  \\ ,-+-. ;   , |,---.'     \\ .   /   ;.  |   :     :  ",
            "|    :     .   ;   /  ` ;,--.'|'   |  ;|   |  .`\\  .   ;   /  ` .   |  ;. /  ",
            ";    |.';  ;   |  ; \\ ; |   |  ,', |  ':   : |  '  ;   |  ; \\ ; .   ; /--`   ",
            "`----'  |  |   :  | ; | |   | /  | |  ||   ' '  ;  |   :  | ; | ;   | ;  __  ",
            "    '   :  .   |  ' ' ' '   | :  | :  |'   | ;  .  .   |  ' ' ' |   : |.' .' ",
            "    |   |  '   ;  \\; /  ;   . |  ; |--'|   | :  |  '   ;  \\; /  .   | '_.' : ",
            "    '   :  |\\   \\  ',  /|   : |  | ,   '   : | /  ; \\   \\  ',  /'   ; : \\  | ",
            "    ;   |.'  ;   :    / |   : '  |/    |   | '` ,/   ;   :    / '   | '/  .' ",
            "    '---'     \\   \\ .'  ;   | |`-'     ;   :  .'      \\   \\ .'  |   :    /   ",
            "               `---`    |   ;/         |   ,.'         `---`     \\   \\ .'    ",
            "                        '---'          '---'                      `---`      ",
            "              :: Tomdog Boot ::        (v0.1.1)           \n\n"};


    public static void main(String[] args) {
        for (String len : strs) {
            System.out.println(len);
        }
//        HttpConnector connector = new HttpConnector();
////        connector.start();
//        Wrapper wrapper = new SimpleWrapper();
//        wrapper.setServletClass("ModernServlet");
//        Loader loader = new SimpleLoader();
//        Valve valve1 = new HeaderLoggerValve();
//        Valve valve2 = new ClientIPLoggerValve();
//        wrapper.setLoader(loader);
//        ((Pipeline) wrapper).addValve(valve1);
//        ((Pipeline) wrapper).addValve(valve2);
//        connector.setContainer(wrapper);
//
//        try {
////            connector.initialize();
//            connector.start();
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        HttpConnector connector = new HttpConnector();
//        Wrapper wrapper1 = new SimpleWrapper();
//        wrapper1.setName("Primitive");
//        wrapper1.setServletClass("PrimitiveServlet");
        Wrapper wrapper2 = new SimpleWrapper();
        wrapper2.setName("Modern");
        wrapper2.setServletClass("ModernServlet");

        Context context = new SimpleContext();
//        context.addChild(wrapper1);
        context.addChild(wrapper2);

        Valve valve1 = new HeaderLoggerValve();
        Valve valve2 = new ClientIPLoggerValve();

        ((Pipeline) context).addValve(valve1);
        ((Pipeline) context).addValve(valve2);

        Mapper mapper = new SimpleContextMapper();
        mapper.setProtocol("http");
        context.addMapper(mapper);
        Loader loader = new SimpleLoader();
        context.setLoader(loader);
        // context.addServletMapping(pattern, name);
        context.addServletMapping("/Primitive", "Primitive");
        context.addServletMapping("/Modern", "Modern");
        connector.setContainer(context);
        try {
//            connector.initialize();
            connector.start();

            // make the application wait until we press a key.
            System.in.read();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
