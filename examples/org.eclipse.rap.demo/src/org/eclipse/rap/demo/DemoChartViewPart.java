/*******************************************************************************
 * Copyright (c) 2012, 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.demo;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.rap.demo.customComponent.PieChart;
import org.eclipse.rap.demo.customComponent.PieChartData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;


public class DemoChartViewPart extends ViewPart {

  private PieChart chart;
  private Label selectedDescription;
  private Spinner selectedValue;

  private PieChartData[] chartData = new PieChartData[] { 
    new PieChartData( "Im Vorratsschrank", 1, "rgb(27, 158, 119)"), 
    new PieChartData( "Im Kühlschrank", 2, "rgb(217, 95, 2)"), 
    new PieChartData( "In der Laptoptastatur", 10, "rgb(117, 112, 179)"), 
    new PieChartData( "In den Couchritzen", 11, "rgb(102, 166, 30)") 
  };
  
  @Override
  public void createPartControl( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    GridLayoutFactory.fillDefaults().numColumns( 1 ).applyTo( composite );
    Label title = new Label( composite, SWT.NONE );
    title.setText( "Nahrungsvorräte meiner Wohnung" );
    title.setAlignment( SWT.CENTER );
    FontData[] fontData = title.getFont().getFontData();
    fontData[0].setHeight( 25 );
    title.setFont( new Font( parent.getDisplay(), fontData ) );
    GridDataFactory.fillDefaults().grab( true, false ).applyTo( title );
    
    chart = new PieChart( composite, SWT.NONE );
    chart.setData(chartData);
    
    GridDataFactory.fillDefaults().grab( true, true ).hint( SWT.DEFAULT, 100 ).applyTo( chart );
    
    chart.addListener( SWT.Selection, evt -> 
      setSelected( composite, evt.index )
    );
  }
  
  Listener selectionListener = null;
  
  private void setSelected(Composite parent, int index) {
    if(selectedDescription == null) {
      Composite composite = new Composite( parent, SWT.NONE );
      GridDataFactory.fillDefaults().grab( true, false ).applyTo( composite );
      GridLayoutFactory.fillDefaults().numColumns( 2 ).applyTo( composite );
      
      selectedDescription = new Label( composite, SWT.NONE );
      GridDataFactory.fillDefaults().align( SWT.FILL, SWT.CENTER ).indent( 20, 0 ).grab( true, false ).applyTo( selectedDescription );
      selectedValue = new Spinner( composite, SWT.NONE );
    }
    selectedDescription.setText( chartData[index].label );
    if(selectionListener != null) {
      selectedValue.removeListener(SWT.Selection, selectionListener );
    }
    selectedValue.setValues( (int) chartData[index].value, 0, 100, 0, 1, 10);

    selectionListener = evt -> {
      chartData[index].value = selectedValue.getSelection();
      chart.setData(chartData);      
    };
    selectedValue.addListener( SWT.Selection, selectionListener );
    
    
    parent.layout();
  }

  @Override
  public void setFocus() {
  }
}
