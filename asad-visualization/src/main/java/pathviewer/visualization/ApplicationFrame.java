package pathviewer.visualization;

import pathviewer.models.Position;
import pathviewer.data.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Viz inspired from https://github.com/psikoi/AStar-Pathfinding
public class ApplicationFrame extends JFrame
{

	private final Data data;
	private final FieldPanel fieldPanel;
	private final ControlPanel controlPanel;
    
	public static void main(String[] args)
	{
		ApplicationFrame fv = new ApplicationFrame();
		fv.Redraw();
	}

	public ApplicationFrame() 
	{
		data = new Data();
		fieldPanel = new FieldPanel(data);
		controlPanel = new ControlPanel(this, data, 1080 / 4);
		
		fieldPanel.addMouseListener(new MouseListener() 
		{
            @Override
            public void mouseClicked(MouseEvent me) 
            {
            	
            }

            @Override
            public void mousePressed(MouseEvent me) 
            {
            	if (!data.hasField()) return;

            	data.setLoading(true);
            	Redraw();

                Position pos = new Position(me.getX() / fieldPanel.GetTileWidth(), me.getY() / fieldPanel.GetTileHeight());
                
                switch(controlPanel.getSelectionType())
                {
					case END:
						data.setEnd(pos);
						break;
					case START:
						data.setStart(pos);
						break;
					case COST:
						int cost = data.getCurrentField().getCell(pos).getCost();
						boolean changed = false;

						switch (me.getButton()) {
							case MouseEvent.BUTTON1:
								cost += 1;
								if (cost > 99) {
									cost = 1;
								}
								changed = true;
								break;

							case MouseEvent.BUTTON3:
								cost -= 1;
								if (cost < 1) {
									cost = 99;
								}
								changed = true;
								break;

							case MouseEvent.BUTTON2:
								cost = 1;
								changed = true;
								break;
						}

						if (changed) {
							data.setCost(pos, cost);
						}
						break;
						
					case ZOOM_IN:
						data.zoomIn(pos);
						break;
						
					case ZOOM_OUT:
						data.zoomOut();
						break;
						
					case CHANGE_CELL_TO_FIELD:
						data.AddField(pos, controlPanel.GetFieldWidth(), controlPanel.GetFieldHeight());
						break;

					case CHANGE_FIELD_TO_CELL:
						data.setCell(pos);
						break;
						
					default:
						break;
				}

				data.setLoading(false);
                Redraw();
            }

            @Override
            public void mouseReleased(MouseEvent me)
            {
            	
            }

            @Override
            public void mouseEntered(MouseEvent me)
            {
            	
            }

            @Override
            public void mouseExited(MouseEvent me)
            {
            	
            }
        });
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		this.setPreferredSize(new Dimension(1080, 720));
		controlPanel.setPreferredSize(new Dimension(1080 / 4, 720));
        
		this.add(fieldPanel, BorderLayout.CENTER);
		this.add(controlPanel, BorderLayout.EAST);
        this.setVisible(true);
        this.pack();
	}

	public void Redraw()
	{
		fieldPanel.repaint();
	}	
}
