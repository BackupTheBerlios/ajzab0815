package de.applejuicenet.client.gui.plugins;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.applejuicenet.client.gui.controller.ApplejuiceFassade;
import de.applejuicenet.client.gui.listener.DataUpdateListener;
import de.applejuicenet.client.gui.plugins.ajstats.AjStatsSettings;
import de.applejuicenet.client.gui.plugins.ajstats.GraphPanel;
import de.charts.updownchart.UpDownChart;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/ajzab0815/Repository/AJStatsPlugin/de/applejuicenet/client/gui/plugins/AJStatsPlugin.java,v 1.3 2004/06/17 10:14:03 zab0815 Exp $
 *
 * <p>Titel: AppleJuice Core-GUI</p>
 * <p>Beschreibung: Erstes GUI f�r den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: open-source</p>
 *
 * @author: zab0815 <aj@tkl-soft.de>
 *
 * $Log: AJStatsPlugin.java,v $
 * Revision 1.3  2004/06/17 10:14:03  zab0815
 * Fix BUG with timertask, read settings for max up/download speed, read initial settings
 *
 * Revision 1.2  2004/05/28 21:47:41  zab0815
 * Next try...
 * need to delete data when graph is full and make things multithreaded.
 * Also the timer has to define the scroll-speed.
 *
 * Revision 1.1  2004/05/21 18:52:21  zab0815
 * First commit since upgrading to new Plugin interface. Only few changes with settings are needed.
 *
 * Revision 1.1  2004/01/06 20:49:07  andy
 * Initial checkin
 *
 * Revision 0.9  2003/12/22 16:25:35  zab0815
 * Neues Plugin auf basis von SpeedGraph vom Maj0r.
 *
 */
