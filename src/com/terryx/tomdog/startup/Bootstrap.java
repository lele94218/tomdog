package com.terryx.tomdog.startup;

import com.terryx.tomdog.Loader;
import com.terryx.tomdog.Pipeline;
import com.terryx.tomdog.Valve;
import com.terryx.tomdog.Wrapper;
import com.terryx.tomdog.connector.http.HttpConnector;
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
        HttpConnector connector = new HttpConnector();
//        connector.start();
        Wrapper wrapper = new SimpleWrapper();
        wrapper.setServletClass("ModernServlet");
        Loader loader = new SimpleLoader();
        Valve valve1 = new HeaderLoggerValve();
        Valve valve2 = new ClientIPLoggerValve();
        wrapper.setLoader(loader);
        ((Pipeline) wrapper).addValve(valve1);
        ((Pipeline) wrapper).addValve(valve2);
        connector.setContainer(wrapper);

        try {
//            connector.initialize();
            connector.start();
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
