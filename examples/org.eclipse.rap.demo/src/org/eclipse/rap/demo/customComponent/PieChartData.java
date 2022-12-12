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

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;

public class PieChartData {
  public String label;
  public float value;
  public String color;
  
  public PieChartData( String label, float value, String color ) {
    this.label = label;
    this.value = value;
    this.color = color;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel( String label ) {
    this.label = label;
  }

  public float getValue() {
    return value;
  }
  
  public void setValue( float value ) {
    this.value = value;
  }

  public String getColor() {
    return color;
  }
  
  public void setColor( String color ) {
    this.color = color;
  }
  
  public JsonValue toJSON() {
    return new JsonObject()
    .add( "label", label)
    .add( "value", value)
    .add( "color", color);
  }
  
}
