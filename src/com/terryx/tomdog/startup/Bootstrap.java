package com.terryx.tomdog.startup;

import com.terryx.tomdog.connector.http.HttpConnector;

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
            "------------------------------   VERSION 0.01   ------------------------------\n\n"};


    public static void main(String[] args) {
        for (String len : strs) {
            System.out.println(len);
        }
        HttpConnector connector = new HttpConnector();
        connector.start();
    }
}
