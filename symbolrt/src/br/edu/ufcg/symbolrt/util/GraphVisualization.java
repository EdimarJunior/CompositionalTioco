/*
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * (C) Copyright 2010-2013 Federal University of Campina Grande (UFCG)
 * 
 * This file is part of SYMBOLRT.
 *
 * SYMBOLRT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SYMBOLRT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SYMBOLRT.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * REVISION HISTORY:
 * Author                           Date           Brief Description
 * -------------------------------- -------------- ------------------------------
 * Wilkerson de Lucena Andrade      20/07/2010     Initial version
 * 
 */
package br.edu.ufcg.symbolrt.util;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.base.Transition;

import com.mxgraph.io.mxCodec;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.png.mxPngEncodeParam;
import com.mxgraph.util.png.mxPngImageEncoder;
import com.mxgraph.view.mxGraph;

/**
 * <code>GraphVisualization</code> Class. <br>
 * This class is used to create a visualization of TIOSTS models. Furthermore, this class can save an image of the models in the PNG format.
 * 
 * @author Wilkerson de Lucena Andrade  ( <a href="mailto:wilkerson@computacao.ufcg.edu.br">wilkerson@computacao.ufcg.edu.br</a> )
 * 
 * @version 1.0
 * <br>
 * SYmbolic Model-Based test case generation toOL for Real-Time systems (SYMBOLRT)
 * <br>
 * (C) Copyright 2010-2013 Federal University of Campina Grande (UFCG)
 * <br>
 * <a href="https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt">https://sites.google.com/a/computacao.ufcg.edu.br/symbolrt</a>
 */
public class GraphVisualization {

