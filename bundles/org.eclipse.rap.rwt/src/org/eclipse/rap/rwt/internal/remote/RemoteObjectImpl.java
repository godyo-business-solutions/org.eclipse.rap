/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.rwt.internal.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.rap.rwt.internal.protocol.ProtocolMessageWriter;
import org.eclipse.rap.rwt.internal.service.ContextProvider;
import org.eclipse.rap.rwt.internal.util.ParamCheck;


public class RemoteObjectImpl implements RemoteObject {

  private final String id;
  private final List<RenderRunnable> renderQueue;
  private boolean destroyed;

  public RemoteObjectImpl( final String id, final String type ) {
    this.id = id;
    destroyed = false;
    renderQueue = new ArrayList<RenderRunnable>();
    renderQueue.add( new RenderRunnable() {
      public void render( ProtocolMessageWriter writer ) {
        writer.appendCreate( id, type );
      }
    } );
  }

  public String getId() {
    return id;
  }

  public void set( final String name, final int value ) {
    ParamCheck.notNullOrEmpty( name, "name" );
    checkState();
    renderQueue.add( new RenderRunnable() {
      public void render( ProtocolMessageWriter writer ) {
        writer.appendSet( id, name, value );
      }
    } );
  }

  public void set( final String name, final double value ) {
    ParamCheck.notNullOrEmpty( name, "name" );
    checkState();
    renderQueue.add( new RenderRunnable() {
      public void render( ProtocolMessageWriter writer ) {
        writer.appendSet( id, name, value );
      }
    } );
  }

  public void set( final String name, final boolean value ) {
    ParamCheck.notNullOrEmpty( name, "name" );
    checkState();
    renderQueue.add( new RenderRunnable() {
      public void render( ProtocolMessageWriter writer ) {
        writer.appendSet( id, name, value );
      }
    } );
  }

  public void set( final String name, final String value ) {
    ParamCheck.notNullOrEmpty( name, "name" );
    checkState();
    renderQueue.add( new RenderRunnable() {
      public void render( ProtocolMessageWriter writer ) {
        writer.appendSet( id, name, value );
      }
    } );
  }

  public void set( final String name, final Object value ) {
    ParamCheck.notNullOrEmpty( name, "name" );
    checkState();
    renderQueue.add( new RenderRunnable() {
      public void render( ProtocolMessageWriter writer ) {
        writer.appendSet( id, name, value );
      }
    } );
  }

  public void listen( final String eventType, final boolean listen ) {
    ParamCheck.notNullOrEmpty( eventType, "eventType" );
    checkState();
    renderQueue.add( new RenderRunnable() {
      public void render( ProtocolMessageWriter writer ) {
        writer.appendListen( id, eventType, listen );
      }
    } );
  }

  public void call( final String method, final Map<String, Object> properties ) {
    ParamCheck.notNullOrEmpty( method, "method" );
    checkState();
    renderQueue.add( new RenderRunnable() {
      public void render( ProtocolMessageWriter writer ) {
        writer.appendCall( id, method, properties );
      }
    } );
  }

  public void destroy() {
    checkState();
    renderQueue.add( new RenderRunnable() {
      public void render( ProtocolMessageWriter writer ) {
        writer.appendDestroy( id );
      }
    } );
    destroyed = true;
  }

  public boolean isDestroyed() {
    return destroyed;
  }

  public void handleSet( Map<String, Object> properties ) {
  }

  public void handleCall( String method, Map<String, Object> properties ) {
  }

  public void handleNotify( String event, Map<String, Object> properties ) {
  }

  public void render( ProtocolMessageWriter writer ) {
    for( RenderRunnable runnable : renderQueue ) {
      runnable.render( writer );
    }
    renderQueue.clear();
  }

  void checkState() {
    // TODO [rst] Prevent calls with fake context as they break thread confinement
    if( !ContextProvider.hasContext() ) {
      throw new IllegalStateException( "Remote object called from wrong thread" );
    }
    if( destroyed ) {
      throw new IllegalStateException( "Remote object is destroyed" );
    }
  }

  public interface RenderRunnable {

    void render( ProtocolMessageWriter writer );

  }

}
