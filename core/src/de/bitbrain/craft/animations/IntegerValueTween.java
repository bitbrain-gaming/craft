package de.bitbrain.craft.animations;

import de.bitbrain.craft.animations.TweenAnimations.TweenType;
import de.bitbrain.craft.util.IntegerValueProvider;

public class IntegerValueTween extends AbstractTween<IntegerValueProvider> {

  @Override
  public int getValues(IntegerValueProvider target, TweenType type, float[] returnValues) {
    switch (type) {
      case VALUE:
        returnValues[0] = target.getValue();
        return 1;
      default:
        return 0;
    }
  }

  @SuppressWarnings("incomplete-switch")
  @Override
  public void setValues(IntegerValueProvider target, TweenType type, float[] newValues) {
    switch (type) {
      case VALUE:
        target.setValue(Math.round(newValues[0]));
        break;
    }
  }

}