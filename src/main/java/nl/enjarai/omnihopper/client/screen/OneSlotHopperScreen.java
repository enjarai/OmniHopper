package nl.enjarai.omnihopper.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
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
		passEvents = false;
		backgroundHeight = 133;
		playerInventoryTitleY = backgroundHeight - 94;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, TEXTURE);
		int i = (width - backgroundWidth) / 2;
		int j = (height - backgroundHeight) / 2;
		drawTexture(matrices, i, j, 0, 0, backgroundWidth, backgroundHeight);
	}
}
