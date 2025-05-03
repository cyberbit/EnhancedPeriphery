package dev.cyberbit.enhancedperiphery;

import dan200.computercraft.api.detail.VanillaDetailRegistries;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import dan200.computercraft.api.peripheral.PeripheralType;
import dan200.computercraft.core.util.ArgumentHelpers;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EnhancedInventoryMethods implements GenericPeripheral {
    @Override
    public String id() {
        return EnhancedPeriphery.MODID + ":inventory_enhanced";
    }

    @Override
    public PeripheralType getType() {
        return PeripheralType.ofAdditional("inventory_enhanced");
    }

    private Map<String, Object> getBasicDetailsWithCapabilities(ItemStack itemStack) {
        var basicMap = VanillaDetailRegistries.ITEM_STACK.getBasicDetails(itemStack);

        if (!itemStack.isEmpty()) {
            itemStack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(consumer -> basicMap.put("hasInventory", true));
            itemStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(consumer -> basicMap.put("hasEnergy", true));
            itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(consumer -> basicMap.put("hasFluid", true));
        }

        return basicMap;
    }

    private Map<Integer, Map<String, Object>> deepInventory(IItemHandler inventory) {
        Map<Integer, Map<String, Object>> result = new HashMap<>();

        for (int i = 0; i < inventory.getSlots(); i++) {
            var stack = inventory.getStackInSlot(i);

            if (stack.isEmpty()) continue;

            var basicMap = getBasicDetailsWithCapabilities(stack);

            var inventoryCap = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();

            inventoryCap.ifPresent(consumer -> basicMap.put("inventory", deepInventory(consumer)));

            result.put(i + 1, basicMap);
        }

        return result;
    }

    @Nullable
    @LuaFunction(mainThread = true)
    public Map<Integer, Map<String, Object>> listWithCapabilities(IItemHandler inventory) {
        Map<Integer, Map<String, Object>> result = new HashMap<>();

        var size = inventory.getSlots();

        for (var i = 0; i < size; i++) {
            var itemStack = inventory.getStackInSlot(i);

            if (itemStack.isEmpty()) continue;

            result.put(i + 1, getBasicDetailsWithCapabilities(itemStack));
        }

        return result;
    }

    @Nullable
    @LuaFunction(mainThread = true)
    public Map<Integer, Map<String, Object>> getItemInventory(IItemHandler inventory, int slot) throws LuaException {
        ArgumentHelpers.assertBetween(slot, 1, inventory.getSlots(), "Slot out of range (%s)");

        var stack = inventory.getStackInSlot(slot - 1);

        if (stack.isEmpty()) return null;

        // is stack an inventory? :o
        var inventoryCap = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();

        if (inventoryCap.isPresent()) {
            var itemInventory = inventoryCap.get();

            return listWithCapabilities(itemInventory);
        }

        return null;
    }

    @Nullable
    @LuaFunction(mainThread = true)
    public Map<Integer, Map<String, Object>> getItemInventoryDeep(IItemHandler inventory, int slot) throws LuaException {
        ArgumentHelpers.assertBetween(slot, 1, inventory.getSlots(), "Slot out of range (%s)");

        var stack = inventory.getStackInSlot(slot - 1);

        if (stack.isEmpty()) return null;

        // is stack an inventory? :o
        var inventoryCap = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();

        if (inventoryCap.isPresent()) {
            var itemInventory = inventoryCap.get();

            return deepInventory(itemInventory);
        }

        return null;
    }
}
