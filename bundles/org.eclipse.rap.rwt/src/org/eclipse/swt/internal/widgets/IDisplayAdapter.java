/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.swt.internal.widgets;

import org.eclipse.rwt.service.ISessionStore;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;



/**
 * TODO [rh] JavaDoc
 */
public interface IDisplayAdapter {
  
  final static IFilterEntry[] EMPTY_FILTERS = new IFilterEntry[ 0 ];
  
  interface IFilterEntry {
    int getType();
    Listener getListener();
  }
  
  void setBounds( Rectangle bounds );
  void setActiveShell( Shell shell );
  void setFocusControl( Control control );
  ISessionStore getSession();
  IFilterEntry[] getFilters();
}
