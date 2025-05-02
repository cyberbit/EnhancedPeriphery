package dev.cyberbit.enhancedperiphery;

import dan200.computercraft.api.detail.VanillaDetailRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.registries.RegistryManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Registration {
    // Register our stuff
    public static void register() {
        VanillaDetailRegistries.ITEM_STACK.addProvider((out, stack) -> {
            stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(consumer -> {
                Map<String, Integer> energyMap = new HashMap<>();

                energyMap.put("stored", consumer.getEnergyStored());
                energyMap.put("capacity", consumer.getMaxEnergyStored());

                out.put("energy", energyMap);
            });

            stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(consumer -> {
                Map<Integer, Map<String, ?>> tanksMap = new HashMap<>();

                var size = consumer.getTanks();

                for (var i = 0; i < size; i++) {
                    var fluidStack = consumer.getFluidInTank(i);

                    if (!fluidStack.isEmpty()) {
                        Map<String, Object> tankMap = new HashMap<>();

                        var fluidName = Optional.ofNullable(RegistryManager.ACTIVE.getRegistry(Registries.FLUID).getKey(fluidStack.getFluid()));

                        tankMap.put("name", fluidName.map(ResourceLocation::toString).orElse(null));
                        tankMap.put("amount", fluidStack.getAmount());
                        tankMap.put("capacity", consumer.getTankCapacity(i));

                        tanksMap.put(i + 1, tankMap);
                    }
                }

                out.put("tanks", tanksMap);
            });
        });
    }
}