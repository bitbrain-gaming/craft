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

package de.bitbrain.craft.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.bitbrain.craft.Styles;
import de.bitbrain.craft.core.IconManager.Icon;
import de.bitbrain.craft.models.Item.Rarity;

/**
 * List element which shows basic element info
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class ElementInfo extends Table {
	
	
	public ElementInfo(ElementData data) {
		this.debug();
		Label name = new Label(data.getName(), Styles.LBL_ITEM);
		name.setColor(data.getRarity().getColor());
		
		RarityIcon icon = new RarityIcon(data.getIcon());
		
		add(icon);
		add(name);
	}
	
	
	public static interface ElementData {
		
		Icon getIcon();
		
		String getDescription();
		
		Rarity getRarity();
		
		String getName();
	}
}