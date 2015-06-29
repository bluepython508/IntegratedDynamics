package org.cyclops.integrateddynamics.core.part.aspect;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.integrateddynamics.core.client.model.VariableModelBaked;
import org.cyclops.integrateddynamics.core.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.core.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.core.evaluate.variable.IVariable;
import org.cyclops.integrateddynamics.core.item.VariableFacadeBase;
import org.cyclops.integrateddynamics.core.network.Network;
import org.cyclops.integrateddynamics.core.part.write.IPartStateWriter;

import java.util.List;

/**
 * Variable facade for variables determined by part aspects.
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AspectVariableFacade extends VariableFacadeBase {

    private final int partId;
    private final IAspect aspect;

    public AspectVariableFacade(boolean generateId, int partId, IAspect aspect) {
        super(generateId);
        this.partId = partId;
        this.aspect = aspect;
    }

    public AspectVariableFacade(int id, int partId, IAspect aspect) {
        super(id);
        this.partId = partId;
        this.aspect = aspect;
    }

    @Override
    public <V extends IValue> IVariable<V> getVariable(Network network) {
        if(isValid() && getAspect() instanceof IAspectRead && network.hasPart(getPartId())) {
            return network.getPartVariable(getPartId(), (IAspectRead) getAspect());
        }
        return null;
    }

    @Override
    public boolean isValid() {
        return getPartId() >= 0 && getAspect() != null;
    }

    @Override
    public void validate(Network network, IPartStateWriter validator) {
        if (getPartId() < 0) {
            validator.setError(validator.getActiveAspect(), new L10NHelpers.UnlocalizedString("aspect.error.invalidVariableItem"));
        } else if (!(getAspect() instanceof IAspectRead
                && network.hasPart(getPartId()))) {
            validator.setError(validator.getActiveAspect(), new L10NHelpers.UnlocalizedString("aspect.error.partNotInNetwork",
                    Integer.toString(getPartId())));
        } else if (validator.getActiveAspect().getValueType() != getAspect().getValueType()) {
            validator.setError(validator.getActiveAspect(), new L10NHelpers.UnlocalizedString("aspect.error.invalidType",
                    new L10NHelpers.UnlocalizedString(validator.getActiveAspect().getValueType().getUnlocalizedName()),
                    new L10NHelpers.UnlocalizedString(getAspect().getValueType().getUnlocalizedName())));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(List<String> list, EntityPlayer entityPlayer) {
        if(isValid()) {
            getAspect().loadTooltip(list, false);
            list.add(L10NHelpers.localize("item.items.integrateddynamics.variable.partId", getPartId()));
        }
        super.addInformation(list, entityPlayer);
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void addModelOverlay(VariableModelBaked variableModelBaked, List<BakedQuad> quads) {
        if(isValid()) {
            IAspect aspect = getAspect();
            IValueType valueType = aspect.getValueType();
            quads.addAll(variableModelBaked.getValueTypeSubModels().get(valueType).getGeneralQuads());
            quads.addAll(variableModelBaked.getAspectSubModels().get(aspect).getGeneralQuads());
        }
    }
}
