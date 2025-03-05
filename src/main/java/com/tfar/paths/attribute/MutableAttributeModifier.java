package com.tfar.paths.attribute;

import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class MutableAttributeModifier extends AttributeModifier {
    protected double amount;

    public MutableAttributeModifier(UUID uuid, String name, double amount, int operation) {
        super(uuid, name, amount, operation);
    }

    @Override
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
