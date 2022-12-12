/*******************************************************************************
 * Copyright (c) 2022 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.demo.customComponent;

import org.eclipse.rap.json.*;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.ClientFileLoader;
import org.eclipse.rap.rwt.remote.*;
import org.eclipse.rap.rwt.widgets.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

public class PieChart extends Canvas {

  private static final String D3_JS_URL = "https://d3js.org/d3.v7.min.js";
  private static final String REMOTE_TYPE = "rwt.chart.PieChart";
  
  private final transient RemoteObject remoteObject;
  
  public PieChart(Composite parent, int style) {
    super( parent, style );

    remoteObject = RWT.getUISession().getConnection().createRemoteObject( REMOTE_TYPE );
    remoteObject.set( "parent", WidgetUtil.getId( this ) );
    remoteObject.setHandler( new ChartOperationHandler());

    Resources resources = new Resources();//SingletonUtil.getUniqueInstance( Resources.class, RWT.getApplicationContext() );
    
    requireJs( D3_JS_URL );
    requireJs( resources.register( "chart.js" ));
    requireCss( resources.register( "ChartStyle.css" ));
  }
  
  public void setData(PieChartData... data) {
    
    JsonArray array = new JsonArray();
    for(PieChartData d : data) {
      array.add( d.toJSON() );
    }
    remoteObject.set( "items", array );
  }
  
  private class ChartOperationHandler extends AbstractOperationHandler {
    @Override
    public void handleNotify( String eventName, JsonObject properties ) {
      if( "Selection".equals( eventName ) ) {
        Event event = new Event();
        event.index = properties.get( "index" ).asInt();
        notifyListeners( SWT.Selection, event );
      }
    }
  }
  
  private static void requireJs( String url ) {
    RWT.getClient().getService( ClientFileLoader.class ).requireJs( url );
  }
  
  private static void requireCss( String url ) {
    RWT.getClient().getService( ClientFileLoader.class ).requireCss( url );
  }
  
  @Override
  public void addListener( int eventType, Listener listener ) {
    boolean wasListening = isListening( SWT.Selection );
    super.addListener( eventType, listener );
    if( eventType == SWT.Selection && !wasListening ) {
      remoteObject.listen( "Selection", true );
    }
  }

  @Override
  public void removeListener( int eventType, Listener listener ) {
    boolean wasListening = isListening( SWT.Selection );
    super.removeListener( eventType, listener );
    if( eventType == SWT.Selection && wasListening && !isListening( SWT.Selection ) ) {
      remoteObject.listen( "Selection", false );
    }
  }
}