	private Hashtable<String, Object> style;
	private final String LOCATION_STYLE = "ROUNDED";

	
	
	
	/**
	 * Creates a new GraphVisualization object for visualizing TIOSTS models.
	 */
	public GraphVisualization(){
		style = new Hashtable<String, Object>();
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		style.put(mxConstants.STYLE_GRADIENTCOLOR, "#49A2DD");
		style.put(mxConstants.STYLE_FILLCOLOR, "white");
	}
	
	

	
	/**
	 * Shows a TIOSTS model inside a window.
	 * @param tiosts The TIOSTS model to be shown.
	 */
	public void show(TIOSTS tiosts){
		// Translates a TIOSTS to mxGraph 
		mxGraph graph = TIOSTS2mxGraph(tiosts);
				
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setBorder(BorderFactory.createTitledBorder(tiosts.getName()));


		// ********** Creates a Menu Bar **********
		JMenuBar menuBar = new JMenuBar();
		
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setMnemonic('x');
		exitMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		
		JMenuItem saveasPNGMenuItem = new JMenuItem("Save as PNG");
		saveasPNGMenuItem.setMnemonic('s');
		saveasPNGMenuItem.addActionListener(new SaveasPNGActionListener(tiosts.getName(), graphComponent));
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		fileMenu.add(saveasPNGMenuItem);
		fileMenu.add(exitMenuItem);
		
		menuBar.add(fileMenu);
		// ****************************************

		
		JFrame frame = new JFrame(tiosts.getName());
		frame.setJMenuBar(menuBar);
		frame.getContentPane().add(graphComponent);
		
		//mxIGraphLayout layout = new mxCircleLayout(graph);
		mxIGraphLayout layout = new mxHierarchicalLayout(graph);
		layout.execute(graph.getDefaultParent());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 750);
		frame.setVisible(true);
	}
	
	
	/**
	 * Saves a graph component as a PNG file.
	 * @param fileName The name of the file.
	 * @param graphComponent The graph component to be saved.
	 * @throws IOException     
	 */
	private void saveAsPNG(String fileName, mxGraphComponent graphComponent) throws IOException {
		// Creates the image for the PNG file
		BufferedImage image = mxCellRenderer.createBufferedImage(graphComponent.getGraph(),
				null, 1, new Color(255,255,255), graphComponent.isAntiAlias(), null,
				graphComponent.getCanvas());
		
		// Creates the URL-encoded XML data
		mxCodec codec = new mxCodec();
		String xml = URLEncoder.encode(mxUtils.getXml(codec.encode(graphComponent.getGraph().getModel())), "UTF-8");

		mxPngEncodeParam param = mxPngEncodeParam.getDefaultEncodeParam(image);
		param.setCompressedText(new String[] { "mxGraphModel", xml });

		// Saves as a PNG file
		FileOutputStream outputStream = new FileOutputStream(new File(fileName+".png"));
		mxPngImageEncoder encoder = new mxPngImageEncoder(outputStream, param);

		if (image != null) {
			encoder.encode(image);
		} else {
			System.out.println("Warning: " + mxResources.get("noImageData"));
		}
		
		outputStream.close();
	}
	
	
	/**
	 * Saves a TIOSTS model as an image.
	 * @param tiosts The TOSTS model to be saved.
	 */
	public void save(TIOSTS tiosts){
		// Translates a TIOSTS to mxGraph 
		mxGraph graph = TIOSTS2mxGraph(tiosts);
		
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		
		// Defines the layout
		mxIGraphLayout layout = new mxHierarchicalLayout(graph);
		layout.execute(graph.getDefaultParent());

		try {
			saveAsPNG(tiosts.getName(), graphComponent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Converts a TIOSTS model into a mxGraph model.
	 * @param tiosts The TIOSTs to be converted into a mxGraph.
	 * @return The mxGraph model converted from a TIOSTS.
	 */
	private mxGraph TIOSTS2mxGraph(TIOSTS tiosts){
		mxGraph graph = new mxGraph();
		graph.getStylesheet().putCellStyle(this.LOCATION_STYLE, style); // Define the style of TIOSTS locations
		Object parent = graph.getDefaultParent();
	
		graph.getModel().beginUpdate();
		try {
		
			Map<String, Object> locations = new HashMap<String, Object>();

			Collection<Transition> transitions = tiosts.getTransitions();
			for (Transition transition : transitions) {
			
				Object sourceVertex;
				Object targetVertex;
				Object transitionVertex;
			
				transitionVertex = graph.insertVertex(parent, null, transition.toString(), 20, 20, 20, 20);
				graph.updateCellSize(transitionVertex);
			
				String sourceLabel = transition.getSource().getLabel();
				if (!locations.containsKey(sourceLabel)){
					sourceVertex = graph.insertVertex(parent, null, sourceLabel, 20, 20, sourceLabel.length()*10, 20, this.LOCATION_STYLE);
					locations.put(sourceLabel, sourceVertex);
				} else {
					sourceVertex = locations.get(sourceLabel);
				}
			
				graph.insertEdge(parent, null, "", sourceVertex, transitionVertex);
			
			
				String targetLabel = transition.getTarget().getLabel();
				if (!locations.containsKey(targetLabel)){
					targetVertex = graph.insertVertex(parent, null, targetLabel, 20, 20, targetLabel.length()*10, 20, this.LOCATION_STYLE);
					locations.put(targetLabel, targetVertex);
				} else {
					targetVertex = locations.get(targetLabel);
				}
			
				graph.insertEdge(parent, null, "", transitionVertex, targetVertex);
			}

		} finally {
			graph.getModel().endUpdate();
		}
		
		return graph;
	}
	

	
	/**
	 * <code>SaveasPNGActionListener</code> Class. <br>
	 * This class is used as a listener for the "Save as PNG" menu item. It is responsible for saving the TIOSTS visualization as a PNG file.
	 * 
	 * @author Wilkerson de Lucena Andrade  ( <a href="mailto:wilker@dsc.ufcg.edu.br">wilker@dsc.ufcg.edu.br</a> )
	 * 
	 * @version 1.0
	 * <br>
	 * Copyright (C) 2010 SyMBol RT (Symbolic Model-Based Test Generation Tool for Real-Time Systems).
	 */
	private class SaveasPNGActionListener implements ActionListener {

		private String fileName;
		private mxGraphComponent graphComponent;
		
		/**
		 * Creates a new listener for the "Save as PNG" menu item based on the following parameters. 
		 * @param fileName The name of the file to be saved.
		 * @param graphComponent The component containing the model to be saved.
		 */
		public SaveasPNGActionListener(String fileName, mxGraphComponent graphComponent){
			this.fileName = fileName;
			this.graphComponent = graphComponent;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				saveAsPNG(this.fileName, this.graphComponent);
				JOptionPane.showMessageDialog(graphComponent, "Image successfully saved.");
			} catch (IOException exception) {
				JOptionPane.showMessageDialog(graphComponent, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	} // End of class SaveasPNGActionListener.	
	
	
	
	
} // End of class GraphVisualization. 



