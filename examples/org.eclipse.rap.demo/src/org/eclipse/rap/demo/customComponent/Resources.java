/*******************************************************************************
 * Copyright (c) 2013, 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.demo.customComponent;

import org.eclipse.rap.rwt.RWT;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.service.ResourceManager;


public class Resources {

  private final Map<String, String> locations = new HashMap<>();

  public String register( String resourcePath ) {
    ResourceManager resourceManager = RWT.getResourceManager();
    
    if( locations.get( resourcePath ) == null ) {
      try( InputStream inputStream = getClass().getResourceAsStream( resourcePath ) ) {
        resourceManager.register( resourcePath, inputStream );
      } catch( Exception exception ) {
        throw new RuntimeException( "Failed to register resource " + resourcePath, exception );
      }
    }
    String location = resourceManager.getLocation( resourcePath );
    locations.put( resourcePath, location );
    return location;
  }

}
