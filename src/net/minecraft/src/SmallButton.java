package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class SmallButton extends GuiButton {
    public SmallButton(int id, int x, int y, String text) {
        super(id, x, y, 12, 12, text);
    }

    public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
        if (!drawButton) {
            return;
        }

        FontRenderer fontrenderer = par1Minecraft.fontRenderer;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1Minecraft.renderEngine.getTexture("/gui/small.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        boolean flag = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + 12 && par3 < yPosition + 12;
        int i = getHoverState(flag);
        drawTexturedModalRect(xPosition, yPosition, i * 12, 0, 12, 12);
        mouseDragged(par1Minecraft, par2, par3);
        int j = 0xe0e0e0;

        if (!enabled) {
            j = 0xa0a0a0;
        } else if (flag) {
            j = 0xffffa0;
        }

        drawCenteredString(fontrenderer, displayString, xPosition + 6, yPosition + 2, j);
    }
}
