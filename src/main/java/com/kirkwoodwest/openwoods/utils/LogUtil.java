package com.kirkwoodwest.openwoods.utils;

import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.ControllerHost;

/**
 * Simple Log tool to initialized in the main extension and then used in any class.
 */
public class LogUtil {
  private static ControllerHost _host = null;
  public static void init(ControllerHost host){
    _host = host;
  }
  public static void print(String s){

    _host.println(s);
  }
  public static void println(String s){
    _host.println(s);
  }

	public static void h1(String s) {
    _host.println("--- " + s.toUpperCase() + "--------------------------");
	}

	public static void indent(String s, int indent) {
    String s2 = "";
    for(int i=0;i<indent;i++){
      s2 = s2 + "-";
    }
    s2 = s2 + " " + s;
    _host.println(s2);
  }

  public static ControllerHost getHost(){
    return _host;
  }


  public static void reportExtensionStatus(ControllerExtension extension, String status_message) {
    //If your reading this... Say hello to a loved one today. <3
    String name = extension.getExtensionDefinition().getName();
    String version = extension.getExtensionDefinition().getVersion();
    _host.println(name + " " + version + " " + status_message);
  }

}
