package de.applejuicenet.client.gui.plugins;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;

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
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/ajzab0815/Repository/AJStats/de/applejuicenet/client/gui/plugins/AJStatsPlugin.java,v 1.1 2004/01/09 19:26:28 zab0815 Exp $
 *
 * <p>Titel: AppleJuice Core-GUI</p>
 * <p>Beschreibung: Erstes GUI für den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: open-source</p>
 *
 * @author: zab0815 <aj@tkl-soft.de>
 *
 * $Log: AJStatsPlugin.java,v $
 * Revision 1.1  2004/01/09 19:26:28  zab0815
 * Initial checkin
 * Version 0.9.1 Alpha
 *
 * Revision 1.2  2004/01/06 23:09:39  andy
 * insert averaging-graph
 * insert speed marks for DSL(128/768)
 * change image handling
 * insert buttons for changing up/download colors
 *
 * Revision 1.1  2004/01/06 20:49:07  andy
 * Initial checkin
 *
 * Revision 0.9  2003/12/22 16:25:35  zab0815
 * Neues Plugin auf basis von SpeedGraph vom Maj0r.
 *
 */
public class AJStatsPlugin extends PluginConnector {
    private GraphPanel graphPanel = new GraphPanel();
    private UpDownChart ud = new UpDownChart();
    private static Logger logger;
    private AjStatsSettings settings =new AjStatsSettings(ud);

    public AJStatsPlugin() {
    	try{
    		logger = Logger.getLogger(getClass());
            initIcon();
            setLayout(new BorderLayout());
            
            graphPanel.setBackground(Color.BLACK);
            ud.setGradientDirection(UpDownChart.BG_UP_TO_DOWN);
            JSplitPane jsPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,graphPanel,ud);
            jsPane.setDividerLocation(getHeight()/2);
            jsPane.setOneTouchExpandable(true);
            add(jsPane, BorderLayout.CENTER);
            setBackground(Color.BLACK);
            
            
            ApplejuiceFassade.getInstance().addDataUpdateListener(this, DataUpdateListener.SPEED_CHANGED);
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
    	
    	jb.addActionListener(new java.awt.event.ActionListener() { 
    		public void actionPerformed(java.awt.event.ActionEvent e) {    
    			JFrame js =new JFrame("Plugin Settings");
    			AjStatsSettings pluginSettings = new AjStatsSettings(plugin);
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
      Über den DataManger können diese abgerufen werden.*/
    public void fireContentChanged(int type, Object content) {
        if (type==DataUpdateListener.SPEED_CHANGED){
            ud.update((HashMap) content);
            graphPanel.update(ud.getData());
        }
    }

    public JPanel getOptionPanel(){
    	return settings.getPanel(); 
    }
    
    public void registerSelected() {
    }

    public String getTitle() {
        return "AJ-Stats";
    }

    public String getAutor() {
        return "Zab0815";
    }

    public String getBeschreibung() {
        return "Das AJ-Stats-Plugin zeigt Statistiken zu den\r\nDownload- und Uploadgeschwindigkeiten an.";
    }

    public String getVersion() {
        return "0.9.1";
    }

    public boolean istReiter() {
        return true;
    }
}