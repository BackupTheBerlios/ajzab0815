package de.applejuicenet.client.gui.plugins;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.utils.javabroser.JavaBrowser;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/ajzab0815/Repository/ApplelinksPlugin/de/applejuicenet/client/gui/plugins/ApplelinksPlugin.java,v 1.1 2004/01/22 22:45:16 zab0815 Exp $
 *
 * <p>Titel: AppleJuice Core-GUI</p>
 * <p>Beschreibung: Erstes GUI für den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: open-source</p>
 *
 * @author: zab0815 <andy@zab0815.homelinux.org>
 *
 * $Log: ApplelinksPlugin.java,v $
 * Revision 1.1  2004/01/22 22:45:16  zab0815
 * initial commit,
 * poor HTML rendering,
 * no back, no fwd buttons,
 * component does not really fit into frame
 *
 * Version 0.9.1 Alpha
 * Initial checkin
 *
 */

public class ApplelinksPlugin extends PluginConnector {
    
    private static Logger logger;
    private JavaBrowser html;
    
    public ApplelinksPlugin() {
    	try{
    		logger = Logger.getLogger(getClass());
            initIcon();
            //setLayout(new BorderLayout());
            
            html =new JavaBrowser();
            html.displayPage("http://www.applelinks.org");
            html.setSize(getWidth(), getHeight());
            add(html);
            setBackground(Color.BLACK);
        }
        catch (Exception e){
            if (logger.isEnabledFor(Level.ERROR))
                logger.error("Unbehandelte Exception", e);
        }
    }

    public static void main ( String[] args )  {
    	JavaBrowser html;
    	
    	String initPage = "" ;
    	JFrame jf =new JFrame("HTML-Test");
    	JPanel jp =new JPanel();
    	
    	jf.setSize(500, 300);
 
    	jp.setBorder(new EmptyBorder(5,5,5,5));
    	jf.getContentPane().add(jp);
    	
    	html =new JavaBrowser();
		try {
			html.displayPage("http://www.applelinks.org");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jp.add(html);
    	jp.setBackground(Color.BLACK);
    	jf.show();
    	
    }


    
    
    public void fireLanguageChanged() {
    }

    /*Wird automatisch aufgerufen, wenn neue Informationen vom Server eingegangen sind.
      Über den DataManger können diese abgerufen werden.*/
    public void fireContentChanged(int type, Object content) {
    }

    public JPanel getOptionPanel(){
    	return null;
    }
    
    public void registerSelected() {
    }

    public String getTitle() {
        return "Applelinks";
    }

    public String getAutor() {
        return "Zab0815";
    }

    public String getBeschreibung() {
        return "Das Applelinks plugin zeigt die webseite www.applelinks.org.";
    }

    public String getVersion() {
        return "0.9.a";
    }

    public boolean istReiter() {
        return true;
    }
}