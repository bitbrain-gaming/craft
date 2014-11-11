/*
 * Craft - Crafting game for Android, PC and Browser.
 * Copyright (C) 2014 Miguel Gonzalez
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package de.bitbrain.craft.models;

import java.util.List;

import de.bitbrain.craft.core.Icon;
import de.bitbrain.craft.util.Identifiable;
import de.bitbrain.jpersis.annotations.Ignored;
import de.bitbrain.jpersis.annotations.PrimaryKey;

/**
 * Recipe which describes how to make items out of other items
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class Recipe implements Identifiable {

	@PrimaryKey
	private String id;
	
	@Ignored
	private List<String> itemIds;
	
	private String productId;
	
	private String name;
	
	private String description;
	
	private Icon icon;
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setItemIds(List<String> items) {
		this.itemIds = items;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Icon getIcon() {
		return icon;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	public String getProductId() {
		return productId;
	}
	
	public void setProductId(String product) {
		this.productId = product;
	}
	
	public List<String> getItemIds() {
		return itemIds;
	}
	
	public String getName() {
		return name;
	}
}
