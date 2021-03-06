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

package de.bitbrain.craft.ui.widgets;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.google.inject.Inject;

import de.bitbrain.craft.Assets;
import de.bitbrain.craft.Sizes;
import de.bitbrain.craft.Styles;
import de.bitbrain.craft.animations.IntegerValueTween;
import de.bitbrain.craft.animations.TweenAnimations.TweenType;
import de.bitbrain.craft.events.Event.EventType;
import de.bitbrain.craft.events.EventBus;
import de.bitbrain.craft.events.ItemEvent;
import de.bitbrain.craft.events.MouseEvent;
import de.bitbrain.craft.graphics.GraphicsFactory;
import de.bitbrain.craft.graphics.IconManager;
import de.bitbrain.craft.graphics.IconManager.IconDrawable;
import de.bitbrain.craft.inject.SharedInjector;
import de.bitbrain.craft.models.Item;
import de.bitbrain.craft.util.FloatValueProvider;
import de.bitbrain.craft.util.IntegerValueProvider;

/**
 * An icon which also shows rarity and special effects
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class IconWidget extends Actor implements IntegerValueProvider {

  public float iconScale;

  private NinePatch background;

  private Label amountLabel;

  private IconDrawable icon;

  private int currentAmount, amount;

  @Inject
  private TweenManager tweenManager;

  @Inject
  private IconManager iconManager;

  @Inject
  private EventBus eventBus;

  private Item item;
  
  private FloatValueProvider iconOffset = new FloatValueProvider();
  
  private FloatValueProvider labelScale = new FloatValueProvider();
  
  private float currentIconOffset = 0f;

  private IconHandle iconHandle = new DefaultIconHandle();

  public class DefaultIconHandle implements IconHandle {

    @Override
    public Color getColor(int currentAmount) {
      if (currentAmount == Item.INFINITE_AMOUNT) {
        return Assets.CLR_YELLOW_SAND;
      } else {
        return Color.WHITE;
      }
    }

    @Override
    public String getContent(int currentAmount) {
      if (currentAmount == Item.INFINITE_AMOUNT) {
        return "INF";
      } else {
        return String.valueOf(currentAmount);
      }
    }

    @Override
    public boolean isVisible(int currentAmount) {
      return amount >= Item.INFINITE_AMOUNT;
    }

    @Override
    public boolean isDraggable(int amount) {
      return amount > 0 || amount == Item.INFINITE_AMOUNT;
    }

    @Override
    public int getDragAmount() {
      return 1;
    }

  };

  public IconWidget(Item item, int amount) {
    Tween.registerAccessor(IconWidget.class, new IntegerValueTween());
    SharedInjector.get().injectMembers(this);
    this.item = item;
    this.amount = amount;
    amountLabel = new Label("1", Styles.LBL_TOOLTIP);
    amountLabel.setFontScale(2.1f);
    background = GraphicsFactory.createNinePatch(Assets.TEX_PANEL_TRANSPARENT_9patch, Sizes.panelTransparentRadius());
    this.icon = iconManager.fetch(item.getIcon());
    this.currentAmount = amount;
    labelScale.setValue(2f);
    registerEvents();
  }

  public final void setSource(Item item, int amount) {
    this.item = item;
    this.icon = iconManager.fetch(item.getIcon());
    this.amount = amount;
    animateAmount();
  }
  
  public void setIconOffset(float offset) {
    if (offset != 0) {
      if (currentIconOffset == 0 ) {
        iconOffset.setValue(offset);
        Tween.to(iconOffset, TweenType.VALUE.ordinal(), 0.75f).target(0).ease(TweenEquations.easeOutBounce).repeat(Tween.INFINITY, 0).start(tweenManager);
      }
    } else {
      tweenManager.killTarget(iconOffset);
      Tween.to(iconOffset, TweenType.VALUE.ordinal(), 0.4f).target(0).ease(TweenEquations.easeOutQuad).start(tweenManager);
    }
    currentIconOffset = offset;
  }
  
  public void setLabelScale(float scale) {
    tweenManager.killTarget(labelScale);
    Tween.to(labelScale, TweenType.VALUE.ordinal(), 0.15f).target(scale).ease(TweenEquations.easeOutCubic).start(tweenManager);
  }

  public final void setHandle(IconHandle iconHandle) {
    this.iconHandle = iconHandle;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.badlogic.gdx.scenes.scene2d.Actor#draw(com.badlogic.gdx.graphics. g2d.Batch, float)
   */
  @Override
  public void draw(Batch batch, float parentAlpha) {

    float iconScale = 0.8f;

    // background
    background.setColor(getColor());
    background.draw(batch, getX(), getY(), getWidth(), getHeight());

    // Icon
    icon.width = getWidth() * -iconScale;
    icon.height = getHeight() * iconScale;
    icon.x = getX() + (getWidth() - icon.width) / 2;
    icon.y = getY() + (getHeight() - icon.height) / 2 - iconOffset.getValue();
    icon.rotation = 180f;
    icon.color = getColor();
    icon.draw(batch, parentAlpha);

    if (iconHandle.isVisible(currentAmount)) {
      amountLabel.setText(iconHandle.getContent(currentAmount));
      
      if (Math.abs(labelScale.getValue()) > 0) {
        amountLabel.setFontScale(Math.abs(labelScale.getValue()));
      }
      amountLabel.setColor(iconHandle.getColor(currentAmount));
      amountLabel.setX(getX() + getWidth() - amountLabel.getPrefWidth() - getPadding() / 2f);
      amountLabel.setY(getY() + getPadding());
      amountLabel.draw(batch, parentAlpha);
    }
  }

  public void addAmount(int amount) {
    if (this.amount != Item.INFINITE_AMOUNT) {
      this.amount += amount;
      animateAmount();
    }
  }

  public void reduceAmount(int amount) {
    if (this.amount != Item.INFINITE_AMOUNT) {
      this.amount -= amount;
      if (this.amount < 0) {
        this.amount = 0;
      }
      animateAmount();
    }
  }

  private float getPadding() {
    return 16f;
  }

  @Override
  public int getValue() {
    return currentAmount;
  }

  @Override
  public void setValue(int value) {
    currentAmount = value;
  }

  private void animateAmount() {
    tweenManager.killTarget(this);
    Tween.to(this, TweenType.VALUE.ordinal(), 1f).target(amount).ease(TweenEquations.easeOutQuart).start(tweenManager);
  }

  private void registerEvents() {
    // Allow dragging for icons only
    addListener(new DragListener() {
      @Override
      public void dragStart(InputEvent event, float x, float y, int pointer) {
        if (iconHandle.isDraggable(amount) && (amount == Item.INFINITE_AMOUNT || iconHandle.getDragAmount() <= amount)) {
          MouseEvent<Item> mouseEvent =
              new MouseEvent<Item>(EventType.MOUSEDRAG, item, Sizes.localMouseX(), Sizes.localMouseY());
          mouseEvent.setParam(ItemEvent.AMOUNT, iconHandle.getDragAmount());
          eventBus.fireEvent(mouseEvent);
        }
      }

      @Override
      public void dragStop(InputEvent event, float x, float y, int pointer) {
        if (iconHandle.isDraggable(amount) && (amount == Item.INFINITE_AMOUNT || iconHandle.getDragAmount() <= amount)) {
          MouseEvent<Item> mouseEvent =
              new MouseEvent<Item>(EventType.MOUSEDROP, item, Sizes.localMouseX(), Sizes.localMouseY());
          mouseEvent.setParam(ItemEvent.AMOUNT, iconHandle.getDragAmount());
          eventBus.fireEvent(mouseEvent);
        }
      }
    });
    addListener(new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        super.enter(event, x, y, pointer, fromActor);
        final float BOUNCE = 15f;
        Tween.to(IconWidget.this, TweenType.POS_Y.ordinal(), 0.5f).target(getY() + BOUNCE)
            .ease(TweenEquations.easeInOutBounce).repeat(Tween.INFINITY, 0).start(tweenManager);
      }

      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        super.exit(event, x, y, pointer, toActor);
        tweenManager.killTarget(IconWidget.this);
      }
    });
    addCaptureListener(new InputListener() {
      /*
       * (non-Javadoc)
       * 
       * @see com.badlogic.gdx.scenes.scene2d.InputListener#touchDown(com.badlogic .gdx.scenes.scene2d.InputEvent,
       * float, float, int, int)
       */
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return true;
      }
    });
  }

  public static interface IconHandle {

    Color getColor(int currentAmount);

    String getContent(int currentAmount);

    boolean isVisible(int currentAmount);

    boolean isDraggable(int amount);

    int getDragAmount();
  }
}