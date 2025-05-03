package dev.cyberbit.enhancedperiphery;


import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.shared.ModRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = EnhancedPeriphery.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ComputerCraftEnhancementInitiative {
    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(ModRegistry::registerMainThread);

        ComputerCraftAPI.registerGenericSource(new EnhancedInventoryMethods());
    }
}

