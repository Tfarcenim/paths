package com.tfar.examplemod;

import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;
import java.util.function.Supplier;

public class MutableAttributeModifier extends AttributeModifier {

  protected double amount;

  public MutableAttributeModifier(String nameIn, double amountIn, Operation operationIn) {
    super(nameIn, amountIn, operationIn);
  }

  public MutableAttributeModifier(UUID uuid, String nameIn, double amountIn, Operation operationIn) {
    super(uuid, nameIn, amountIn, operationIn);
  }

  public MutableAttributeModifier(UUID uuid, Supplier<String> nameIn, double amountIn, Operation operationIn) {
    super(uuid, nameIn, amountIn, operationIn);
  }

  @Override
  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }
}
