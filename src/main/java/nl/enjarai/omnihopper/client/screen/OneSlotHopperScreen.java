package nl.enjarai.omnihopper.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.screen.OneSlotHopperScreenHandler;

@Environment(EnvType.CLIENT)
public class OneSlotHopperScreen extends AbstractContainerScreen<OneSlotHopperScreenHandler> {
	private static final Identifier TEXTURE = OmniHopper.id("textures/gui/container/one_slot_hopper.png");

	public OneSlotHopperScreen(OneSlotHopperScreenHandler handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
		imageHeight = 133;
		inventoryLabelY = imageHeight - 94;
	}

	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);
		renderTooltip(context, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
		int i = (width - imageWidth) / 2;
		int j = (height - imageHeight) / 2;
		context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0, 0, imageWidth, imageHeight, 256, 256);
	}
}
