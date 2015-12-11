package org.cyclops.integrateddynamics.api.logicprogrammer;

import net.minecraft.item.ItemStack;
import org.cyclops.integrateddynamics.api.client.gui.subgui.IGuiInputElement;
import org.cyclops.integrateddynamics.api.item.IVariableFacade;
import org.cyclops.integrateddynamics.client.gui.GuiLogicProgrammer;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammer;

/**
 * An element instantiation inside the logic programmer.
 * @author rubensworks
 */
public interface ILogicProgrammerElement extends IGuiInputElement<GuiLogicProgrammer, ContainerLogicProgrammer> {

    /**
     * @return The element type.
     */
    public ILogicProgrammerElementType getType();

    /**
     * @return The string used to match regex searching.
     */
    public String getMatchString();

    /**
     * Called when an input item slot has been updated.
     * @param slotId The slot id.
     * @param itemStack The itemstack currently in the slot, can be null.
     */
    public void onInputSlotUpdated(int slotId, ItemStack itemStack);

    /**
     * @return If this element can be written to an item in its current state.
     */
    public boolean canWriteElementPre();

    /**
     * The stack to write the current state of this element to.
     * @param itemStack The stack to write to.
     * @return The resulting itemstack.
     */
    public ItemStack writeElement(ItemStack itemStack);

    /**
     * If this element in its current state can be deactivated because of another item being inserted into the
     * write slot.
     * @return If this element can be deactivated.
     */
    public boolean canCurrentlyReadFromOtherItem();

    /**
     * @param variableFacade A variable facade
     * @return If this element corresponds to the given variable facade.
     */
    public boolean isFor(IVariableFacade variableFacade);

    /**
     * Check if the given item can be inserted into the given slot.
     * @param slotId The slot id.
     * @param itemStack The item that will be inserted.
     * @return If it can be inserted.
     */
    public boolean isItemValidForSlot(int slotId, ItemStack itemStack);

}