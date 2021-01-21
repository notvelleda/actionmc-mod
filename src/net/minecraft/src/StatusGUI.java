package net.minecraft.src;

// Class to manage the status GUI
public class StatusGUI {
    public static String version = "0.1.0";
    public static boolean showFullStatus = false;
    public static GuiButton fixButton = null;

    // Draws the ActionMC text and service status text
    public static void drawStatusGUI(GuiScreen screen, int offset) {
        screen.drawString(screen.fontRenderer, "Action\u00a7dMC", screen.width - screen.fontRenderer.getStringWidth("ActionMC") - offset, offset, 0xffffff);

        if (showFullStatus) {
            if (StatusChecker.statusList.size() > 0) {
                int y = offset;
                for (String service : StatusChecker.getServices()) {
                    // Minecraft doesn't handle string widths with formatting properly, so two strings are needed (at least for right-wards justification)
                    String s1 = service + ": " + StatusChecker.getColoredPrettyStatus(service);
                    String s2 = service + ": " + StatusChecker.getPrettyStatus(service);
                    screen.drawString(screen.fontRenderer, s1, screen.width - screen.fontRenderer.getStringWidth(s2) - offset, y += 10, 0xffffff);
                }

                if (fixButton != null) {
                    fixButton.enabled = true;
                    fixButton = null;
                }
            } else {
                screen.drawString(screen.fontRenderer, "\u00a77Loading...", screen.width - screen.fontRenderer.getStringWidth("Loading...") - offset, offset + 10, 0xffffff);
            }
        } else {
            if (StatusChecker.shortStatusList.size() > 0) {
                String s1 = "";
                String s2 = "";
                
                for (String service : StatusChecker.getShortServices()) {
                    s1 += " " + StatusChecker.getStatusColor(service) + StatusChecker.getStatusSymbol(service) + " " + service;
                    s2 += " " + StatusChecker.getStatusSymbol(service) + " " + service;
                }

                screen.drawString(screen.fontRenderer, s1, screen.width - screen.fontRenderer.getStringWidth(s2) - offset, offset + 10, 0xffffff);

                if (fixButton != null) {
                    fixButton.enabled = true;
                    fixButton = null;
                }
            } else {
                screen.drawString(screen.fontRenderer, "\u00a77Loading...", screen.width - screen.fontRenderer.getStringWidth("Loading...") - offset, offset + 10, 0xffffff);
            }
        }
    }

    // Creates GUI Buttons
    public static void createButtons(GuiScreen screen, int offset) {
        SmallButton button;
        if (showFullStatus) {
            button = new SmallButton(255, screen.width - 14 - offset, StatusChecker.statusList.size() * 10 + 10 + offset, "▲");
        } else {
            button = new SmallButton(255, screen.width - 14 - offset, 20 + offset, "▼");
        }
        if (StatusChecker.statusList.size() <= 0) {
            button.enabled = false;
            fixButton = button;
        }
        screen.controlList.add(button);
    }

    // Handles button presses
    public static void handleButtonClick(GuiButton button, int offset) {
        if (button.id == 255) {
            showFullStatus = !showFullStatus;
            if (showFullStatus) {
                button.yPosition = StatusChecker.statusList.size() * 10 + 10 + offset;
                button.displayString = "▲";
            } else {
                button.yPosition = 20 + offset;
                button.displayString = "▼";
            }
        }
    }
}