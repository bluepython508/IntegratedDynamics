package org.cyclops.integrateddynamics.part.aspect.read.world;

import com.google.common.math.DoubleMath;
import net.minecraft.server.MinecraftServer;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeInteger;
import org.cyclops.integrateddynamics.core.part.PartTarget;
import org.cyclops.integrateddynamics.core.part.aspect.property.AspectProperties;

/**
 * Aspect that displays the average tick time of the target space world.
 * @author rubensworks
 */
public class AspectReadIntegerWorldTicktime extends AspectReadIntegerWorldBase {

    @Override
    protected String getUnlocalizedIntegerWorldType() {
        return "ticktime";
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ValueTypeInteger.ValueInteger getValue(PartTarget target, AspectProperties properties) {
        return ValueTypeInteger.ValueInteger.of((int) DoubleMath.mean(MinecraftServer.getServer().worldTickTimes.get(target.getTarget().getPos().getWorld().provider.getDimensionId())));
    }
}
