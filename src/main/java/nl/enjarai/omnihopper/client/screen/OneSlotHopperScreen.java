package nl.enjarai.omnihopper.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.screen.OneSlotHopperScreenHandler;

@Environment(EnvType.CLIENT)
public class OneSlotHopperScreen extends HandledScreen<OneSlotHopperScreenHandler> {
	private static final Identifier TEXTURE = OmniHopper.id("textures/gui/container/one_slot_hopper.png");

	public OneSlotHopperScreen(OneSlotHopperScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		backgroundHeight = 133;
		playerInventoryTitleY = backgroundHeight - 94;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);
		drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int i = (width - backgroundWidth) / 2;
		int j = (height - backgroundHeight) / 2;
		context.drawTexture(TEXTURE, i, j, 0, 0, backgroundWidth, backgroundHeight);
	}
}
