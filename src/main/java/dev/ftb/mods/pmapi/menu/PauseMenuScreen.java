package dev.ftb.mods.pmapi.menu;

import dev.ftb.mods.pmapi.api.PauseMenuApi;
import dev.ftb.mods.pmapi.api.menu.MenuLocation;
import dev.ftb.mods.pmapi.api.menu.PauseItemHolder;
import dev.ftb.mods.pmapi.api.menu.ScreenHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class PauseMenuScreen extends OriginalPauseScreenImpl {
    public static boolean DISABLE_CUSTOM_PAUSE = false;

    private final ScreenHolder holder = ScreenHolder.of(this);

    private final Set<ItemPlacement> placements = new HashSet<>();

    public PauseMenuScreen() {
        super();
    }

    @Override
    protected void init() {
        super.init();
        initPauseItems();
    }

    private void initPauseItems() {
        // We create the elements here to ensure the width and height of the screen is set
        createPlacements();

        // Set-up the renderables for each placement
        for (var placement : placements) {
            placement.initRenderables(this, holder);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        for (var placement : placements) {
            if (!placement.holder.provider().hasRender()) {
                continue;
            }

            placement.render(holder, guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    private void createPlacements() {
        placements.clear();

        // We need to pre-calculate the wrapping aspect of each layout
        HashMap<MenuLocation, Pair<Integer, Integer>> rowsToHeights = new HashMap<>();
        for (Map.Entry<MenuLocation, HashSet<PauseItemHolder>> locationToItems : PauseMenuApi.get().getPauseItems().entrySet()) {
            var location = locationToItems.getKey();
            var items = locationToItems.getValue();

            List<PauseItemHolder> sortedItems = new ArrayList<>(items);
            // Sort the items by their sortOrder placing the highest first
            sortedItems.sort(Comparator.<PauseItemHolder>comparingInt(e -> e.provider().sortingOrder()).reversed());

            int xOffset = 0;
            int yOffest = 0;
            int widestWidth = 0;

            int highestHeight = 0;
            int targetMaxWidth = calculateMaxWidth(location);

            for (var item : sortedItems) {
                if (item.provider().disabled()) {
                    continue;
                }

                var itemWidth = item.provider().width();
                var itemHeight = item.provider().height();

                boolean hasOverflowed = false;
                if (xOffset + itemWidth > targetMaxWidth) {
                    xOffset = 0;
                    hasOverflowed = true;
                    yOffest += highestHeight + 4;
                    highestHeight = 0;
                }

                highestHeight = Math.max(highestHeight, itemHeight);

                xOffset += itemWidth + (hasOverflowed ? -4 : 4);
                widestWidth = Math.max(widestWidth, xOffset - 4);
            }

            yOffest += highestHeight;
            rowsToHeights.put(location, Pair.of(widestWidth, yOffest));
        }

        // Now we can compute the render positions
        for (Map.Entry<MenuLocation, HashSet<PauseItemHolder>> locationToItems : PauseMenuApi.get().getPauseItems().entrySet()) {
            var location = locationToItems.getKey();
            var items = locationToItems.getValue();

            List<PauseItemHolder> sortedItems = new ArrayList<>(items);
            // Sort the items by their sortOrder placing the highest first
            sortedItems.sort(Comparator.<PauseItemHolder>comparingInt(e -> e.provider().sortingOrder()).reversed());

            int xOffset = 0;
            int yOffset = 0;

            int maxWidth = calculateMaxWidth(location);
            int rowHighestHeight = 0;

            boolean isRightAligned = switch (location) {
                case TOP_RIGHT, BOTTOM_RIGHT, MENU_LEFT -> true;
                default -> false;
            };

            boolean isCenterAligned = switch (location) {
                case TOP_CENTER, BOTTOM_CENTER -> true;
                default -> false;
            };

            boolean isBottomAligned = switch (location) {
                case BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM_CENTER -> true;
                default -> false;
            };

            for (var item : sortedItems) {
                if (item.provider().disabled()) {
                    continue;
                }

                var itemWidth = item.provider().width();
                var itemHeight = item.provider().height();

                int[] position = calculatePosition(location);

                int renderX = position[0];
                int renderY = position[1];

                var rowDimensions = rowsToHeights.get(location);
                int rowHeight = rowDimensions.getValue();
                int rowWidth = rowDimensions.getKey();

                if (xOffset + itemWidth > maxWidth) {
                    xOffset = 0;
                    yOffset += rowHighestHeight + 4;
                    rowHighestHeight = 0;
                }

                if (isRightAligned) {
                    renderX -= rowWidth;
                } else if (isCenterAligned) {
                    renderX -= rowWidth / 2;
                }

                if (isBottomAligned) {
                    renderY -= rowHeight;
                }

                rowHighestHeight = Math.max(rowHighestHeight, itemHeight);

                int xPlacement = renderX + xOffset;
                int yPlacement = renderY + yOffset;

                placements.add(new ItemPlacement(item, xPlacement, yPlacement, item.provider().hasRender()));
                xOffset += itemWidth + 4;
            }
        }
    }

    private int[] calculatePosition(MenuLocation target) {
        return switch (target) {
            case TOP_LEFT -> new int[]{4, 4};
            case TOP_RIGHT -> new int[]{this.width - 4, 4};
            case TOP_CENTER -> new int[]{this.width / 2, 4};
            case BOTTOM_LEFT -> new int[]{4, this.height - 4};
            case BOTTOM_RIGHT -> new int[]{this.width - 4, this.height - 4};
            case BOTTOM_CENTER -> new int[]{this.width / 2, this.height - 4};
            case MENU_LEFT -> new int[]{this.width / 2 - 102 - 4, this.height / 4 + 9};
            case MENU_RIGHT -> new int[]{this.width / 2 + 102 + 4, this.height / 4 + 9};
        };
    }

    private int calculateMaxWidth(MenuLocation target) {
        return switch (target) {
            // 3 columns along the x-axis, this should space them out evenly
            case TOP_LEFT, TOP_RIGHT, TOP_CENTER, BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM_CENTER -> this.width / 3;
            case MENU_LEFT, MENU_RIGHT -> 80;
        };
    }

    private record ItemPlacement(PauseItemHolder holder, int x, int y, boolean hasRender) {
        private void initRenderables(Screen screen, ScreenHolder screenHolder) {
            var elements = holder.provider().init(holder.location(), screenHolder, x, y);
            if (elements == null) {
                return;
            }

            elements.commitToScreen(screen);
        }

        private void render(ScreenHolder screenHolder, GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            holder.provider().render(holder.location(), screenHolder, graphics, x, y, mouseX, mouseY, partialTick);
        }
    }
}
