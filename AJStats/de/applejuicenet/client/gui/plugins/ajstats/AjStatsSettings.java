/*
 * Created on 02.01.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.applejuicenet.client.gui.plugins.ajstats;

import java.awt.Color;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.charts.updownchart.UpDownChart;

/**
 * @author Zab
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AjStatsSettings extends JPanel {
	
	private JButton btnUpColor;
	private JButton btnDownColor;
	private UpDownChart ud;
	private JLabel lblUpColor;
	private JLabel lblDownColor;
	
	public AjStatsSettings(UpDownChart ud){
		
		this.ud=ud;
		initialize();
		
	}
	
	private void initialize() {
		setSize(500, 500);
		
		add(getlblUpColor(), null);
		add(getbtnUpColor(), null);
		add(getlblDownColor(), null);
		add(getbtnDownColor(), null);

	}
	
	private JLabel getlblUpColor() {
		if(lblUpColor == null) {
			lblUpColor = new JLabel();
			lblUpColor.setBounds(180,100,80,20);
			lblUpColor.setText("Upload Color");
			lblUpColor.setOpaque(true);
			lblUpColor.setBackground(ud.getUpCol());
		}
		return lblUpColor;
	}
	
	private javax.swing.JButton getbtnUpColor() {
		if(btnUpColor == null) {
			btnUpColor = new javax.swing.JButton();
			btnUpColor.setBounds(190, 100, 80, 20);
			btnUpColor.setText("Wählen");
			btnUpColor.setMnemonic('W');
			
			btnUpColor.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					Color col = JColorChooser.showDialog(getPanel(), "Upload Farbe wählen", ud.getBGStart());
					if (col != null) {
						ud.setUpCol(col);
						lblUpColor.setBackground(ud.getUpCol());
					}
				}
			});
		}
		return btnUpColor;
	}
	
	private JLabel getlblDownColor() {
		if(lblDownColor == null) {
			lblDownColor = new JLabel();
			lblDownColor.setBounds(180,125,80,20);
			lblDownColor.setText("Download Color");
			lblDownColor.setOpaque(true);
			lblDownColor.setBackground(ud.getDownCol());
		}
		return lblDownColor;
	}
	
	private javax.swing.JButton getbtnDownColor() {
		if(btnDownColor == null) {
			btnDownColor = new javax.swing.JButton();
			btnDownColor.setBounds(190, 125, 80, 20);
			btnDownColor.setText("Wählen");
			btnDownColor.setMnemonic('h');
						
			btnDownColor.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					Color col = JColorChooser.showDialog(getPanel(), "Download Farbe wählen", ud.getBGEnd());
					if (col != null) {
						ud.setDownCol(col);
						lblDownColor.setBackground(ud.getDownCol());
					}
				}
			});
		}
		return btnDownColor;
	}
	
	public JPanel getPanel(){
		return this;
	}
	
	/**
	 * @return Icon with 18x18 from given color
	 */
	private Icon getColorIcon(Color color) {
		Image ico = createVolatileImage(18, 18);
		
		try {
			ico.getGraphics().setColor(color);
			ico.getGraphics().fillRect(0,0,17,17);
			//System.out.println("Icon drawn");
		} catch (NullPointerException e) {
			//System.out.println("Error in getColorIcon() "); e.printStackTrace();
			//System.out.println("Icon not drawn");
			return null;
		}
		
		return new ImageIcon(ico);
	}

	private Color chooseColor(Color color) {
		btnUpColor.setIcon(getColorIcon(ud.getUpCol()));
		btnDownColor.setIcon(getColorIcon(ud.getDownCol()));
		return Color.BLACK;
	}
}
