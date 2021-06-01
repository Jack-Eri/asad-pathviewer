package pathviewer.visualization;

import pathviewer.data.Data;
import pathviewer.models.Cell;
import pathviewer.models.Field;
import pathviewer.models.Position;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class FieldPanel extends JPanel {
    private final Data data;

    private final int ERROR_HEIGHT = 40;

    private final Color SOLUTION = new Color(229, 142, 229, 128);
    private final Color START = new Color(20, 122, 17, 128);
    private final Color END = new Color(16, 49, 119, 128);
    private final Color GRID_LINE = new Color(0, 0, 0);

    private final Color SOLUTION_BIS = new Color(229, 142, 229);
    private final Color START_BIS = new Color(20, 122, 17);
    private final Color END_BIS = new Color(16, 49, 119);
    private final Color GRID_LINE_BIS = new Color(128, 128, 128);

    private int tileWidth;
    private int tileHeight;
    public FieldPanel(Data data) {
        this.data = data;

        setBorder(new LineBorder(Color.gray));

//        tileWidth = this.getWidth() / data.getCurrentField().getWidth();
//        tileHeight = this.getHeight() / data.getCurrentField().getHeight();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (data.isLoading()) {
            graphics.setColor(Color.BLACK);
            graphics.drawString("Loading...", this.getWidth() - 80, this.getHeight() - ERROR_HEIGHT / 2);
        }

        if (data.getError() != null) {
            graphics.setColor(new Color(255, 100, 100));
            graphics.drawString(data.getError(), 30, this.getHeight() - ERROR_HEIGHT / 2);
        }

        if (!data.hasField()) {
            graphics.setColor(Color.BLACK);
            graphics.drawString("No field loaded!",30, 30);
            return;
        }

        Field field = data.getCurrentField();

        tileWidth = this.getWidth() / field.getWidth();
        tileHeight = (this.getHeight() - ERROR_HEIGHT) / field.getHeight();

        graphics.drawRect(getWidth() - 1, 0, 1, getHeight());
        graphics.drawRect(0, getHeight() - 1, getWidth(), 1);

        Cell[][] cells = field.getCells();
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.isField()) {
                    int shiftX = cell.getPosition().getX() * tileWidth;
                    int shiftY = cell.getPosition().getY() * tileHeight;
                    int width = tileWidth / cell.getField().getWidth();
                    int height = tileHeight / cell.getField().getHeight();

                    PrintField(cell.getField(), shiftX, shiftY, width, height, false, graphics, true);
                }
            }
        }

        PrintField(field, 0, 0, tileWidth, tileHeight, true, graphics, false);
    }

    private void PrintField(Field field, int shiftX, int shiftY, int tileW, int tileH, boolean showCost, Graphics graphics, boolean bis) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Position start = field.getStart();
        Position end = field.getEnd();
        Cell[][] grid = field.getCells();
        Position[] path = field.getPath();

        // Solution

        if (bis) {
            graphics2D.setColor(SOLUTION_BIS);
        } else {
            graphics2D.setColor(SOLUTION);
        }

        for (Position position : path) {
            graphics2D.fillRect(shiftX + position.getX() * tileW, shiftY + position.getY() * tileH, tileW, tileH);
        }

        if (!start.equals(end)) {
            // Start

            if (bis) {
                graphics2D.setColor(START_BIS);
            } else {
                graphics2D.setColor(START);
            }

            graphics2D.fillRect(shiftX + start.getX() * tileW, shiftY + start.getY() * tileH, tileW, tileH);

            // End

            if (bis) {
                graphics2D.setColor(END_BIS);
            } else {
                graphics2D.setColor(END);
            }

            graphics2D.fillRect(shiftX + end.getX() * tileW, shiftY + end.getY() * tileH, tileW, tileH);
        }

        // Grid
        graphics2D.setFont(new Font("Courrier", Font.PLAIN, 18));
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                int x = cell.getPosition().getX() * tileW + shiftX;
                int y = cell.getPosition().getY() * tileH + shiftY;

                graphics2D.setColor(bis ? GRID_LINE_BIS : GRID_LINE);
                graphics2D.drawRect(x, y, tileW, tileH);

                if (showCost) {
                    graphics2D.setColor(Color.black);
                    graphics2D.drawString(String.format("%2d", cell.getCost()), x + tileW / 2 - 7, y + tileH / 2 + 7);
                }
            }
        }
    }

    public int GetTileWidth() {
        return tileWidth;
    }

    public int GetTileHeight() {
        return tileHeight;
    }
}
