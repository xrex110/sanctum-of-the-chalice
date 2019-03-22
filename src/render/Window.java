package render;
import main.*;

import game.*;
import sound.*;
import object.*;


import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class Window extends JFrame {  

	private String titleText;	/* Text for JFrame title */
	private int xSize, ySize;	/* Size of Frame in x and y axis in px */
	private JPanel view;		/* The JPanel view to add the the Frame */
    private JPanel menus;
	public Window(String titleText, JPanel view, int x, int y) {
		this.titleText = titleText;
		this.xSize = x;
		this.ySize = y;
		this.view = view;
	    
		this.add(view);
        
        this.setTitle(titleText);
		this.setSize(xSize, ySize);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//this.setIgnoreRepaint(true);

		//Player cannot resize for now
		//We can look into scaling things later
		this.setResizable(false);
	}

	public Window(String titleText, JPanel view) {
		this(titleText, view, 800, 800);
	}

	public void setInputHandler(InputHandler ih) {
		this.addKeyListener(ih);
	}

	public void updateWindow() {
		this.setTitle(titleText);
		this.setSize(xSize, ySize);
		this.add(view);
	}

	public void showWindow() {
		this.setVisible(true);
	}

	public void hideWindow() {
		this.setVisible(false);
	}

	public void killWindow() {
		hideWindow();
		this.dispose();
	}

	public void setWindowSize(int x, int y) {
		xSize = x;
		ySize = y;
	}

	public void setWindowTitle(String title) {
		titleText = title;
	}
    public void setWindowView(JPanel newView) {
        this.getContentPane().remove(view);
        
        view = newView;
        this.add(newView);
	    this.getContentPane().invalidate();
        this.getContentPane().validate();
    }
    
    public JPanel getWindowView() {
        return view;
    }

}
