package com.accolite.advjava;

import utils.Helper;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	if( args.length != 3 ) {
    		return ;
    	}
    	
    	String command = args[0] ;
    	String filePath = args[1] ;
    	String tableName = args[2] ;
    	
    	if( command.equals("import") ) {
    		
    		System.out.println("importing from "+filePath);
    		Helper.XMLToTable(filePath, tableName);
    		
    	} else if ( command.equals("export") ) {
    		
    		System.out.println("Exporting to table "+filePath );
    		Helper.TableToXML(filePath, tableName+"s", tableName);
    		
    		
    	} else {
    		
    		System.out.println("Invalid Command");
    		return ;
    		
    	}
    	

    }
}
