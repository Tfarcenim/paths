package com.tfar.examplemod;

import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class MutableAttributeModifier extends AttributeModifier {

  protected double amount;

  public MutableAttributeModifier(UUID idIn, String nameIn, double amountIn, int operationIn) {
    super(idIn, nameIn, amountIn, operationIn);
  }


  @Override
  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }
}