public class AJStatsPlugin 
	extends PluginConnector 
    implements FocusListener, ActionListener, ComponentListener, KeyListener,
    MouseListener {

    private GraphPanel graphPanel = null;
    private UpDownChart ud = null;
    private static Logger logger;
    private AjStatsSettings settings = null;
	
	private String savePath;
    private Properties properties;
    private XMLValueHolder pluginsPropertiesXMLHolder;

    private void init(){
		logger = Logger.getLogger(getClass());
        savePath = ApplejuiceFassade.getPropertiesPath();
        int index = savePath.lastIndexOf(File.separator);
        savePath = savePath.substring(0, index) + File.separator +
            "ajstatsplugin.properties";
        properties = new Properties();
        
        File tmpFile = new File(savePath);
        if (tmpFile.isFile()){
            try {
                properties.load(new FileInputStream(tmpFile));
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        setLayout(new BorderLayout());
		
        graphPanel = new GraphPanel();
        graphPanel.setBackground(Color.BLACK);
        
        ud = new UpDownChart();
        ud.setParent(this);
        addPropertyChangeListener(ud);
        addMouseListener(ud);
        addMouseMotionListener(ud);
        String propGradient = properties.getProperty("UseGradient");
        if (propGradient != null){
        	Integer propGradientDir = null;
        	try {
            	propGradientDir = Integer.decode(properties.getProperty("GradientDirection").toString());
			} catch (Exception e) {
				propGradientDir = new Integer(UpDownChart.BG_UP_TO_DOWN);
				properties.put("GradientDirection", Integer.toString(UpDownChart.BG_NO_GRADIENT));
			} 

        	ud.setGradientDirection(propGradientDir.intValue());
        	ud.setBGStart(getPropertyColor("GradientStart",Color.white));
        	ud.setBGEnd(getPropertyColor("GradientEnd",Color.green));
        	ud.setUpdatePeriod(Long.parseLong(properties.getProperty("UpdateTime","2000").toString()));
        } else
        	ud.setGradientDirection(UpDownChart.BG_NO_GRADIENT);

        JSplitPane jsPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,graphPanel,ud);
        jsPane.setDividerLocation(getHeight()/2);
        add(jsPane, BorderLayout.CENTER);
        setBackground(Color.BLACK);
    }

    public void saveProperties(){
        try {
            properties.store(new FileOutputStream(savePath),
                             "ajStatsPlugin-PropertyFile");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Properties getProperties(){
        return properties;
    }	
	
    public void setPropertyColor(String propertyKey, Color ColorKey) {
    	
    	String valString=null;
    	valString = "0x" + Integer.toHexString(ColorKey.getRGB());
    	
    	properties.put(propertyKey, valString);
//    	System.out.println("Key:" + propertyKey + " Val:" + valString);
    }
    
    public Color getPropertyColor(String propertyKey, Color defaultCol) {
    	
    	try{
    		String keyStr = properties.get(propertyKey).toString();
    		Long keyLong = Long.decode(keyStr);
//    		System.out.println( propertyKey + ":" + Long.toHexString(keyLong.longValue()) +
//    						   " Red:" + ((int)((keyLong.longValue() >> 16) & 0xff)) +
//							   " Green:" + ((int)(keyLong.longValue() >> 8) & 0xff) +
//							   " Blue:" + ((int)keyLong.longValue() & 0xff) +
//							   " Alpha:" + ((int)(keyLong.longValue() >> 24) & 0xff));
    		
    		return  new Color((int)(keyLong.longValue() >> 16) & 0xff,
    						  (int)(keyLong.longValue() >> 8) & 0xff,
    						  (int)(keyLong.longValue()) & 0xff,
    						  (int)(keyLong.longValue() >> 24) & 0xff);
    	}
    	catch (Exception e) {
//    		System.out.println(e.toString() + " bei Key:" + propertyKey);
			return defaultCol;
		}
    }
    
    
    public AJStatsPlugin(XMLValueHolder pluginsPropertiesXMLHolder, Map languageFiles, ImageIcon icon) {
        super(pluginsPropertiesXMLHolder, languageFiles, icon);
        this.pluginsPropertiesXMLHolder = pluginsPropertiesXMLHolder;

//        addFocusListener(this);
    	try{
    		init();
            
            //registriere Plugin f�r Upload-�nderungen
            ApplejuiceFassade.getInstance().addDataUpdateListener(this,
                DataUpdateListener.SPEED_CHANGED);

            fireContentChanged(DataUpdateListener.SPEED_CHANGED, new HashMap());
        }
        catch (Exception e){
            if (logger.isEnabledFor(Level.ERROR))
                logger.error("Unbehandelte Exception", e);
        }
    }

    public static void main(String[] args) {
    	JFrame jp =new JFrame("Plugin test");
    	JButton jb =new JButton("Settings");
    	final UpDownChart plugin = new UpDownChart();
    	
    	JFrame.setDefaultLookAndFeelDecorated(true);
    	jp.setSize(800,600);
    	jb.setSize(100,50);
    	plugin.setSize(600,700);
    	
    	jb.addActionListener(new java.awt.event.ActionListener() { 
    		public void actionPerformed(java.awt.event.ActionEvent e) {    
    			JFrame js =new JFrame("Plugin Settings");
    			AjStatsSettings pluginSettings = new AjStatsSettings();
    			js.getContentPane().add((JPanel)pluginSettings);
    			
    			js.setSize(800,600);
    			js.show();
    		}
    	});
    	
    	jp.getContentPane().add(plugin);
    	jp.getContentPane().add (jb);
    	jp.show();
    }

    
    public void fireLanguageChanged() {
    }

    /*Wird automatisch aufgerufen, wenn neue Informationen vom Server eingegangen sind.
      �ber den DataManger k�nnen diese abgerufen werden.*/
    public void fireContentChanged(int type, Object content) {
        if (type==DataUpdateListener.SPEED_CHANGED){

            try {
                HashMap map = (HashMap) content; //ge�nderte Uploads
                
                graphPanel.update(map);
	            ud.update(map);
            }
            catch (Exception e) {
                logger.error("Fehler: " + e);
            }
        }
    }

    public JPanel getOptionPanel(){
    	settings = new AjStatsSettings();
    	settings.parent = this;
    	return settings.getPanel(); 
    }
    
    public void registerSelected() {
    }

    
	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}